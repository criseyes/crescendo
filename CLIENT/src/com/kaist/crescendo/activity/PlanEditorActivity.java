package com.kaist.crescendo.activity;

import android.R.drawable;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.kaist.crescendo.R;
import com.kaist.crescendo.data.PlanData;
import com.kaist.crescendo.utils.MyStaticValue;

public class PlanEditorActivity extends UpdateActivity {
	
	private int mode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_planeditor);
		
		mode = getIntent().getExtras().getInt(MyStaticValue.MODE);
		if(mode != MyStaticValue.MODE_NEW) /* user want to update existing plan */
		{
			// Fill data for each field.
		}
		
		OnClickListener mSelectDayOfWeek = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/* no need */
			}
		};
		
		OnClickListener mClickListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 *  TODO Call add New Plan
				 */
				//PlanData plan = new PlanData(type, title, start, end);
				verifyAndSave();
				
			}
		};
		
		findViewById(R.id.planeditor_save).setOnClickListener(mClickListener);
		
		findViewById(R.id.button_thus).setOnClickListener(mSelectDayOfWeek);
		findViewById(R.id.button_wed).setOnClickListener(mSelectDayOfWeek);
		findViewById(R.id.button_thur).setOnClickListener(mSelectDayOfWeek);
		findViewById(R.id.button_fri).setOnClickListener(mSelectDayOfWeek);
		findViewById(R.id.button_sat).setOnClickListener(mSelectDayOfWeek);
		findViewById(R.id.button_sun).setOnClickListener(mSelectDayOfWeek);
		findViewById(R.id.button_mon).setOnClickListener(mSelectDayOfWeek);
	}
	
	private void complete()
	{
		Intent intent = new Intent();
		/*
		 *  TODO if failed, you should insert "false" not "true"
		 */
		intent.putExtra("success", true);
		this.setResult(RESULT_OK, intent); 
		this.finish();
		
	}
	
	protected void verifyAndSave() {
		// TODO Auto-generated method stub
		boolean isOK = true;
		EditText title = (EditText)this.findViewById(R.id.editTile);
		EditText start = (EditText)this.findViewById(R.id.editStartDate);
		EditText end = (EditText)this.findViewById(R.id.editEndDate);
		
		if(title.getText().length() < 3 == false)
		{
			isOK = false;
			Toast.makeText(this, "Please Input valid Tile..", Toast.LENGTH_LONG).show();
		}
					
		if(isOK == true && start.getText().length() < 5) {
			isOK = false;
			Toast.makeText(this, "Please Input valid start date", Toast.LENGTH_LONG).show();
		}
		
		if(isOK == true && end.getText().length() < 5) {
			isOK = false;
			Toast.makeText(this, "Please Input valid start date", Toast.LENGTH_LONG).show();
		}
			
		if(isOK == true)
		{
			if(mode == MyStaticValue.MODE_NEW)
			{
				/* 
				 * TODO addNewPlan
				 */
			}
			else {
				/* 
				 *  TODO updatePlan
				 */
			}
		}
	}
}
