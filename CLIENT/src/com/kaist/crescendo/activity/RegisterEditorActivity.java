package com.kaist.crescendo.activity;

import com.kaist.crescendo.R;
import com.kaist.crescendo.utils.MyStaticValue;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterEditorActivity extends UpdateActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_registereditor);
		
		findViewById(R.id.button_ToRegister).setOnClickListener(mClickListener);
	}
	
	/* handling buttons */
	Button.OnClickListener mClickListener = new View.OnClickListener()
	{
	      public void onClick(View v)
	      {
	           switch (v.getId())
	           {
	           		case R.id.button_ToRegister:
	           			verifyAndRegister(); /* register */
	           			break;

	           		default:
	           			break;
	           }
	      }
	};

	protected void verifyAndRegister() {
		// TODO Auto-generated method stub
		boolean isOK = true;
		EditText id = (EditText)this.findViewById(R.id.editID);
		EditText pw = (EditText)this.findViewById(R.id.editPW);
		EditText ph = (EditText)this.findViewById(R.id.editPhoneNumber);
		EditText bi = (EditText)this.findViewById(R.id.editBirthday);
		
		if(id.getText().length() < 5 || id.getText().toString().contains("@") == false)
		{
			isOK = false;
			Toast.makeText(this, "Please Input valid ID format (Email address)", Toast.LENGTH_LONG).show();
		}
					
		if(isOK == true && pw.getText().length() < 5) {
			isOK = false;
			Toast.makeText(this, "Please Input Password more than 5 characters", Toast.LENGTH_LONG).show();
		}
		
		if(isOK == true && ph.getText().length() < 9) {
			isOK = false;
			Toast.makeText(this, "Please Input valid Phone number", Toast.LENGTH_LONG).show();
		}
			
		if(isOK == true)
		{
			String result = register(id.getText().toString(), pw.getText().toString(), ph.getText().toString(), bi.getText().toString());
			if(result.equals("good")) /* TODO :: should re-check result status */
			{ 
				//saveSessionStatus(true);
				finish();
			}
		}
	}
}
