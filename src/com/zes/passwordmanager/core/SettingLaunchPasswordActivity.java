package com.zes.passwordmanager.core;

import com.zes.passwordmanager.R;
import com.zes.passwordmanager.base.ActivityCollector;
import com.zes.passwordmanager.base.BaseActivity;
import com.zes.passwordmanager.tools.ITools;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SettingLaunchPasswordActivity extends BaseActivity{
	private EditText mOldPasswordEditText;
	private EditText mNewPasswordEditText;
	private EditText mAgainNewPasswordEditText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_launch_password);
		
		Toolbar toolbar = (Toolbar)findViewById(R.id.setting_launch_password_toolbar);
		toolbar.setTitle("");
		TextView textView = (TextView)findViewById(R.id.toolbar_title);
		textView.setText(R.string.setting_launch_password_toolbar_title);
		setSupportActionBar(toolbar);
		
		if(Build.VERSION.SDK_INT < 16) {
			if(NavUtils.getParentActivityName(this) != null) {
				getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}else {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		mOldPasswordEditText = (EditText)findViewById(R.id.setting_launch_password_old_password);
		mNewPasswordEditText = (EditText)findViewById(R.id.setting_launch_password_new_password);
		mAgainNewPasswordEditText = (EditText)findViewById(R.id.setting_launch_password_again_new_password);
		if(LaunchPassword.getInstance().isEmptyStartPassword()) {
			mOldPasswordEditText.setVisibility(View.GONE);
			mNewPasswordEditText.setHint(R.string.setting_launch_password_new_password_hint_2);
			mAgainNewPasswordEditText.setHint(R.string.setting_launch_password_again_new_password_hint_2);
			ITools.showSoftInput(mNewPasswordEditText);
		}else {
			ITools.showSoftInput(mOldPasswordEditText);
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.menu_toolbar_setting_launch_password, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.home:
			if(Build.VERSION.SDK_INT < 16) {
				if(NavUtils.getParentActivityName(this) != null) {
					NavUtils.navigateUpFromSameTask(this);
				}
				return true;
			}
			break;
		case R.id.menu_toolbar_setting_launch_password_confirm:
			String msg = "";
			if(mOldPasswordEditText.getVisibility() != View.GONE) {
				if(!LaunchPassword.getInstance().checkStartPassword(mOldPasswordEditText.getText().toString())) {
					msg = "旧密码输入错误，请重新输入！";
				}
			}
			if(TextUtils.isEmpty(msg)) {
				if(TextUtils.isEmpty(mNewPasswordEditText.getText().toString())) {
					msg = "密码不能为空！";
				}
			}
			if(TextUtils.isEmpty(msg)) {
				if(TextUtils.isEmpty(mAgainNewPasswordEditText.getText().toString())) {
					msg = "确认新密码不能为空，请再输入一次新密码！";
				}
			}
			if(TextUtils.isEmpty(msg)) {
				if(!mAgainNewPasswordEditText.getText().toString().equals(mNewPasswordEditText.getText().toString())) {
					msg = "两次密码不一致，请重新输入！";
				}
			}
			if(TextUtils.isEmpty(msg)) {
				int newPasswordLength = mAgainNewPasswordEditText.getText().toString().length();
				if(newPasswordLength < 6 || newPasswordLength > 16) {
					msg = "密码长度必须为6到16位，请重新输入！";
				}
			}
			if(TextUtils.isEmpty(msg)) {
				LaunchPassword.getInstance().setLaunchPassword(mAgainNewPasswordEditText.getText().toString());
				String text = "密码修改成功";
				if(mOldPasswordEditText.getVisibility() == View.GONE) {
					text = "密码设置成功";
					setResult(Activity.RESULT_OK);
				}
				Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
				if(!ActivityCollector.isExistActivity(PasswordListActivity.class)) {
					Intent i = new Intent(SettingLaunchPasswordActivity.this, PasswordListActivity.class);
					startActivity(i);
				}
				finish();
			}else {
				new AlertDialog.Builder(this)
				.setMessage(msg)
				.setPositiveButton("确定", null)
				.show();
			}
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
