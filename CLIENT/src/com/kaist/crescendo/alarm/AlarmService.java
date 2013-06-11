package com.kaist.crescendo.alarm;

import java.nio.channels.AlreadyConnectedException;
import java.util.ArrayList;
import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class AlarmService extends Service {
	static private String TAG = "AlarmService";
	
	private List<Integer> alarmList;
	
	public static final String INTENT_ACTION = "android.intent.action.com.kaist.crescendo.ON_BIND";
	
	// IAlarmService interface ÀÌ´Ù. 
	private IAlarmService.Stub serviceStub = new IAlarmService.Stub() {

		@Override
		public int addAlarm(int planId, int dayOfWeek, String alarmTime)
				throws RemoteException {
			// TODO Auto-generated method stub
			Log.v(TAG, "[addAlarm]" + "planId : " + planId + ", dayOfWeek : " + dayOfWeek + ", alarmTime : " + alarmTime);
			addAlarmService(planId, dayOfWeek, alarmTime);
			return 0;
		}
		
		@Override
		public int updateAlarm(int planId, int dayOfWeek, String alarmTime)
				throws RemoteException {
			// TODO Auto-generated method stub
			Log.v(TAG, "[updateAlarm]" + "planId : " + planId + ", dayOfWeek : " + dayOfWeek + ", alarmTime : " + alarmTime);
			updateAlarmService(planId, dayOfWeek, alarmTime);
			return 0;
		}

		@Override
		public int delAlarm(int planId) throws RemoteException {
			// TODO Auto-generated method stub
			Log.v(TAG, "[delAlarm]" + "planId : " + planId);
			deleteAlarmService(planId);
			return 0;
		}

		@Override
		public boolean registerCallback(IAlarmServiceCallback callback)
				throws RemoteException {
			// TODO Auto-generated method stub
			Log.v(TAG, "[registerCallback]");
			return false;
		}

		@Override
		public boolean unreigsterCallback(IAlarmServiceCallback callback)
				throws RemoteException {
			// TODO Auto-generated method stub
			Log.v(TAG, "[unreigsterCallback]");
			return false;
		}
    };
    
    private int addAlarmService(int planId, int dayOfWeek, String alarmTime) {
    	int result = 0;
    	// add new alarm to db and set to alarm manager
    	
    	AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
    	
    	Intent intent = new Intent(this, AlarmReceiver.class);
    	PendingIntent sender = PendingIntent.getBroadcast(this, planId, intent, 0);
    	
    	alarmList.add(planId);
    	
    	return result;
    }
    
    private int updateAlarmService(int planId, int dayOfWeek, String alarmTime) {
    	int result = 0;
    	
    	return result;
    }
    
    private int deleteAlarmService(int uId) {
    	int result = 0;
    	
    	return result;
    }
    
	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG,"-----------> AlarmService: onBind()...");
		return serviceStub;
	}
	
	@Override
	public void onCreate() {
		Log.i(TAG,"-----------> AlarmService: onCreate()...");
		alarmList = new ArrayList<Integer>();
		super.onCreate();
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		Log.i(TAG,"-----------> AlarmService: onUnbind()...");
		return super.onUnbind(intent);
	}
	
	@Override
	public void onDestroy() {
		Log.i(TAG,"-----------> AlarmService: onDestroy()...");
		super.onDestroy();
	}
}
