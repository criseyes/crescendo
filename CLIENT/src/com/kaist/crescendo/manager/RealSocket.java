package com.kaist.crescendo.manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import org.json.JSONObject;
import android.util.Log;

public class RealSocket implements CommunicationInterface {
	
	private String output;

	private void connect(JSONObject msg) {
    	Socket socket = null;
    	try{
    		// Code to send data 
    		socket = new Socket("14.63.227.174", 9999);
    		
    		//ObjectOutputStream objOut = new ObjectOutputStream(socket.getOutputStream());
    		BufferedWriter objOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    		objOut.write(msg.toString());
    		objOut.flush();
    		    		
    		// Code to receive data 
    	//	ObjectInputStream objInput = new ObjectInputStream(socket.getInputStream());
    	//	response = (String)objInput.readObject();
    	//	tv.setText(response);
    	//	handler.post(new Runnable() {
		//		public void run() {
		//			tv.setText(response);
		//		}
    	//	});
    	} catch(Exception ex) {
    		Log.e("家南立加坷幅", ex.getMessage());
    	}
	}
    	
	@Override
	public int write(JSONObject msg) {
		// TODO Auto-generated method stub
		//connect(msg);
		Socket socket = null;
		
		try {
    		// Code to send data 
    		socket = new Socket("14.63.227.174", 9999);
    		
    		//ObjectOutputStream objOut = new ObjectOutputStream(socket.getOutputStream());
    		BufferedWriter objOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    		objOut.write(msg.toString());
    		objOut.flush();
    		
    		BufferedReader objIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			output = objIn.readLine();
			Log.d("read data from Server", output.toString()); 
			
    		
		} catch(Exception ex) {
    		Log.e("家南立加坷幅", ex.getMessage());
    	}
		
		return 0;
	}

	@Override
	public String read() {
		// TODO Auto-generated method stub
		Socket socket = null;
		try {
			socket = new Socket("192.168.0.3", 9999);
			
			BufferedReader networkReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));			
			output = networkReader.readLine();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return output;
	}

}
