package com.kaist.crescendo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kaist.crescendo.R;
import com.kaist.crescendo.utils.MyPref;
import com.kaist.crescendo.utils.MyStaticValue;

public class EntranceActivity extends UpdateActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_entrance);
		setTitle(R.string.str_title_welcome);
		
		findViewById(R.id.button_login).setOnClickListener(mClickListener);
		findViewById(R.id.button_ToRegister).setOnClickListener(mClickListener);
	}
	
	/*  save latest session information */
	private void saveSessionStatus(boolean status) {
		SharedPreferences prefs = getSharedPreferences(MyPref.myPref, MODE_MULTI_PROCESS);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(MyPref.KEY_SESSION, status);
		editor.commit();
	}
	
	/* handling buttons */
	Button.OnClickListener mClickListener = new View.OnClickListener()
	{
	      public void onClick(View v)
	      {
	           switch (v.getId())
	           {
	           		case R.id.button_login:
	           			verifyAndLogin(); /* Login */
	           			break;
	           		case R.id.button_ToRegister:
	           			/* Call Register Editor Activity */
	           			Intent intent = new Intent();
	           			startActivityForResult(intent.setClass(getApplicationContext(),
	           					RegisterEditorActivity.class), MyStaticValue.REQUESTCODE_REGISTER);
	           			
	           			break;
	           		default:
	           			break;
	           }
	      }
	};
	
	private void verifyAndLogin()
	{
		boolean isOK = true;
		EditText id = (EditText)this.findViewById(R.id.editID);
		EditText pw = (EditText)this.findViewById(R.id.editPW);
		if(id.getText().length() < 5 || id.getText().toString().contains("@") == false)
		{
			isOK = false;
			Toast.makeText(this, "Please Input valid ID format (Email address)", Toast.LENGTH_LONG).show();
		}
					
		if(isOK == true && pw.getText().length() < 5) {
			isOK = false;
			Toast.makeText(this, "Please Input Password more than 5 characters", Toast.LENGTH_LONG).show();
		}
			
		if(isOK == true)
		{
			String result = login(id.getText().toString(), pw.getText().toString());
			//if(result.length() > 1) /* TODO :: should re-check result status */
			
			if(result == "good")
			{ // login success.
				saveSessionStatus(true);
				
				/* saved last login id */
				SharedPreferences prefs = getSharedPreferences(MyPref.myPref, MODE_MULTI_PROCESS);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString(MyPref.KEY_MYID, id.getText().toString());
				editor.commit();
				
				Intent intent = new Intent();
				startActivity(intent.setClass(getApplicationContext(), MainActivity.class));
				finish();
			}
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch(requestCode){
		case MyStaticValue.REQUESTCODE_REGISTER: 
			if(resultCode == RESULT_OK){ 
				boolean result = data.getExtras().getBoolean(MyStaticValue.RESULT_REGISTER);
				
				if(result == true) 
				{
					((EditText) findViewById(R.id.editID)).setText(data.getStringExtra(MyStaticValue.RESULT_ID));
					((EditText) findViewById(R.id.editPW)).setText(data.getStringExtra(MyStaticValue.RESULT_PW));
					
					/* save phone information to preference */
					SharedPreferences prefs = getSharedPreferences(MyPref.myPref, MODE_MULTI_PROCESS);
					SharedPreferences.Editor editor = prefs.edit();
					editor.putString(MyPref.KEY_PHONE, data.getStringExtra(MyStaticValue.RESULT_PHONE));
					editor.commit();
				}
				else {
					((EditText) findViewById(R.id.editID)).setText("");
					((EditText) findViewById(R.id.editPW)).setText("");
				}
			}
	}
		
	}
}
