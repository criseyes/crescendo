package com.kaist.crescendo.activity;

import com.kaist.crescendo.R;
import com.kaist.crescendo.utils.MyPref;
import com.kaist.crescendo.utils.MyStaticValue;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.sax.TextElementListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordActivity extends UpdateActivity {	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_password);
		setTitle(R.string.str_changing_password);
		
		findViewById(R.id.button_ToChange).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 switch (v.getId())
		           {
		           		case R.id.button_ToChange:
		           			verifyAndChange(); /* register */
		           			break;

		           		default:
		           			break;
		           }
				
			}
			});		
	}
		
	private void verifyAndChange() {
		// TODO Auto-generated method stub
		boolean isOK = true;
		
		EditText curPW = (EditText) findViewById(R.id.editCurPW);
		EditText newPW = (EditText) findViewById(R.id.editNewPW);
		EditText confirmPW = (EditText) findViewById(R.id.editConfirmPW);
		
		if(curPW.length() < 7) {
			Toast.makeText(this, getResources().getText(R.string.str_curPW) + " " + getResources().getText(R.string.str_tooshort_password), Toast.LENGTH_LONG).show();
			isOK = false;
		}
		
		if(newPW.length() < 7) {
			Toast.makeText(this, getResources().getText(R.string.str_newPW) + " " + getResources().getText(R.string.str_tooshort_password), Toast.LENGTH_LONG).show();
			isOK = false;
		}
		
		if(confirmPW.length() < 7) {
			Toast.makeText(this, getResources().getText(R.string.str_confirmPW) + " " + getResources().getText(R.string.str_tooshort_password), Toast.LENGTH_LONG).show();
			isOK = false;
		}
		
		if(newPW.getText().toString().equals(curPW.getText().toString()) != true) {
			Toast.makeText(this, getResources().getText(R.string.str_newPW) +"와 " + getResources().getText(R.string.str_confirmPW) + "값이 다릅니다.", Toast.LENGTH_LONG).show();
			isOK = false;
		}
		
		if(isOK) {
			boolean ret = false;
			
			ret = changePassword(curPW.getText().toString(), newPW.getText().toString());
			
			if(ret) {
				saveSessionStatus(false);
				Toast.makeText(this,getResources().getText(R.string.str_change_password_relogin) , Toast.LENGTH_LONG).show();
			}			
		}
	}
	
	private void saveSessionStatus(boolean status) {
		SharedPreferences prefs = getSharedPreferences(MyPref.myPref, MODE_MULTI_PROCESS);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(MyPref.KEY_SESSION, status);
		editor.commit();
	}
}
