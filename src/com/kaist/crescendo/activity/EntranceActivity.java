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
		SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
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
	           			//@TODO
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
			if(result.length() > 1) /* TODO :: should re-check result status */
			{ // login success.
				saveSessionStatus(true);
			}
		}
	}
}
