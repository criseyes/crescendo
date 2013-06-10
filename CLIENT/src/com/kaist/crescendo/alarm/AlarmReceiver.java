package com.kaist.crescendo.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
	static final String BOOT_ACTION = "android.intent.action.BOOT_COMPLETED";
	static final String START_ACTION = "com.kaist.crescendo.START_SERVICE";
	static final String ALARM_ACTION = "com.kaist.crescendo.ALARM_EXPIRED";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		if(intent.getAction().equals(BOOT_ACTION)) {
			//start AlarmService here
			context.startService(new Intent(START_ACTION));
		}
	}

}
