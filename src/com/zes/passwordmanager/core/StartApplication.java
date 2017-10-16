package com.zes.passwordmanager.core;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class StartApplication extends Application {
	
	private static final String TAG = StartApplication.class.getName();
	
	private static Context mContext;
	private static final String SP_FILENAME = "app_data";
	private static final String SP_KEY_LAUNCH_COUNT = "launch_count";
	
	private static int mLaunchCount;
	
	private static SharedPreferences mPreferences;
	
	public static Context getGlobalApplicationContext() {
		return mContext;
	}
	
	public static SharedPreferences getAppDataPreferences() {
		return mContext.getSharedPreferences(SP_FILENAME, Context.MODE_PRIVATE);
	}
	
	public static boolean isFirstLaunch() {
		return mLaunchCount == 1;
	}
	
	public static void launchCount() {
		mLaunchCount = mPreferences.getInt(SP_KEY_LAUNCH_COUNT, 0);
		mPreferences.edit().putInt(SP_KEY_LAUNCH_COUNT, ++mLaunchCount).commit();
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		mContext = getApplicationContext();
		mPreferences = getAppDataPreferences();
//		launchCount();
		
		PasswordManager.getInstance().load(mContext);
	}

}
