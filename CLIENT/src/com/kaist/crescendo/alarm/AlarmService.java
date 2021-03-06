package com.kaist.crescendo.alarm;

import java.util.ArrayList;
import java.util.Calendar;
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
	
	private ArrayList<Integer> alarmList;
	
	public static final String INTENT_ACTION = "android.intent.action.com.kaist.crescendo.ON_BIND";
	public static final String EXPIRED_ACTION = "android.intent.action.com.kaist.crescendo.ALARM_EXPIRED";
	
	private SQLiteDatabase db;
	
	// IAlarmService interface �̴�. 
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
		public int resetAlarm() throws RemoteException {
			//delete all items from db
			Log.v(TAG, "[resetAlarm]");
			deleteAllAlarmService();
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
    
    private boolean checkAlarmIsValid(int planId) {
		boolean result = false;		
		String[] columns = {"planId", "dayOfWeek", "alarmTime"};
		int db_count;
		Cursor c = db.query(DBManager.DB_NAME, columns, null, null, null, null, null);
		Calendar calendar = Calendar.getInstance();
		
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		Log.v(TAG, "today : " +  day);
		boolean isOK = false;
		
		if((db_count = c.getCount()) > 0) {
			Log.v(TAG, "checkAlarmIsValid db count : " + db_count);			
			c.moveToFirst();
			
			while(!c.isAfterLast()) {
				String planId2 = c.getString(0);
				int dayOfWeek = c.getInt(1);
				isOK = false;
				
				if(Integer.parseInt(planId2) == planId) {
					for(int i = 0; i < 7 ; i++) {
						if((dayOfWeek & (0x01 << (i*4))) == (0x01 << ((day - 1) * 4))) {
							isOK = true;
							break;
						}
					}
					
					if(isOK == true) {
						result = true;
						break;
					}
				}
				
				c.moveToNext();
			}
		}
		
		c.close();
		
		Log.v(TAG, "planId: " + planId + " is alarm valid ?" + (isOK?"yes":"no"));
		
		return result;
	}
    
    private void getAlarmTime(int planId, int[] val) {
		String[] columns = {"planId", "dayOfWeek", "alarmTime"};
		int db_count;
		Cursor c = db.query(DBManager.DB_NAME, columns, null, null, null, null, null);
		
		if((db_count = c.getCount()) > 0) {
			Log.v(TAG, "getAlarmTime db count : " + db_count);			
			c.moveToFirst();
			
			while(!c.isAfterLast()) {
				String planId2 = c.getString(0);
				String alarmTime = c.getString(2);
				
				if(Integer.parseInt(planId2) == planId) {
					StringTokenizer st = new StringTokenizer(alarmTime,":");
					val[0] = Integer.parseInt(st.nextToken());
					val[1] = Integer.parseInt(st.nextToken());
					Log.v(TAG, "hour: minute" + val[0]+":"+ val[1]);
					break;
				}
				
				c.moveToNext();
			}
		}
		
		c.close();
    }
    
    private void setNextAlarm(int planId) {
    	AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
    	
    	Intent intent = new Intent(EXPIRED_ACTION);
    	intent.putExtra("planId", planId);
    	PendingIntent sender = PendingIntent.getBroadcast(this, planId, intent, 0);
    	    	
    	Calendar calendar = Calendar.getInstance();
    	
    	int[] val = {0, 0};
    	getAlarmTime(planId, val);
    	
    	calendar.add(Calendar.DAY_OF_YEAR, 1);
    	calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
    			val[0], val[1], 0);
    	
    	//test code set alarm after 5 minutes
    	//calendar.add(Calendar.MINUTE, 5);
    	//calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
    	//		calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
 
    	am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
    	Log.v(TAG, "setNextAlarm is started :" + planId + " Time :" + calendar.get(Calendar.YEAR)+"-"+ (calendar.get(Calendar.MONTH) + 1)+"-"+ calendar.get(Calendar.DAY_OF_MONTH)+"/"+ val[0]+":"+ val[1]);
    	
    	if(checkAlarmIsValid(planId)) {
	    	//start InputActivity
	    	Intent i = new Intent(this, InputActivity.class);
	    	i.putExtra("planId", planId);
	    	//i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
	    	i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    	startActivity(i);
    	}
    }
    
    private boolean checkAlarmIsToday(int alarmHour, int alarmMin, int curHour, int curMin) {
    	boolean isToday = false;
    	if(alarmHour > curHour) {
    		isToday = true;
    	} else if(alarmHour == curHour && alarmMin > curMin) {
    		isToday = true;
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
    	
    	boolean isToday = checkAlarmIsToday(alarmHour, alarmMin, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
    	
    	Log.v(TAG, "isToday : " + (isToday?"true":"false"));
    	//test code set alarm after 1 minute
    	//calendar.add(Calendar.MINUTE, 1);
    	//calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
    	//		calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));

    	if(isToday) {
    		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), alarmHour, alarmMin, 0);
    	} else {
    		calendar.add(Calendar.DAY_OF_YEAR, 1);
    		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), alarmHour, alarmMin, 0);
    	}
    	
    	am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
    	Log.v(TAG, "setAlarm is started :" + planId + " Time :" + calendar.get(Calendar.YEAR)+"-"+ (calendar.get(Calendar.MONTH) + 1)+"-"+ calendar.get(Calendar.DAY_OF_MONTH)+"/"+ alarmHour+":"+alarmMin);
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
   	
    	for(int i  = 0; i < alarmList.size(); i++) {
    		if(alarmList.get(i) == planId) {
    			alarmList.remove(i);
    			break;
    		}
    	}
    	
    	//cancel registered alarm
    	cancelAlarm(planId);
    	
    	return result;
    }
    
    private int deleteAllAlarmService() {
    	String[] columns = {"planId", "dayOfWeek", "alarmTime"};
		int db_count;
		Cursor c = db.query(DBManager.DB_NAME, columns, null, null, null, null, null);
		if((db_count = c.getCount()) > 0) {
			Log.v(TAG, "deleteAllAlarmService db count : " + db_count);			
			c.moveToFirst();
			
			while(!c.isAfterLast()) {
				String planId = c.getString(0);
				
				cancelAlarm(Integer.parseInt(planId));
				c.moveToNext();
			}
		}
		
		alarmList.clear();
    		
    	db.delete(DBManager.DB_NAME, null, null);
    	
    	return 0;
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
		db.close();
		super.onDestroy();
	}
	
	private void setAllAlarm() {
		String[] columns = {"planId", "dayOfWeek", "alarmTime"};
		int db_count;
		Cursor c = db.query(DBManager.DB_NAME, columns, null, null, null, null, null);
		if((db_count = c.getCount()) > 0) {
			Log.v(TAG, "db count : " + db_count);			
			c.moveToFirst();
			
			while(!c.isAfterLast()) {
				String planId = c.getString(0);
				int dayOfWeek = c.getInt(1);
				String alarmTime = c.getString(2);
				
				setAlarm(Integer.parseInt(planId), dayOfWeek, alarmTime);
				
				alarmList.add(Integer.parseInt(planId));
				
				Log.v(TAG, "set new alarm - planId : " + planId);
				
				c.moveToNext();
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
			if(intent.getAction().equals(EXPIRED_ACTION)) {
				int planId = intent.getExtras().getInt("planId");
				((AlarmService) context).setNextAlarm(planId);
			}			
		}
	};
	
	
}
