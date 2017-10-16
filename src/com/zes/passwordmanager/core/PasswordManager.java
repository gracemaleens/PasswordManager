package com.zes.passwordmanager.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zes.passwordmanager.base.ISerializer;

import android.content.Context;

public class PasswordManager {
	private static PasswordManager sInstance;

	private ArrayList<Password> mPasswords = new ArrayList<Password>();

	private static final String JOSN_FILENAME = "Password.json";
	private static final String JSON_ID = "id";
	private static final String JSON_TITLE = "title";
	private static final String JSON_PASSWORD = "password";
	private static final String JSON_NOTES = "notes";
	private static final String JSON_START_TIME = "start_time";
	private static final String JSON_TIMELOCK = "timelock";

	public static PasswordManager getInstance() {
		if (sInstance == null) {
			sInstance = new PasswordManager();
		}
		return sInstance;
	}

	public void addPassword(Context context, Password password) {
		password.setStartTime(new Date());
		if (!contain(password)) {
			mPasswords.add(password);
		}
		save(context);
	}

	public boolean removePassword(Context context, Password password) {
		boolean b = false;
		if(contain(password)) {
			b = mPasswords.remove(password);
			save(context);
		}else {
			return b;
		}
		return b;
	}
	
	public void removePasswords(Context context, ArrayList<Password> passwords) {
		if(!passwords.isEmpty()) {
			for(Password p : passwords) {
				removePassword(context, p);
			}
		}
	}

	public void removePassword(Context context, int index) {
		mPasswords.remove(index);
		save(context);
	}

	public Password getPassword(int index) {
		if (index >= 0 && index < mPasswords.size()) {
			return mPasswords.get(index);
		}
		return null;
	}

	public Password getPassword(UUID id) {
		for (Password p : mPasswords) {
			if (p.getId().equals(id)) {
				return p;
			}
		}
		return null;
	}

	public boolean contain(Password password) {
		for (Password p : mPasswords) {
			if (p.getId().equals(password.getId())) {
				return true;
			}
		}
		return false;
	}
	
	public int getPasswordCount() {
		return mPasswords.size();
	}

	public void save(Context context) {
		try {
			JSONArray jsonArray = new JSONArray();
			for (Password p : mPasswords) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(JSON_TITLE, p.getTitle());
				jsonObject.put(JSON_ID, p.getId());
				jsonObject.put(JSON_PASSWORD, p.getPasswordForSave());
				jsonObject.put(JSON_NOTES, p.getNotes());
				jsonObject.put(JSON_START_TIME, p.getStartTimeFormat());
				jsonObject.put(JSON_TIMELOCK, p.getTimeLockOfOrdinal());
				jsonArray.put(jsonObject);
			}
			ISerializer.saveDataOfLocal(context, JOSN_FILENAME, jsonArray.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void load(Context context) {
		try {
			JSONArray jsonArray = new JSONArray(ISerializer.loadDataOfLocal(context, JOSN_FILENAME));
			if(jsonArray != null) {
				if(!mPasswords.isEmpty()) {
					mPasswords.clear();
				}
			}
			for(int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.optJSONObject(i);
				Password p = new Password();
				p.setId(jsonObject.optString(JSON_ID));
				p.setTitle(jsonObject.optString(JSON_TITLE));
				p.setPassword(jsonObject.optString(JSON_PASSWORD));
				p.setNotes(jsonObject.optString(JSON_NOTES));
				p.setTimeLock(jsonObject.optInt(JSON_TIMELOCK));
				p.setStartTime(jsonObject.optString(JSON_START_TIME));
				mPasswords.add(p);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
