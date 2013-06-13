package com.kaist.crescendo.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.Window;
import android.widget.ImageView;

import com.kaist.crescendo.R;
import com.kaist.crescendo.utils.MyPref;

public class IntroActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);	
		setContentView(R.layout.activity_intro);
		
		Handler handler = new Handler();
		handler.postDelayed(run, 2500);
	}
	
	Runnable run = new Runnable() {
		
		@Override
		public void run() {
			/* Call Register Activity when session is not established */
			SharedPreferences prefs = getSharedPreferences(MyPref.myPref, MODE_MULTI_PROCESS);
			boolean saved = prefs.getBoolean(MyPref.KEY_SESSION, false);
			String saved_phone = prefs.getString(MyPref.KEY_PHONE, "0");
			
			/* Get phone number, it's unique id */
			TelephonyManager systemService = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
	        String PhoneNumber = systemService.getLine1Number(); 
	        
	        if(PhoneNumber != null)
	        {
		        if(PhoneNumber.length() > 10) {
			        PhoneNumber = PhoneNumber.substring(PhoneNumber.length()-10,PhoneNumber.length());
			        PhoneNumber="0"+PhoneNumber;
		        }
		        
		        if(saved_phone.equals(PhoneNumber) == false) /* if SIM is changed, it'll be required new session */
		        	saved = false;
	        }
			
			if(saved == false)
			{
				Intent intent = new Intent();
				startActivity(intent.setClass(getApplicationContext(), EntranceActivity.class));
			}
			else {
				Intent intent = new Intent();
				intent.putExtra("login", false);
				startActivity(intent.setClass(getApplicationContext(), MainActivity.class));
			}
			finish();
		}
	};
	
	@Override
	public void onBackPressed() {
		//super.onBackPressed(); // it's intended
		// consume back key
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		ImageView img = (ImageView)findViewById(R.id.imageanimation);
		AnimationDrawable frameAnimation =    (AnimationDrawable)img.getDrawable();
		frameAnimation.setCallback(img);
		frameAnimation.setVisible(true, true);
		frameAnimation.start();
	}
}
