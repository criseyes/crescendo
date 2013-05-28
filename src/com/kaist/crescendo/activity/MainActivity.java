package com.kaist.crescendo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.kaist.crescendo.R;

public class MainActivity extends UpdateActivity {

	public static final String KEY_SESSION = "SESSION_CREATED";
	public static final int REQUESTCODE_FOR_REGISTER = 100;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		/* Call Register Activity when session is not established */
		SharedPreferences prefs = getSharedPreferences("MyPref", MODE_PRIVATE);
		boolean saved = prefs.getBoolean(KEY_SESSION, false);
		if(saved == false)
		{
			//Intent intent = new Intent(this, IntroActivity.class );
			Intent intent = new Intent(this, EntranceActivity.class );
			startActivityForResult(intent, REQUESTCODE_FOR_REGISTER);
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		switch(requestCode) {
		case REQUESTCODE_FOR_REGISTER:
			if(resultCode == 1)
			{
				SaveSessionStatus(true);
			}
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void SaveSessionStatus(boolean status) {
		SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(KEY_SESSION, status);
		editor.commit();
	}
	/* handling buttons */
	Button.OnClickListener mClickListener = new View.OnClickListener()
	{
	      public void onClick(View v)
	      {
	           switch (v.getId())
	           {
	           		case R.id.main_plans_list:
	           			break;
	           		case R.id.main_friends_list:
	           			break;
	           		case R.id.main_manage_settings:
	           			break;
	           		case R.id.main_manage_widget:
	           			break;
	           		case R.id.main_view_status:
	           			break;
	           		default:
	           			break;
	           }
	      }
	};
	

}
