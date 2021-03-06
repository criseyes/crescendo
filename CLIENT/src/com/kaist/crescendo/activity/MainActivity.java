package com.kaist.crescendo.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.kaist.crescendo.R;
import com.kaist.crescendo.alarm.AlarmService;
import com.kaist.crescendo.alarm.BootReceiver;
import com.kaist.crescendo.alarm.IAlarmService;
import com.kaist.crescendo.alarm.IAlarmServiceCallback;
import com.kaist.crescendo.data.PlanData;
import com.kaist.crescendo.utils.MyPref;
import com.kaist.crescendo.utils.MyStaticValue;

public class MainActivity extends UpdateActivity {
	
	private static boolean mFlag = false;
	private final String TAG = "MainActivity";
	private boolean login = false;
	
	IAlarmServiceCallback mCallback = new IAlarmServiceCallback.Stub() {
		
		@Override
		public void alarmExpired(int planId) throws RemoteException {
			// TODO Auto-generated method stub
			
		}
	};
	
	IAlarmService mService;
	ServiceConnection mConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			if(mService != null) {
				try {
					mService.unreigsterCallback(mCallback);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}			
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			Log.v(TAG, "onServiceConnected : " + service);
			if(service != null) {
				mService = IAlarmService.Stub.asInterface(service);
				Log.v(TAG, "mService : " + mService);
				setServiceInterface(mService);
				syncAlarmService();
				try {
					mService.registerCallback(mCallback);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}			
		}
	};
	//private ArrayList<PlanData> planArrayList;
	
	private void startService() {
		startService(new Intent(BootReceiver.START_ACTION));
		Log.v(TAG, "startService");
	}
	
	private void bindService() {
		bindService(new Intent(AlarmService.INTENT_ACTION), mConnection, Context.BIND_AUTO_CREATE);
		Log.v(TAG, "bindService");
		//register mService for other activity
	}
	
	private void unBindService() {
		unbindService(mConnection);
		Log.v(TAG, "unBindService");
		//unregister mService
	}
	
	private boolean getServiceTaskName() {
		boolean checked = false;
		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> info;
		info = am.getRunningServices(Integer.MAX_VALUE);
		
		for(Iterator<RunningServiceInfo> iterator = info.iterator(); iterator.hasNext(); ) {
			RunningServiceInfo runningTaskInfo = (RunningServiceInfo) iterator.next();
			Log.i(TAG, "Service name : " + runningTaskInfo.service.getClassName());
			if(runningTaskInfo.service.getClassName().equals("com.kaist.crescendo.alarm.AlarmService")) {
				checked = true;
				Log.i(TAG, "Service is..." + checked);
				break;
			}
		}
		
		return checked;
	}
	
	private void syncAlarmService() {
		//when install application again we have to sync server information with alarmDB
		if(login) {
			ArrayList<PlanData> planList = new ArrayList<PlanData>();
			if(getPlanList(planList).equals("good")) {
				try {
					//delete all items and cancel registered alarm from AlarmManager
					mService.resetAlarm();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				for(int i = 0; i < planList.size(); i++) {
					PlanData plan = planList.get(i);
					try {
						mService.addAlarm(plan.uId, plan.dayOfWeek, plan.alarm);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		findViewById(R.id.main_plans_list).setOnClickListener(mClickListener);
		findViewById(R.id.main_friends_list).setOnClickListener(mClickListener);
		findViewById(R.id.main_manage_settings).setOnClickListener(mClickListener);
		findViewById(R.id.main_view_status).setOnClickListener(mClickListener);
		findViewById(R.id.main_manage_widget).setOnClickListener(mClickListener);
		
		MyStaticValue.myId = getMyID();
		
		login = getIntent().getExtras().getBoolean("login");
		
		if(getServiceTaskName() == false) {
			startService();
		}
		
		bindService();
	}
	
	static private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0) {
				mFlag = false;
			}
		}
	};
	
	@Override
	protected void onDestroy() {
		unBindService();
		super.onDestroy();
	};
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			if(!mFlag) {
				Toast.makeText(this, R.string.str_finish_app, Toast.LENGTH_LONG).show();
				mFlag = true;
				mHandler.sendEmptyMessageDelayed(0, 2000);
				return false;
			} else {
				finish();
			}
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	
	/* handling buttons */
	Button.OnClickListener mClickListener = new View.OnClickListener()
	{
	      public void onClick(View v)
	      {
	    	  Intent intent = new Intent();
	    	  SharedPreferences prefs;
	    	  int planUid;
	           switch (v.getId())
	           {
	           		case R.id.main_plans_list:
	           			intent.setClass(getApplicationContext(), PlanListActivity.class);
	           			break;
	           		case R.id.main_friends_list:
	           			intent.setClass(getApplicationContext(), FriendsListActivity.class);
	           			break;
	           		case R.id.main_manage_settings:
	           			intent.setClass(getApplicationContext(), SettingsActivity.class);
	           			startActivityForResult(intent, MyStaticValue.REQUESTCODE_LOGOUT);
	           			return;
	           			
	           		case R.id.main_manage_widget:	
	           			prefs = getSharedPreferences(MyPref.myPref, MODE_MULTI_PROCESS);
	           			planUid = prefs.getInt(MyPref.MY_AVATA_UID, 0);
	           			if(planUid == 0)
	           			{
	           				Toast.makeText(getApplicationContext(), R.string.str_err_setdefaultplan, Toast.LENGTH_LONG).show();
	           				return;
	           			}
	           			intent.setClass(getApplicationContext(), AvataEditorActivity.class);
	           			break;
	           		case R.id.main_view_status:
	           			prefs = getSharedPreferences(MyPref.myPref, MODE_MULTI_PROCESS);
	           			planUid = prefs.getInt(MyPref.MY_AVATA_UID, 0);
	           			if(planUid == 0)
	           			{
	           				Toast.makeText(getApplicationContext(), R.string.str_err_setdefaultplan, Toast.LENGTH_LONG).show();
	           				return;
	           			}
	           			intent.setClass(getApplicationContext(), StatusActivity.class);
	           			break;
	           		default:
	           			break;
	           }
	           startActivity(intent);
	      }
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch(requestCode){
		case MyStaticValue.REQUESTCODE_LOGOUT: 
			if(resultCode == RESULT_OK){ 
				//moveTaskToBack(true);
				finish();
			}
		}
		
	}
	
	

}
