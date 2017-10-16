package com.zes.passwordmanager.base;

import com.zes.passwordmanager.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;

public abstract class BaseListDialogFragment extends DialogFragment implements OnItemClickListener {
	private ListView mListView;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_base, null);
		
		mListView = (ListView)v.findViewById(android.R.id.list);
		mListView.setAdapter(createAdapter());
		mListView.setOnItemClickListener(this);
		
		return new AlertDialog.Builder(getContext(), AlertDialog.THEME_HOLO_LIGHT)
				.setView(v)
				.create();
	}
	
	protected ListView getListView() {
		return mListView;
	}
	
	protected ListAdapter getListAdapter() {
		if(mListView != null) {
			return mListView.getAdapter();
		}else {
			return null;
		}
	}
	
	public abstract ListAdapter createAdapter();
	
	public abstract int getAdapterCount();
	
	public abstract Object getAdapterItem(int position);
}
