package com.kaist.crescendo.manager;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;
import org.json.JSONObject;
import android.util.Log;

public class RealSocket implements CommunicationInterface {

	private void connect(JSONObject msg) {
    	Socket socket = null;
    	try{
    		// Code to send data 
    		socket = new Socket("192.168.0.3", 9999);
    		
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
    	}catch(Exception ex) {
    		Log.e("소켓접속오류", ex.getMessage());
    	}
	}
    	
	@Override
	public int write(JSONObject msg) {
		// TODO Auto-generated method stub
		connect(msg);
		return 0;
	}

	@Override
	public String read() {
		// TODO Auto-generated method stub
		return null;
	}

}
