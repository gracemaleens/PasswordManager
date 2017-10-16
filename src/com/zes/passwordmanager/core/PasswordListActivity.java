package com.zes.passwordmanager.core;

import com.zes.passwordmanager.base.SingleFragmentActivity;

import android.support.v4.app.Fragment;

public class PasswordListActivity extends SingleFragmentActivity {
	
	@Override
	protected Fragment createFragment() {
		// TODO Auto-generated method stub
		return new PasswordListFragment();
	}

}
