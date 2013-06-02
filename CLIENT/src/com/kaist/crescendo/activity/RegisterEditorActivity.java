package com.kaist.crescendo.activity;

import java.text.DateFormat;
import java.util.Calendar;

import com.kaist.crescendo.R;
import com.kaist.crescendo.utils.MyStaticValue;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterEditorActivity extends UpdateActivity {
	
	Calendar calendar = Calendar.getInstance();
	EditText birthDay;
	
	DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			calendar.set(year, monthOfYear, dayOfMonth);
			birthDay.setText(DateFormat.getDateInstance().format(calendar.getTime()));
		}
	};
	
	OnClickListener mBirthDayListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			new DatePickerDialog(RegisterEditorActivity.this, endDateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_registereditor);
		
		findViewById(R.id.button_ToRegister).setOnClickListener(mClickListener);
		
		calendar.set(1994, 12, 25); 
		
		birthDay = (EditText) findViewById(R.id.editBirthday);
		birthDay.setOnClickListener(mBirthDayListener);
		birthDay.setText(DateFormat.getDateInstance().format(calendar.getTime()));
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
