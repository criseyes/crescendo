package com.kaist.crescendo.alarm;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class AlarmService extends Service {
	static private String TAG = "AlarmService";
	
	public static final String INTENT_ACTION = "android.intent.action.com.kaist.crescendo.ON_BIND";
	
	// IAlarmService interface ÀÌ´Ù. 
	private IAlarmService.Stub serviceStub = new IAlarmService.Stub() {

		@Override
		public int addAlarm(int planId, int dayOfWeek, String alarmTime)
				throws RemoteException {
			// TODO Auto-generated method stub
			Log.v(TAG, "[addAlarm]" + "planId : " + planId + ", dayOfWeek : " + dayOfWeek + ", alarmTime : " + alarmTime);
			return 0;
		}
		
		@Override
		public int updateAlarm(int planId, int dayOfWeek, String alarmTime)
				throws RemoteException {
			// TODO Auto-generated method stub
			Log.v(TAG, "[updateAlarm]" + "planId : " + planId + ", dayOfWeek : " + dayOfWeek + ", alarmTime : " + alarmTime);
			return 0;
		}

		@Override
		public int delAlarm(int planId) throws RemoteException {
			// TODO Auto-generated method stub
			Log.v(TAG, "[delAlarm]" + "planId : " + planId);
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
    
	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG,"-----------> AlarmService: onBind()...");
		return serviceStub;
	}
	
	@Override
	public void onCreate() {
		Log.i(TAG,"-----------> AlarmService: onCreate()...");
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
