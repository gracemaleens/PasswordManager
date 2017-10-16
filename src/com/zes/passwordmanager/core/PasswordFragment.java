package com.zes.passwordmanager.core;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import com.zes.passwordmanager.R;
import com.zes.passwordmanager.base.DateTimePickerDialogFragment;
import com.zes.passwordmanager.base.SimpleListDialogFragment;
import com.zes.passwordmanager.tools.ITools;
import com.zes.passwordmanager.tools.TimeUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.view.ActionMode.Callback;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PasswordFragment extends Fragment {
	private static final String TAG = PasswordFragment.class.getName();
	
	private static final int REQUEST_CODE_DATETIME = 0;
	
	private static final int HANDLER_UPDATE = 0;
	
	public static final String KEY_ID = PasswordFragment.class.getName() + ".key_id";
	
	private EditText mTitleEditText;
	private EditText mPasswordEditText;
	private EditText mNotesEditText;
	private Button mDatetimeButton;
	
	private Password mPassword;
	private Timer mUpdateTimer;
	private PasswordHandler mHandler;
	
	public static PasswordFragment newInstance(UUID id) {
		PasswordFragment fragment = new PasswordFragment();
		Bundle args = new Bundle();
		args.putSerializable(KEY_ID, id);
		fragment.setArguments(args);
		
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		if(getArguments() != null) {
			UUID id = (UUID)getArguments().getSerializable(KEY_ID);
			mPassword = PasswordManager.getInstance().getPassword(id);
		}
		if(mPassword == null) {
			mPassword = new Password();
		}
		mHandler = new PasswordHandler();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_password, container, false);
		
		Toolbar toolbar = (Toolbar)v.findViewById(R.id.password_toolbar);
		toolbar.setTitle("");
		TextView title = (TextView)toolbar.findViewById(R.id.toolbar_title);
		if(!PasswordManager.getInstance().contain(mPassword)) {
			title.setText(R.string.password_toolbar_title_1);
		}else {
			title.setText(R.string.password_toolbar_title_2);
		}
		
		((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
		if(Build.VERSION.SDK_INT < 16) {
			if(NavUtils.getParentActivityName(getActivity()) != null) {
				((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}else {
			((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		setHasOptionsMenu(true);
		
		mTitleEditText = (EditText)v.findViewById(R.id.password_title_editText);
		mTitleEditText.setText(mPassword.getTitle());
		if(!TextUtils.isEmpty(mTitleEditText.getText())) {
			LinearLayout parentLayout = (LinearLayout)(mTitleEditText.getParent());
			parentLayout.setFocusable(true);
			parentLayout.setFocusableInTouchMode(true);
		}else {
			ITools.showSoftInput(mTitleEditText);
		}
		mTitleEditText.addTextChangedListener(new PasswordTextWatcher(mTitleEditText));
		
		mPasswordEditText = (EditText)v.findViewById(R.id.password_password_editText);
		mPasswordEditText.setText(mPassword.getPassword());
		mPasswordEditText.addTextChangedListener(new PasswordTextWatcher(mPasswordEditText));
		
		mNotesEditText = (EditText)v.findViewById(R.id.password_notes_editText);
		mNotesEditText.setText(mPassword.getNotes());
		mNotesEditText.addTextChangedListener(new PasswordTextWatcher(mNotesEditText));
		
		mDatetimeButton = (Button)v.findViewById(R.id.password_datetime_button);
		mDatetimeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TimeLockDialogFragment fragment = new TimeLockDialogFragment();
				fragment.setTargetFragment(PasswordFragment.this, REQUEST_CODE_DATETIME);
				fragment.show(getFragmentManager(), DateTimePickerDialogFragment.TAG);
			}
		});
		
		timeluck(mPassword.isTimeLock());
		update();
		
		return v;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		inflater.inflate(R.menu.menu_toolbar_password, menu);
		MenuItem deleteItem = menu.findItem(R.id.menu_toolbar_password_delete);
		if(PasswordManager.getInstance().contain(mPassword)) {
			deleteItem.setVisible(true);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.home:
			if(Build.VERSION.SDK_INT < 16) {
				if(NavUtils.getParentActivityName(getActivity()) != null) {
					NavUtils.navigateUpFromSameTask(getActivity());
				}
				return true;
			}
			break;
		case R.id.menu_toolbar_password_delete:
			new AlertDialog.Builder(getContext())
			.setMessage(getResources().getString(R.string.password_dialog_delete_msg_isDelete))
			.setPositiveButton(R.string.password_dialog_delete_button_ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					PasswordManager.getInstance().removePassword(getContext(), mPassword);
					getActivity().finish();
				}
			})
			.setNeutralButton(R.string.password_dialog_delete_button_cancel, null)
			.show();
			return true;
		case R.id.menu_toolbar_password_save:
			String msg = "";
			if(TextUtils.isEmpty(mPassword.getTitle())) {
				msg = getResources().getString(R.string.password_dialog_save_msg_noTitle);
			}
			if(TextUtils.isEmpty(msg) && TextUtils.isEmpty(mPassword.getPassword())) {
				msg = getResources().getString(R.string.password_dialog_save_msg_noPassword);
			}
			if(!TextUtils.isEmpty(msg)) {
				new AlertDialog.Builder(getContext())
				.setMessage(msg)
				.setPositiveButton(R.string.password_dialog_save_button_ok, null)
				.show();
			}else {
				PasswordManager.getInstance().addPassword(getContext(), mPassword);
				getActivity().finish();
			}
			return true;
		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch(requestCode) {
		case REQUEST_CODE_DATETIME:
			if(resultCode == Activity.RESULT_OK) {
				int position = data.getIntExtra(SimpleListDialogFragment.EXTRA_ITEM_POSITION, 0);
				mPassword.setTimeLock(position);
				String text = Password.getTimelocksValue(position);
				mDatetimeButton.setText(text);
			}
			break;
		}
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		if(mUpdateTimer != null) {
			mUpdateTimer.cancel();
			mUpdateTimer = null;
		}
	}
	
	private void update() {
		mUpdateTimer = new Timer();
		mUpdateTimer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message msg = mHandler.obtainMessage(HANDLER_UPDATE);
				msg.sendToTarget();
				Log.d(TAG, "update");
				if(!mPassword.isTimeLock()) {
					mUpdateTimer.cancel();
					mUpdateTimer = null;
				}
			}
		}, 0, 1000);
	}
	
	private void timeluck(boolean timeluck) {
		if(timeluck) {
			TimeUtils tu = TimeUtils.getInstance();
			tu.setTimeDifference(mPassword.getUnlockRemainingTime());
			String text = getResources().getString(R.string.password_datetime_2);
			text = String.format(text, tu.getTimeDifferenceOfDays(), tu.getTimeDifferenceOfHours(),
					tu.getTimeDifferenceOfMinutes(), tu.getTimeDifferenceOfSeconds());
			mDatetimeButton.setText(text);
		}else {
			mDatetimeButton.setText(Password.getTimelocksValue(mPassword.getTimeLockOfOrdinal()));
			mPasswordEditText.setText(mPassword.getPassword());
		}
		mPasswordEditText.setEnabled(!timeluck);
		mDatetimeButton.setEnabled(!timeluck);
	}
	
	private class PasswordTextWatcher implements TextWatcher{
		private View mView;
		
		public PasswordTextWatcher(View v) {
			mView = v;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			if(mView == null) {
				return;
			}
			
			switch(mView.getId()) {
			case R.id.password_title_editText:
				mPassword.setTitle(s.toString());
				break;
			case R.id.password_password_editText:
				mPassword.setPassword(s.toString());
				break;
			case R.id.password_notes_editText:
				mPassword.setNotes(s.toString());
				break;
			}
		}
	}
	
	private class PasswordHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case HANDLER_UPDATE:
				timeluck(mPassword.isTimeLock());
				break;
			default:
				break;
			}
		}
	}
}
