package com.zes.passwordmanager.base;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import android.annotation.SuppressLint;
import android.app.Activity;

public class ActivityCollector {
	private static Map<Class<?>, Activity> mActivitys = new HashMap<Class<?>, Activity>();
	
	public static void addActivity(Activity activity) {
		mActivitys.put(activity.getClass(), activity);
	}
	
	public static <T extends Activity> void removeActivity(Activity activity) {
		if(mActivitys.containsValue(activity)) {
			mActivitys.remove(activity.getClass());
		}
	}
	
	public static void removeAllActivity() {
		for(Entry<Class<?>, Activity> entry : mActivitys.entrySet()) {
			if(!entry.getValue().isFinishing()) {
				entry.getValue().finish();
			}
		}
		mActivitys.clear();
	}
	
	@SuppressLint("NewApi")
	public static <T extends Activity> boolean isExistActivity(Class<T> c) {
		Activity activity = mActivitys.get(c);
		if(activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
			return true;
		}
		return false;
	}
}
