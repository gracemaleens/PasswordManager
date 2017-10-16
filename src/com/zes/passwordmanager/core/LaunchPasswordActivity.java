package com.zes.passwordmanager.core;

import com.zes.passwordmanager.R;
import com.zes.passwordmanager.base.BaseActivity;
import com.zes.passwordmanager.tools.ITools;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LaunchPasswordActivity extends BaseActivity {
	private EditText mLaunchPasswordEditText;
	private Button mLaunchPasswordButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch_password);
		
		mLaunchPasswordEditText = (EditText)findViewById(R.id.launch_password_editText);
		ITools.showSoftInput(mLaunchPasswordEditText);
		mLaunchPasswordButton = (Button)findViewById(R.id.launch_password_button);
		mLaunchPasswordButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String msg = "";
				if(TextUtils.isEmpty(mLaunchPasswordEditText.getText().toString())) {
					msg = "请输入密码！";
				}
				if(TextUtils.isEmpty(msg)) {
					if(!LaunchPassword.getInstance().checkStartPassword(mLaunchPasswordEditText.getText().toString())) {
						msg = "密码输入错误，请重新输入！";
					}
				}
				if(TextUtils.isEmpty(msg)) {
					Intent i = new Intent(LaunchPasswordActivity.this, PasswordListActivity.class);
					startActivity(i);
					finish();
				}else {
					Toast.makeText(LaunchPasswordActivity.this, msg, Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}
