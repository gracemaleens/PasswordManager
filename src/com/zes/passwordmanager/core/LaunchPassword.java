package com.zes.passwordmanager.core;

import android.content.SharedPreferences;
import android.text.TextUtils;

public class LaunchPassword {
	private static LaunchPassword sInstance;
	
	private static final String SP_KEY_LAUNCH_PASSWORD = "launch_password";
	
	private String mLaunchPassword;
	private SharedPreferences mPreferences;
	
	public static synchronized LaunchPassword getInstance() {
		if(sInstance == null) {
			sInstance = new LaunchPassword();
		}
		return sInstance;
	}
	
	private LaunchPassword() {
		mPreferences = StartApplication.getAppDataPreferences();
		loadLaunchPassword();
	}
	
	public void setLaunchPassword(String launchPassword) {
		mLaunchPassword = launchPassword;
		saveLaunchPassword();
	}
	
	public void clearLaunchPassword() {
		if(!isEmptyStartPassword()) {
			mLaunchPassword = null;
			saveLaunchPassword();
		}
	}
	
	public boolean checkStartPassword(String startPassword) {
		if(!isEmptyStartPassword()) {
			if(mLaunchPassword.equals(startPassword)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isEmptyStartPassword() {
		return TextUtils.isEmpty(mLaunchPassword);
	}
	
	private void loadLaunchPassword() {
		mLaunchPassword = mPreferences.getString(SP_KEY_LAUNCH_PASSWORD, "");
	}
	
	private void saveLaunchPassword() {
		mPreferences.edit().putString(SP_KEY_LAUNCH_PASSWORD, mLaunchPassword).commit();
	}
	
}
