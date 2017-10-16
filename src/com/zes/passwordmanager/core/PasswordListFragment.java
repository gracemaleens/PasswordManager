package com.zes.passwordmanager.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.zes.passwordmanager.R;
import com.zes.passwordmanager.tools.ITools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PasswordListFragment extends ListFragment {
	private static final String TAG = PasswordListFragment.class.getName();
	
	private static final int REQUEST_CODE_PASSWORD = 0;
	private static final int REQUEST_CODE_SETTING_LAUNCH_PASSWORD = 1;
	
	private ListView mListView;
	private MenuItem mClearLaunchPassword;
	
	private PasswordListAdapter mAdapter = new PasswordListAdapter();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_password_list, container, false);
		
		Toolbar toolbar = (Toolbar)v.findViewById(R.id.password_list_toolbar);
		toolbar.setTitle("");
		TextView title = (TextView)v.findViewById(R.id.toolbar_title);
		title.setText("密码管理");
		((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
		
		setHasOptionsMenu(true);
		
		setListAdapter(mAdapter);
		
		mListView = (ListView)v.findViewById(android.R.id.list);
		mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		mListView.setMultiChoiceModeListener(new PasswordListMultiChoiceModeListener());
		
		return v;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		inflater.inflate(R.menu.menu_toolbar_password_list, menu);
		if(!LaunchPassword.getInstance().isEmptyStartPassword()) {
			MenuItem settingLaunchPassword = menu.findItem(R.id.menu_toolbar_password_list_setting_launch_password);
			settingLaunchPassword.setTitle(R.string.menu_toolbar_password_list_setting_launch_password_2);
		}else {
			mClearLaunchPassword = menu.findItem(R.id.menu_toolbar_password_list_clear_launch_password);
			mClearLaunchPassword.setVisible(false);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub 
		switch(item.getItemId()) {
		case R.id.menu_toolbar_password_list_add:
			Intent i = new Intent(getContext(), PasswordActivity.class);
			startActivityForResult(i, REQUEST_CODE_PASSWORD);
			return true;
		case R.id.menu_toolbar_password_list_setting_launch_password:
			Intent i2 = new Intent(getContext(), SettingLaunchPasswordActivity.class);
			startActivityForResult(i2, REQUEST_CODE_SETTING_LAUNCH_PASSWORD);
			return true;
		case R.id.menu_toolbar_password_list_clear_launch_password:
			clearLaunchPasswordDialog(item);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void clearLaunchPasswordDialog(final MenuItem item) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(getContext())
		.setTitle(R.string.clear_launch_password_title);
		View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_clear_launch_password, null);
		final EditText editText = (EditText)view.findViewById(R.id.clear_launch_password_editText);
		ITools.showSoftInput(editText);
		dialog.setView(view);
		dialog.setPositiveButton(R.string.clear_launch_password_button_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if(LaunchPassword.getInstance().checkStartPassword(editText.getText().toString())) {
					LaunchPassword.getInstance().clearLaunchPassword();
					Toast.makeText(getContext(), "启动密码已清除", Toast.LENGTH_SHORT).show();
					item.setVisible(false);
				}else {
					Toast.makeText(getContext(), "密码错误", Toast.LENGTH_SHORT).show();
				}
			}
		});
		dialog.setNeutralButton(R.string.clear_launch_password_button_cancel, null);
		dialog.show();
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		Intent i = new Intent(getContext(), PasswordActivity.class);
		i.putExtra(PasswordFragment.KEY_ID, PasswordManager.getInstance().getPassword(position).getId());
		startActivityForResult(i, REQUEST_CODE_PASSWORD);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch(requestCode) {
		case REQUEST_CODE_PASSWORD:
			((PasswordListAdapter)getListAdapter()).notifyDataSetChanged();
			break;
		case REQUEST_CODE_SETTING_LAUNCH_PASSWORD:
			if(resultCode == Activity.RESULT_OK) {
				if(!mClearLaunchPassword.isVisible()) {
					mClearLaunchPassword.setVisible(true);
				}
			}
			break;
		}
	}
	
	private class ViewHolder{
		TextView titleTextView;
		CheckBox checkBox;
	}
	
	private class PasswordListAdapter extends BaseAdapter{
		@SuppressLint("UseSparseArrays")
		private Map<Integer, Boolean> mCheckedMap = new HashMap<Integer, Boolean>();
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return PasswordManager.getInstance().getPasswordCount();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return PasswordManager.getInstance().getPassword(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder;
			if(convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_password_list_item, parent, false);
				viewHolder = new ViewHolder();
				viewHolder.titleTextView = (TextView)convertView.findViewById(R.id.password_list_item_title_textView);
				viewHolder.checkBox = (CheckBox)convertView.findViewById(R.id.password_list_item_checkBox);
				convertView.setTag(viewHolder);
			}else {
				viewHolder = (ViewHolder)convertView.getTag();
			}
			
			viewHolder.titleTextView.setText(PasswordManager.getInstance().getPassword(position).getTitle());
			if(!mCheckedMap.isEmpty()) {
				if(viewHolder.checkBox.getVisibility() != View.VISIBLE) {
					viewHolder.checkBox.setVisibility(View.VISIBLE);
				}
				viewHolder.checkBox.setChecked(mCheckedMap.get(position));
			}else {
				if(viewHolder.checkBox.getVisibility() != View.INVISIBLE) {
					viewHolder.checkBox.setVisibility(View.INVISIBLE);
				}
			}
			
			return convertView;
		}
		
		public void initCheckeds() {
			clearCheckeds();
			for(int i = 0; i < getCount(); i++) {
				mCheckedMap.put(i, false);
			}
		}
		
		public void clearCheckeds() {
			if(!mCheckedMap.isEmpty()) {
				mCheckedMap.clear();
			}
		}
		
		public void setChecked(int position, boolean checked) {
			mCheckedMap.put(position, checked);
			notifyDataSetChanged();
		}
		
		public boolean isChecked(int position) {
			if(!mCheckedMap.isEmpty()) {
				return mCheckedMap.get(position);
			}else {
				return false;
			}
		}
		
	}
	
	private class PasswordListMultiChoiceModeListener implements MultiChoiceModeListener {

		@Override
		public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
			// TODO Auto-generated method stub
			mAdapter.initCheckeds();
			return true;
		}
		
		@Override
		public void onDestroyActionMode(android.view.ActionMode mode) {
			// TODO Auto-generated method stub
			mAdapter.clearCheckeds();
		}
		
		@Override
		public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
			// TODO Auto-generated method stub
			mode.getMenuInflater().inflate(R.menu.menu_action_mode_password_list, menu);
			return true;
		}
		
		@Override
		public boolean onActionItemClicked(final android.view.ActionMode mode, MenuItem item) {
			// TODO Auto-generated method stub
			switch (item.getItemId()) {
			case R.id.menu_action_mode_password_list_delete:
				new AlertDialog.Builder(getContext())
				.setMessage(R.string.password_list_dialog_delete_msg_isDelete)
				.setPositiveButton(R.string.password_list_dialog_delete_button_ok, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						ArrayList<Password> deleteCache = new ArrayList<Password>();
						for(int i = 0; i < mAdapter.getCount(); i++) {
							if(mAdapter.isChecked(i)) {
								deleteCache.add(PasswordManager.getInstance().getPassword(i));
							}
						}
						PasswordManager.getInstance().removePasswords(getContext(), deleteCache);
						mode.finish();
					}
				})
				.setNeutralButton(R.string.password_list_dialog_delete_button_cancel, null)
				.show();
				
				return true;
			default:
				break;
			}
			return false;
		}
		
		@Override
		public void onItemCheckedStateChanged(android.view.ActionMode mode, int position, long id, boolean checked) {
			// TODO Auto-generated method stub
			mAdapter.setChecked(position, checked);
		}
		
	}
}
