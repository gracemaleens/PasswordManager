package com.zes.passwordmanager.tools;

import java.util.EnumSet;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class ITools {
	
	public static <T extends Enum<T>> T getEnumByOrdinal(Class<T> c, int ordinal){
		EnumSet<T> remindTimesSet = EnumSet.allOf(c);
		if(ordinal < 0 || ordinal >= remindTimesSet.size()){
			throw new ArrayIndexOutOfBoundsException();
		}
		
		for(T item : remindTimesSet){
			if(item.ordinal() == ordinal){
				return item;
			}
		}
		return null;
	}
	
	public static void showSoftInput(final View view) {
		new Handler(Looper.getMainLooper()).postDelayed(new Runnable() { // 异步弹出软键盘
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				InputMethodManager manager = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				manager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
			}
		}, 200);
	}
	
}
