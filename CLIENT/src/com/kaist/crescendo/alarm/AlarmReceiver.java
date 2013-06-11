package com.kaist.crescendo.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
	static final String BOOT_ACTION = "android.intent.action.BOOT_COMPLETED";
	static final String START_ACTION = "android.intent.action.com.kaist.crescendo.START_SERVICE";
	static final String ALARM_ACTION = "android.intent.action.com.kaist.crescendo.ALARM_EXPIRED";
	
	private final String TAG = "AlarmReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.v(TAG, intent.getAction().toString());
		if(intent.getAction().equals(BOOT_ACTION)) {
			//start AlarmService here
			Log.v(TAG, "onReceive invoked Service");
			context.startService(new Intent(START_ACTION));
		}
	}

}
