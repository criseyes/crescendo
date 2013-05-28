package com.kaist.crescendo.activity;

import android.app.Activity;
import android.content.Context;

import com.kaist.crescendo.manager.UpdateManager;
import com.kaist.crescendo.manager.UpdateManagerInterface;

public class UpdateActivity extends Activity {
	private final static UpdateManagerInterface mManager = new UpdateManager(); // singletone
	private static Context mContext;
	
	public static UpdateManagerInterface getInstance() 
	{
	     return mManager;
	}
	
	protected String login(String id, String pw)
	{
		setContext(getApplicationContext());
		String sessionId = new String();
		/*
		 *  TODO call manager's method to login.
		 */
		return sessionId;
	}

	private void setContext(Context applicationContext) {
		// TODO Auto-generated method stub
		mContext = applicationContext;
	}
}
