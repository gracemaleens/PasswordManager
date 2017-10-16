package com.zes.passwordmanager.core;

import com.zes.passwordmanager.base.SimpleListDialogFragment;
import com.zes.passwordmanager.core.Password.Timelock;

public class TimeLockDialogFragment extends SimpleListDialogFragment {

	@Override
	public String getItemText(int position) {
		// TODO Auto-generated method stub
		return Password.getTimelocksValue(position);
	}

	@Override
	public int getAdapterCount() {
		// TODO Auto-generated method stub
		return Password.getTimelocksSize();
	}

	@Override
	public Object getAdapterItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

}
