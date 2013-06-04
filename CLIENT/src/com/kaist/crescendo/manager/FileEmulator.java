package com.kaist.crescendo.manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.json.JSONObject;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class FileEmulator {
	
	public boolean checkFileExist(String fileName){
		boolean result = false;
		String path = Environment.getExternalStorageDirectory().getAbsolutePath();
		
		File file = new File(path+"/"+fileName);
		
		if(file.exists()) {
			result = true;
		}
		
		return result;
	}
	
	public void parseJONtoString(String uId, JSONObject jObject) {
		String jsonString = "";
		String fileName;
		jsonString = jObject.toString();
		
		try {
			String path = Environment.getExternalStorageDirectory().getAbsolutePath();
			fileName = uId;
			File jsonFile = new File(path+"/"+fileName);
			jsonFile.createNewFile();
			FileOutputStream fOut = new FileOutputStream(jsonFile);
			OutputStreamWriter OutWriter = new OutputStreamWriter(fOut);
			
			OutWriter.append(jsonString);
			OutWriter.close();
			fOut.close();
		} catch (Exception e) {
			// TODO: handle exception
			Log.v("FileErr","File error" + e.toString());
		}
	}
	
	public String readJSON(String fileName) {
		String buffer = "";
		String jsonString = "";
		
		try {
			String path = Environment.getExternalStorageDirectory().getAbsolutePath();
			File file = new File(path+"/"+fileName);
			FileInputStream fIn = new FileInputStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(fIn));
			
			String dataRow = "";
			
			while((dataRow = reader.readLine()) != null) {
				buffer += dataRow + "\n";
			}
			reader.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		jsonString = buffer;
		return jsonString;
	}
}
