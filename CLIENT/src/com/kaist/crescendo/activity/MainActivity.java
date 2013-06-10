package com.kaist.crescendo.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.kaist.crescendo.R;
import com.kaist.crescendo.activity.UpdateActivity;
import com.kaist.crescendo.alarm.AlarmService;
import com.kaist.crescendo.alarm.IAlarmService;
import com.kaist.crescendo.alarm.IAlarmServiceCallback;
import com.kaist.crescendo.utils.MyStaticValue;

public class MainActivity extends UpdateActivity {
	
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
			if(service != null) {
				mService = IAlarmService.Stub.asInterface(service);				
				try {
					mService.registerCallback(mCallback);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}			
		}
	};
	
	private void bindService() {
		bindService(new Intent(AlarmService.INTENT_ACTION), mConnection, Context.BIND_AUTO_CREATE);
		//register mService for other activity
	}
	
	private void unBindService() {
		unbindService(mConnection);
		//unregister mService
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
		
		bindService();
	}
	
	@Override
	protected void onDestroy() {
		unBindService();
	};
	
	
	/* handling buttons */
	Button.OnClickListener mClickListener = new View.OnClickListener()
	{
	      public void onClick(View v)
	      {
	    	  Intent intent = new Intent();
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
	           			break;
	           		case R.id.main_manage_widget:
	           			intent.setClass(getApplicationContext(), AvataEditorActivity.class);
	           			break;
	           		case R.id.main_view_status:
	           			intent.setClass(getApplicationContext(), StatusActivity.class);
	           			break;
	           		default:
	           			break;
	           }
	           startActivity(intent);
	      }
	};
	
	

}
