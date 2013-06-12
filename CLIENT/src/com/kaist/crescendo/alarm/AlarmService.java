package com.kaist.crescendo.alarm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

import com.kaist.crescendo.activity.InputActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class AlarmService extends Service {
	static private String TAG = "AlarmService";
	
	private List<Integer> alarmList;
	
	public static final String INTENT_ACTION = "android.intent.action.com.kaist.crescendo.ON_BIND";
	public static final String EXPIRED_ACTION = "android.intent.action.com.kaist.crescendo.ALARM_EXPIRED";
	
	private SQLiteDatabase db;
	
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
    
    private void setNextAlarm(int planId) {
    	AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
    	
    	Intent intent = new Intent(EXPIRED_ACTION);
    	intent.putExtra("planId", planId);
    	PendingIntent sender = PendingIntent.getBroadcast(this, planId, intent, 0);
    	    	
    	Calendar calendar = Calendar.getInstance();
    	
    	//calendar.add(Calendar.DAY_OF_YEAR, 1);
    	calendar.add(Calendar.MINUTE, 5);
    	//calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONDAY), calendar.get(Calendar.DAY_OF_MONTH),
    	//		calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), 0);
    	
    	calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONDAY), calendar.get(Calendar.DAY_OF_MONTH),
    			calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
 
    	am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
    	Log.v(TAG, "setNextAlarm is started : " + planId);
    	
    	//start InputActivity
    	Intent i = new Intent(this, InputActivity.class);
    	i.putExtra("planId", planId);
    	i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	startActivity(i);
    }
    
    private boolean checkAlarmIsToday(int alarmHour, int alarmMin, int curHour, int curMin) {
    	boolean isToday = true;
    	if(alarmHour > curHour) {
    		isToday = false;
    	} else if(alarmHour == curHour && alarmMin > curMin) {
    		isToday = false;
    	}
    	
    	return isToday;
    }
    
    private void cancelAlarm(int planId) {
    	AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
    	
    	Intent intent = new Intent(EXPIRED_ACTION);
    	intent.putExtra("planId", planId);
    	PendingIntent sender = PendingIntent.getBroadcast(this, planId, intent, 0);
    	
    	am.cancel(sender);
    }
    
    private void setAlarm(int planId, int dayOfWeek, String alarmTime) {
    	
    	AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
    	
    	//Intent intent = new Intent(this, AlarmReceiver.class);
    	Intent intent = new Intent(EXPIRED_ACTION);
    	intent.putExtra("planId", planId);
    	PendingIntent sender = PendingIntent.getBroadcast(this, planId, intent, 0);
    	
    	StringTokenizer st = new StringTokenizer(alarmTime, ":");
    	int alarmHour = Integer.parseInt(st.nextToken());
    	int alarmMin = Integer.parseInt(st.nextToken());
    	
    	Calendar calendar = Calendar.getInstance();
    	
    	boolean isTody = checkAlarmIsToday(alarmHour, alarmMin, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
    	
    	calendar.add(Calendar.MINUTE, 1);
    	calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONDAY), calendar.get(Calendar.DAY_OF_MONTH),
    			calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
    	/*
    	if(isTody) {
    		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONDAY), calendar.get(Calendar.DAY_OF_MONTH), alarmHour, alarmMin, 0);
    	} else {
    		calendar.add(Calendar.DAY_OF_YEAR, 1);
    		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONDAY), calendar.get(Calendar.DAY_OF_MONTH), alarmHour, alarmMin, 0);
    	}
    	*/
    	
    	am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
    	Log.v(TAG, "setAlarm is started :" + planId);
    }
    
    private int addAlarmService(int planId, int dayOfWeek, String alarmTime) {
    	int result = 0;
    	// add new alarm to db
    	insertItem(planId, dayOfWeek, alarmTime);
    	
    	alarmList.add(planId);
    	
    	//set to alarm manager   	
    	setAlarm(planId, dayOfWeek, alarmTime);
    	
    	return result;
    }
    
    private int updateAlarmService(int planId, int dayOfWeek, String alarmTime) {
    	int result = 0;
    	//update db
    	updateItem(planId, dayOfWeek, alarmTime);
    	//cancel registered alarm
    	cancelAlarm(planId);
    	
    	//set to alarm manager
    	setAlarm(planId, dayOfWeek, alarmTime);
    	
    	return result;
    }
    
    private int deleteAlarmService(int planId) {
    	int result = 0;
    	//delete alarm info from db
    	deleteItem(planId);
    	
    	alarmList.remove(planId);
    	
    	//cancel registered alarm
    	cancelAlarm(planId);
    	
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
		IntentFilter intentFilter = new IntentFilter();
		
		intentFilter.addAction(EXPIRED_ACTION);
		
		registerReceiver(alarmServiceReceiver, intentFilter);
		
		db = new DBManager(this, DBManager.DB_NAME, null, 1).getWritableDatabase();
		
		setAllAlarm();
		
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
		unregisterReceiver(alarmServiceReceiver);
		super.onDestroy();
	}
	
	private void setAllAlarm() {
		String[] columns = {"planId", "dayOfWeek", "alarmTime"};
		Cursor c = db.query(DBManager.DB_NAME, columns, null, null, null, null, null);
		if(c.getCount() > 0) {
			c.moveToFirst();
			
			while(!c.isAfterLast()) {
				String planId = c.getString(0);
				int dayOfWeek = c.getInt(1);
				String alarmTime = c.getString(2);
				
				setAlarm(Integer.parseInt(planId), dayOfWeek, alarmTime);
				
				alarmList.add(Integer.parseInt(planId));
				
				Log.v(TAG, "set new alarm" + planId);
			}
		}
		
		c.close();
	}
	
	private void insertItem(int planId, int dayOfWeek, String alarmTime) {
		ContentValues cv = new ContentValues();
		cv.put("planId", Integer.toString(planId));
		cv.put("dayOfWeek", dayOfWeek);
		cv.put("alarmTime", alarmTime);
		db.insert(DBManager.DB_NAME, "null", cv);
	}
	
	private void updateItem(int planId, int dayOfWeek, String alarmTime) {
		ContentValues cv = new ContentValues();
		String[] args ={Integer.toString(planId)};
		cv.put("planId", Integer.toString(planId));
		cv.put("dayOfWeek", dayOfWeek);
		cv.put("alarmTime", alarmTime);
		db.update(DBManager.DB_NAME, cv, "planId=?", args);
	}
	
	private void deleteItem(int planId) {
		String[] args ={Integer.toString(planId)};
		db.delete(DBManager.DB_NAME, "planId=?", args);
	}
	
	private static BroadcastReceiver alarmServiceReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			
			if(intent.getAction().equals(EXPIRED_ACTION)) {
				int planId = intent.getExtras().getInt("planId");
				((AlarmService) context).setNextAlarm(planId);
			}			
		}
	};
	
	
}
