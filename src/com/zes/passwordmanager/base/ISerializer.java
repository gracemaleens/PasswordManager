package com.zes.passwordmanager.base;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import android.content.Context;
import android.text.TextUtils;

public class ISerializer {
	private static final String TAG = ISerializer.class.getName();
	
	public static String loadDataOfLocal(Context context, String filename)throws IOException{
		FileInputStream input = null;
		InputStreamReader reader = null;
		BufferedReader bufferedReader = null;
		try {
			input = context.openFileInput(filename);
			reader = new InputStreamReader(input);
			bufferedReader = new BufferedReader(reader);
			StringBuffer stringBuffer = new StringBuffer();
			String line = null;
			while((line = bufferedReader.readLine()) != null){
				stringBuffer.append(line);
			}
			return stringBuffer.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(input != null){
				input.close();
			}
			if(reader != null){
				reader.close();
			}
			if(bufferedReader != null){
				bufferedReader.close();
			}
		}
		return "";
	}
	
	public static void saveDataOfLocal(Context context, String filename, String data) throws IOException{
		if(TextUtils.isEmpty(data)){
			return;
		}
		
		OutputStream output = null;
		OutputStreamWriter writer = null;
		try {
			output = context.openFileOutput(filename, Context.MODE_PRIVATE);
			writer = new OutputStreamWriter(output);
			writer.write(data);
			writer.flush();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if(output != null){
				output.close();
			}
			if(writer != null){
				writer.close();
			}
		}
	}
}
