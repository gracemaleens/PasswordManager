package com.zes.passwordmanager.base;

import com.zes.passwordmanager.R;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

public abstract class SimpleListDialogFragment extends BaseListDialogFragment {
	
	public static final String EXTRA_ITEM_POSITION = "com.zes.passwordmanager.core.SimpleDialogFragment.item.position";
	
	@Override
	public ListAdapter createAdapter() {
		// TODO Auto-generated method stub
		return new RemindTimeBaseAdapter();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		setResult(position);
		dismiss();
	}
	
	private void setResult(int position){
		if(getTargetFragment() == null){
			return;
		}
		
		Intent data = new Intent();
		data.putExtra(EXTRA_ITEM_POSITION, position);
		getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, data);
	}
	
	protected void notifyDataSetChanged() {
		if(getListAdapter() == null) {
			return;
		}
		((RemindTimeBaseAdapter)getListAdapter()).notifyDataSetChanged();
	}
	
	public abstract String getItemText(int position);
	
	private class ViewHolder{
		public TextView title;
	}
	
	private class RemindTimeBaseAdapter extends BaseAdapter{

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
			
			if(convertView == null){
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_simple_list_view_item, parent, false);
				viewHolder = new ViewHolder();
				viewHolder.title = (TextView)convertView.findViewById(R.id.simple_list_view_item_titleTextView);
				convertView.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder)convertView.getTag();
			}
			
			viewHolder.title.setText(getItemText(position));
			
			return convertView;
		}
	}
}
