package com.kaist.crescendo.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
	static final String BOOT_ACTION = "android.intent.action.BOOT_COMPLETED";
	static final String INSTALL_ACTION = "android.intent.action.PACKAGE_INSTALL";
	static final String ADDED_ACTION = "android.intent.action.PACKAGE_ADDED";
	static final String REPLACED_ACTION = "android.intent.action.PACKAGE_REPLACED";
	static final String REMOVED_ACTION = "android.intent.action.PACKAGE_REMOVED";
	static final String START_ACTION = "android.intent.action.com.kaist.crescendo.START_SERVICE";
	static final String STOP_ACTION = "android.intent.action.com.kaist.crescendo.STOP_SERVICE";
	
	private final String TAG = "BootReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getAction().equals(BOOT_ACTION)) {
			Log.v(TAG, "onReceive invoked AlarmService when system is booted");
			context.startService(new Intent(START_ACTION));
			
		}
	}

}
