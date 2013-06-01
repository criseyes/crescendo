package com.kaist.crescendo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.kaist.crescendo.manager.UpdateManager;
import com.kaist.crescendo.manager.UpdateManagerInterface;

public class UpdateActivity extends Activity {
	private final static UpdateManagerInterface mManager = new UpdateManager(); // singleton
	private Context mContext;
	
	public static UpdateManagerInterface getInstance() 
	{
	     return mManager;
	}
	
	protected String login(String id, String pw)
	{		
		String sessionId = new String();
		int ret = -1;
		sessionId = "bad";
		/*
		 *  TODO call manager's method to login.
		 */
		// test
		ret = mManager.login(mContext, id, pw);
		if(ret == 0) {
			sessionId = "good";
		}
		return sessionId;
	}
	
	protected String register(String id, String pw, String phone, String birth)
	{
		String result = new String();
		int ret = -1;
		result = "bad";
		/*
		 *  TODO call manager's method to register
		 */
		
		ret = mManager.register(mContext, id, pw, phone, birth);
		if(ret == 0) {
			result = "good";
		}
		return result;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this; /* save context for each activity */
	}
	
	@Override
	public void finish() {
		// override to show the animation effect
		super.finish();
		//makeEffect();
	}
	
	@Override
	public void startActivity(Intent intent) {
		// override to show the animation effect
		super.startActivity(intent);
		makeEffect();
	}
	
	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		// override to show the animation effect
		super.startActivityForResult(intent, requestCode);
		makeEffect();
	}
	
	private void makeEffect()
	{
		//overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_in_left);
	}
}
