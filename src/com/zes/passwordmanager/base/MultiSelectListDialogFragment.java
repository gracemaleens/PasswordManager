package com.zes.passwordmanager.base;

import java.util.ArrayList;
import java.util.Map.Entry;

import com.zes.passwordmanager.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.TextView;

public abstract class MultiSelectListDialogFragment extends BaseListDialogFragment {
	public static final String EXTRA_CHECKED = "checkeds";
	
	private CheckedMap mCheckedMap = new CheckedMap();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		for(int i = 0; i < getAdapterCount(); i++) {
			mCheckedMap.put(i, false);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		mCheckedMap.put(position, !mCheckedMap.get(position));
		getCheckBoxOfListViewItem(position).setChecked(mCheckedMap.get(position));
	}
	
	@Override
	public ListAdapter createAdapter() {
		// TODO Auto-generated method stub
		return new MultiSelectAdapter();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		setResult();
	}

	public ArrayList<Integer> getCheckedIndexs(){
		ArrayList<Integer> checkedIndexs = new ArrayList<Integer>();
		
		for(Entry<Integer, Boolean> entry : mCheckedMap.entrySet()) {
			if(entry.getValue()) {
				checkedIndexs.add(entry.getKey());
			}
		}
		return checkedIndexs;
	}
	
	private CheckBox getCheckBoxOfListViewItem(int position) {
		return (CheckBox)(getListView().getChildAt(position).findViewById(R.id.multi_select_list_view_item_checkBox));
	}
	
	protected void setChecked(int position, boolean checked) {
		mCheckedMap.put(position, checked);
		notifyDataSetChanged();
	}
	
	protected void setChecked(ArrayList<Integer> positions, boolean checked) {
		for(int position : positions) {
			setChecked(position, checked);
			mCheckedMap.put(position, checked);
		}
		notifyDataSetChanged();
	}
	
	protected void notifyDataSetChanged() {
		if(getListAdapter() == null) {
			return;
		}
		((MultiSelectAdapter)getListAdapter()).notifyDataSetChanged();
	}
	
	private void setResult() {
		if(getTargetFragment() == null) {
			return;
		}
		
		Intent data = new Intent();
		data.putExtra(EXTRA_CHECKED, getCheckedIndexs());
		getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, data);
	}

	public abstract String getItemText(int position);
	
	private class ViewHolder{
		public TextView textView;
		public CheckBox checkBox;
	}
	
	protected class MultiSelectAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return getAdapterCount();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return getAdapterItem(position);
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
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_multi_select_list_view_item, parent, false);
				viewHolder = new ViewHolder();
				viewHolder.textView = (TextView)convertView.findViewById(R.id.multi_select_list_view_item_textView);
				viewHolder.checkBox = (CheckBox)convertView.findViewById(R.id.multi_select_list_view_item_checkBox);
				convertView.setTag(viewHolder);
			}else {
				viewHolder = (ViewHolder)convertView.getTag();
			}
			
			viewHolder.textView.setText(getItemText(position));
			viewHolder.checkBox.setChecked(mCheckedMap.get(position));
			
			return convertView;
		}
		
	}

}
