package com.kaist.crescendo.activity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.DataFormatException;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.kaist.crescendo.R;
import com.kaist.crescendo.data.PlanData;
import com.kaist.crescendo.data.PlanListAdapter;
import com.kaist.crescendo.utils.MyStaticValue;

public class PlanEditorActivity extends UpdateActivity {
	
	private int mode;
	private int index;
	Calendar startCalendar = Calendar.getInstance();
	Calendar endCalendar = Calendar.getInstance();
	PlanData plan;
	
	EditText startDay;
	EditText endDay;
	
	EditText initValue;
	EditText targetValue;
	
	Calendar alarmTime = Calendar.getInstance();
	
	EditText alarmTimeValue;
	
	DatePickerDialog.OnDateSetListener startDateSetListener = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			
			SimpleDateFormat Formatter = new SimpleDateFormat("yyyy-MM-dd");		
			startCalendar.set(year, monthOfYear, dayOfMonth);
			startDay.setText(Formatter.format(startCalendar.getTime()));
		}
	};
	
	DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			SimpleDateFormat Formatter = new SimpleDateFormat("yyyy-MM-dd");
			endCalendar.set(year, monthOfYear, dayOfMonth);
			endDay.setText(Formatter.format(endCalendar.getTime()));
		}
	};

	OnClickListener mSelectDayOfWeek = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			/* no need */
		}
	};
	
	OnClickListener mClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			verifyAndSave();
			
		}
	};
	
	OnClickListener mStartDayListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			new DatePickerDialog(PlanEditorActivity.this, startDateSetListener, startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH)).show();
		}
	};
	
	OnClickListener mEndDayListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			new DatePickerDialog(PlanEditorActivity.this, endDateSetListener, endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH), endCalendar.get(Calendar.DAY_OF_MONTH)).show();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_planeditor);
		
		
		startDay = (EditText) findViewById(R.id.editStartDate);
		endDay = (EditText) findViewById(R.id.editEndDate);
		initValue = (EditText) findViewById(R.id.editInitValue);
		targetValue = (EditText) findViewById(R.id.editTargetValue);
		
		alarmTimeValue = (EditText) findViewById(R.id.editAlarmTime);
		
		mode = getIntent().getExtras().getInt(MyStaticValue.MODE);
		
		if(mode != MyStaticValue.MODE_NEW) /* user want to update existing plan */
		{
			setTitle(R.string.str_modify_plan);
			// TODO Fill data for each field.
			
			index = getIntent().getExtras().getInt(MyStaticValue.NUMBER);
			PlanListAdapter adapter = PlanListActivity.getAdapterInstance();
			plan = (PlanData) adapter.getItem(index);
			startDay.setText(plan.start);
			endDay.setText(plan.end);
			initValue.setText(Double.toString(plan.initValue) + "kg");
			targetValue.setText(Double.toString(plan.targetValue));
			((EditText) findViewById(R.id.editTile)).setText(plan.title);
			setAlarmDayOfWeek(plan.dayOfWeek);
			
		}
		else {  /* Add new plan */
			setTitle(R.string.str_addnewplan);
			/* calendar goes to be now */
			SimpleDateFormat Formatter = new SimpleDateFormat("yyyy-MM-dd");
			startCalendar.set(startCalendar.get(Calendar.YEAR),startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH));
			startDay.setText(Formatter.format(startCalendar.getTime()));
		
			/* to next year */
			endCalendar.set(endCalendar.get(Calendar.YEAR)+1,endCalendar.get(Calendar.MONTH), endCalendar.get(Calendar.DAY_OF_MONTH));
			endDay.setText(Formatter.format(endCalendar.getTime()));
			
			initValue.setText("80" + "kg");
			targetValue.setText("70" + "kg");
			/* alarm time */
			alarmTimeValue.setText(alarmTime.get(Calendar.HOUR_OF_DAY) + ":" + alarmTime.get(Calendar.MINUTE));
		}
				
		findViewById(R.id.editStartDate).setOnClickListener(mStartDayListener);
		findViewById(R.id.editEndDate).setOnClickListener(mEndDayListener);
		
		findViewById(R.id.planeditor_save).setOnClickListener(mClickListener);
		
		findViewById(R.id.button_mon).setOnClickListener(mSelectDayOfWeek);
		findViewById(R.id.button_tues).setOnClickListener(mSelectDayOfWeek);
		findViewById(R.id.button_wed).setOnClickListener(mSelectDayOfWeek);
		findViewById(R.id.button_thur).setOnClickListener(mSelectDayOfWeek);
		findViewById(R.id.button_fri).setOnClickListener(mSelectDayOfWeek);
		findViewById(R.id.button_sat).setOnClickListener(mSelectDayOfWeek);
		findViewById(R.id.button_sun).setOnClickListener(mSelectDayOfWeek);

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
	
	private int getAlarmDayOfWeek()	{
		
		int dayOfWeek = 0;
		
		if(((ToggleButton)findViewById(R.id.button_mon)).isChecked())
			dayOfWeek += MyStaticValue.MONDAY;
		if(((ToggleButton)findViewById(R.id.button_tues)).isChecked())
			dayOfWeek += MyStaticValue.TUESDAY;
		if(((ToggleButton)findViewById(R.id.button_wed)).isChecked())
			dayOfWeek += MyStaticValue.WEDNESDAY;
		if(((ToggleButton)findViewById(R.id.button_thur)).isChecked())
			dayOfWeek += MyStaticValue.THURSDAY;
		if(((ToggleButton)findViewById(R.id.button_fri)).isChecked())
			dayOfWeek += MyStaticValue.FRIDAY;
		if(((ToggleButton)findViewById(R.id.button_sat)).isChecked())
			dayOfWeek += MyStaticValue.SATURDAY;
		if(((ToggleButton)findViewById(R.id.button_sun)).isChecked())
			dayOfWeek += MyStaticValue.SUNDAY;
		
		return dayOfWeek;
	}
	
	private void setAlarmDayOfWeek(int dayOfWeek)	{

		if((dayOfWeek & MyStaticValue.MONDAY) > 0)
			((ToggleButton)findViewById(R.id.button_mon)).setChecked(true);
		if((dayOfWeek & MyStaticValue.TUESDAY) > 0)
			((ToggleButton)findViewById(R.id.button_tues)).setChecked(true);
		if((dayOfWeek & MyStaticValue.WEDNESDAY) > 0)
			((ToggleButton)findViewById(R.id.button_wed)).setChecked(true);
		if((dayOfWeek & MyStaticValue.THURSDAY) > 0)
			((ToggleButton)findViewById(R.id.button_thur)).setChecked(true);
		if((dayOfWeek & MyStaticValue.FRIDAY) > 0)
			((ToggleButton)findViewById(R.id.button_fri)).setChecked(true);
		if((dayOfWeek & MyStaticValue.SATURDAY) > 0)
			((ToggleButton)findViewById(R.id.button_sat)).setChecked(true);
		if((dayOfWeek & MyStaticValue.SUNDAY) > 0)
			((ToggleButton)findViewById(R.id.button_sun)).setChecked(true);
	}
	
	protected void verifyAndSave() {
		// TODO Auto-generated method stub
		boolean isOK = true;
		EditText title = (EditText)this.findViewById(R.id.editTile);
		EditText start = (EditText)this.findViewById(R.id.editStartDate);
		EditText end = (EditText)this.findViewById(R.id.editEndDate);
		EditText initV = (EditText)this.findViewById(R.id.editInitValue);
		EditText targetV = (EditText)this.findViewById(R.id.editTargetValue);
		
		if(title.getText().length() < 3)
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
		
		int dayOfWeek = getAlarmDayOfWeek();
			
		if(isOK == true)
		{
			if(mode == MyStaticValue.MODE_NEW)
			{
				PlanData plan = new PlanData(MyStaticValue.PLANTYPE_DIET, title.getText().toString(), start.getText().toString(), end.getText().toString(), dayOfWeek,
										Float.parseFloat(initV.getText().toString().replace("kg", "")), Float.parseFloat(targetV.getText().toString().replace("kg", "")));
				String ret = addNewPlan(plan);
				if(ret.equals("good")) {
					complete();
				}
			}
			else {
				plan.title = title.getText().toString();
				plan.start = start.getText().toString();
				plan.end = end.getText().toString();
				plan.dayOfWeek = dayOfWeek;
				plan.initValue = Float.parseFloat(initV.getText().toString().replace("kg", ""));
				plan.targetValue = Float.parseFloat(targetV.getText().toString().replace("kg", ""));
				
				boolean ret = updatePlan(plan);
				if(ret == true) {
					complete();
				}
			}
		}
	}
	
}
