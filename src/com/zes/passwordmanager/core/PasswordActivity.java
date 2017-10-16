package com.zes.passwordmanager.core;

import java.util.UUID;

import com.zes.passwordmanager.base.SingleFragmentActivity;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.View;

public class PasswordActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		// TODO Auto-generated method stub
		UUID id = (UUID)(getIntent().getSerializableExtra(PasswordFragment.KEY_ID));
		if(id != null) {
			return PasswordFragment.newInstance(id);
		}else {
			return new PasswordFragment();
		}
	}

}
