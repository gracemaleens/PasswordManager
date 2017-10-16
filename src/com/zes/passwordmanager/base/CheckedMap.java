package com.zes.passwordmanager.base;

import java.util.HashMap;

public class CheckedMap extends HashMap<Integer, Boolean>{
	@Override
	public Boolean remove(Object key) {
		// TODO Auto-generated method stub
		Boolean value = super.remove(key);
		int index = Integer.parseInt(key.toString());
		while(index < size()){
			put(index, get(++index));
			if(size()-1 == index){
				break;
			}
		}
		super.remove(index);
		
		return value;
	}
}
