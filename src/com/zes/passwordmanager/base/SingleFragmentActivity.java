package com.zes.passwordmanager.base;

import com.zes.passwordmanager.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public abstract class SingleFragmentActivity extends BaseActivity {
	protected abstract Fragment createFragment();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_fragment);
		
		FragmentManager fm = getSupportFragmentManager();
		Fragment containerFragment = fm.findFragmentById(R.id.fragment_container);
		if(containerFragment == null){
			containerFragment = createFragment();
			fm.beginTransaction()
			.add(R.id.fragment_container, containerFragment)
			.commit();
		}
	}
}
