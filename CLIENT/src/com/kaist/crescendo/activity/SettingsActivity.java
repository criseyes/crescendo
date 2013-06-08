package com.kaist.crescendo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.kaist.crescendo.R;
import com.kaist.crescendo.utils.MyPref;
import com.kaist.crescendo.utils.MyStaticValue;

public class SettingsActivity extends UpdateActivity {

	private boolean isEnabled;
	private boolean isAlarm;
	private boolean isFriendEnabled;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_settings);
		setTitle(R.string.str_settings_title);
		
		findViewById(R.id.setttings_changepw).setOnClickListener(mClickListener);
		findViewById(R.id.settings_help).setOnClickListener(mClickListener);
		
		((Switch) findViewById(R.id.settings_setalarm)).setOnCheckedChangeListener(mSwitchListener);
		((Switch) findViewById(R.id.settings_setfriendwidget)).setOnCheckedChangeListener(mSwitchListener);
		((Switch) findViewById(R.id.settings_setwidget)).setOnCheckedChangeListener(mSwitchListener);
		/*
		 *  TODO, set initial value
		 */
		loadSettings();
		((Switch) findViewById(R.id.settings_setalarm)).setChecked(isAlarm);
		((Switch) findViewById(R.id.settings_setwidget)).setChecked(isEnabled);
		((Switch) findViewById(R.id.settings_setfriendwidget)).setChecked(isFriendEnabled);
	}
	
	private void loadSettings()
	{
		
		SharedPreferences prefs = getSharedPreferences(MyPref.myPref, MODE_MULTI_PROCESS);
		isEnabled = prefs.getBoolean(MyPref.AVATA_ENABLED, false);
		isAlarm = prefs.getBoolean(MyPref.ALARM_NOTI, false);
		isFriendEnabled = prefs.getBoolean(MyPref.FRIEND_ENABLED, false);
	
	}
	
	
	public Switch.OnCheckedChangeListener mSwitchListener = new Switch.OnCheckedChangeListener()

	{

		@Override
		public void onCheckedChanged(CompoundButton v, boolean isChecked) {
			SharedPreferences prefs = getSharedPreferences(MyPref.myPref, MODE_MULTI_PROCESS);
			SharedPreferences.Editor editor = prefs.edit();
			
			switch (v.getId())
           {
           		case R.id.settings_setalarm:
           			editor.putBoolean(MyPref.ALARM_NOTI, isChecked);
           			break;
           		case R.id.settings_setwidget:
           			editor.putBoolean(MyPref.AVATA_ENABLED, isChecked);
           			break;
           		case R.id.settings_setfriendwidget:
           			editor.putBoolean(MyPref.FRIEND_ENABLED, isChecked);
           			break;
           }
			//editor.commit();
			editor.apply();
			sendBroadCasetIntent();
		}
		

	};
	
	private void sendBroadCasetIntent()
	{
		Intent intent = new Intent(MyStaticValue.ACTION_UPDATEWALLPAPER);

		sendBroadcast(intent);
	}
	
	/* handling buttons */
	Button.OnClickListener mClickListener = new View.OnClickListener()
	{
	      public void onClick(View v)
	      {
	    	  Intent intent = new Intent();
	           switch (v.getId())
	           {
	           		case R.id.setttings_changepw:
	           			intent.setClass(getApplicationContext(), PasswordActivity.class);
	           			break;
	           		case R.id.settings_help:
	           			intent.setClass(getApplicationContext(), HelpsActivity.class);	           			
	           			break;
	           }
	           startActivity(intent);
	      }
	};
}
