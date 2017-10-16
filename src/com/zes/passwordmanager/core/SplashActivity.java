package com.zes.passwordmanager.core;

import com.zes.passwordmanager.R;
import com.zes.passwordmanager.base.BaseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends BaseActivity {
	private int mCount = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		StartApplication.launchCount();
		
		final Handler h = new Handler();
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(++mCount == 2) {
					Intent i = new Intent();
					if(StartApplication.isFirstLaunch()) {
						i.setClass(SplashActivity.this, SettingLaunchPasswordActivity.class);
					}else {
						if(LaunchPassword.getInstance().isEmptyStartPassword()) {
							i.setClass(SplashActivity.this, PasswordListActivity.class);
						}else {
							i.setClass(SplashActivity.this, LaunchPasswordActivity.class);
						}
					}
					startActivity(i);
					finish();
				}else {
					h.postDelayed(this, 1000);
				}
			}
		};
		h.postDelayed(r, 1000);
		
	}
	
}
