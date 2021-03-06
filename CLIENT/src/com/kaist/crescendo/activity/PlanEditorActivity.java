package com.kaist.crescendo.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.kaist.crescendo.R;
import com.kaist.crescendo.data.PlanData;
import com.kaist.crescendo.data.PlanListAdapter;
import com.kaist.crescendo.utils.MyStaticValue;

@SuppressLint("SimpleDateFormat")
public class PlanEditorActivity extends UpdateActivity {
	
	private int mode;
	private int index;
	Calendar startCalendar = Calendar.getInstance();
	Calendar endCalendar = Calendar.getInstance();
	Calendar alarmTime = Calendar.getInstance();
	
	PlanData plan;
	
	EditText startDay;
	EditText endDay;
	
	EditText initValue;
	EditText targetValue;
	
	EditText alarmTimeValue;
	EditText planType;
	
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
	
	TimePickerDialog.OnTimeSetListener alarmTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			alarmTime.set(alarmTime.get(Calendar.YEAR), alarmTime.get(Calendar.MONTH), alarmTime.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);
			alarmTimeValue.setText(alarmTime.get(Calendar.HOUR_OF_DAY) + ":" + alarmTime.get(Calendar.MINUTE));
			
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
	
	OnClickListener mAlarmTimeListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			new TimePickerDialog(PlanEditorActivity.this, alarmTimeSetListener, alarmTime.get(Calendar.HOUR_OF_DAY), alarmTime.get(Calendar.MINUTE), false).show();
			
		}
	};
	
	OnClickListener mPlanTypeListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			onPopupButtonClick(v);
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
		planType = (EditText) findViewById(R.id.editPlanType);
				
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
			initValue.setText(Double.toString(plan.initValue));
			targetValue.setText(Double.toString(plan.targetValue));
			((EditText) findViewById(R.id.editTile)).setText(plan.title);
			setAlarmDayOfWeek(plan.dayOfWeek);
			/* alarm time */
			alarmTimeValue.setText(plan.alarm);
			
			if(plan.type == MyStaticValue.PLANTYPE_DIET)
				planType.setText(R.string.str_plantype_diet);
			else if(plan.type == MyStaticValue.PLANTYPE_READING_BOOK)
				planType.setText(R.string.str_plantype_reading);
			else
				planType.setText(R.string.str_plantype_diet);
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
			
			initValue.setText("80");
			targetValue.setText("70");
			/* alarm time */
			alarmTimeValue.setText(alarmTime.get(Calendar.HOUR_OF_DAY) + ":" + alarmTime.get(Calendar.MINUTE));
			
			planType.setText(R.string.str_plantype_diet);
		}
				
		findViewById(R.id.editStartDate).setOnClickListener(mStartDayListener);
		findViewById(R.id.editEndDate).setOnClickListener(mEndDayListener);
		findViewById(R.id.editAlarmTime).setOnClickListener(mAlarmTimeListener);
		
		findViewById(R.id.planeditor_save).setOnClickListener(mClickListener);
		
		findViewById(R.id.button_mon).setOnClickListener(mSelectDayOfWeek);
		findViewById(R.id.button_tues).setOnClickListener(mSelectDayOfWeek);
		findViewById(R.id.button_wed).setOnClickListener(mSelectDayOfWeek);
		findViewById(R.id.button_thur).setOnClickListener(mSelectDayOfWeek);
		findViewById(R.id.button_fri).setOnClickListener(mSelectDayOfWeek);
		findViewById(R.id.button_sat).setOnClickListener(mSelectDayOfWeek);
		findViewById(R.id.button_sun).setOnClickListener(mSelectDayOfWeek);
		findViewById(R.id.editPlanType).setOnClickListener(mPlanTypeListener);

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
		EditText alarmV = (EditText)this.findViewById(R.id.editAlarmTime);
		
		int dayOfWeek = getAlarmDayOfWeek();
		
		if(title.getText().length() < 3)
		{
			isOK = false;
			Toast.makeText(this,R.string.str_err_invalidtitle, Toast.LENGTH_LONG).show();
		}
					
		if(isOK == true && start.getText().length() < 5) {
			isOK = false;
			Toast.makeText(this, R.string.str_err_invaliddate, Toast.LENGTH_LONG).show();
		}
		
		if(isOK == true && end.getText().length() < 5) {
			isOK = false;
			Toast.makeText(this, R.string.str_err_invaliddate, Toast.LENGTH_LONG).show();
		}
		
		if(dayOfWeek == 0) {
			isOK = false;
			Toast.makeText(this, R.string.str_err_invaliddayofweek, Toast.LENGTH_LONG).show();
		}
			
		int plantype_int ;
		
		if(this.planType.getText().toString().equals(getResources().getString(R.string.str_plantype_diet)))
			plantype_int = MyStaticValue.PLANTYPE_DIET;
		else if(this.planType.getText().toString().equals(getResources().getString(R.string.str_plantype_reading)))
			plantype_int = MyStaticValue.PLANTYPE_READING_BOOK;
		else plantype_int = MyStaticValue.PLANTYPE_DIET;
		if(isOK == true)
		{
			if(mode == MyStaticValue.MODE_NEW)
			{
				PlanData plan = new PlanData(plantype_int, title.getText().toString(), start.getText().toString(), end.getText().toString(), alarmV.getText().toString(), dayOfWeek,
										Float.parseFloat(initV.getText().toString().replace("kg", "")), Float.parseFloat(targetV.getText().toString().replace("kg", "")));
				String ret = addNewPlan(plan);
				if(ret.equals("good")) {
					//add new alarm to alarmService
					try {
						getServiceInterface().addAlarm(plan.uId, plan.dayOfWeek, plan.alarm);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					complete();
				}
			}
			else {
				plan.type = plantype_int;
				plan.title = title.getText().toString();
				plan.start = start.getText().toString();
				plan.end = end.getText().toString();
				plan.dayOfWeek = dayOfWeek;
				plan.initValue = Float.parseFloat(initV.getText().toString().replace("kg", ""));
				plan.targetValue = Float.parseFloat(targetV.getText().toString().replace("kg", ""));
				
				boolean ret = updatePlan(plan);
				if(ret == true) {
					//update alarm to alarmService
					try {
						getServiceInterface().updateAlarm(plan.uId, plan.dayOfWeek, plan.alarm);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					complete();
				}
			}
		}
	}
	
	private void onPopupButtonClick(View view)
	{
		PopupMenu menu = new PopupMenu(this, view);
		
		menu.getMenuInflater().inflate(R.menu.plans_list, menu.getMenu());
		
		menu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				switch(item.getItemId())
				{
				case R.id.action_plantype_diet:
					planType.setText(R.string.str_plantype_diet);
					return true;
				case R.id.action_plantype_reading:
					planType.setText(R.string.str_plantype_reading);
					return true;
				default:
					planType.setText(R.string.str_plantype_diet);
				}
				return false;
			}
		});
		
		menu.show();
	}
	
	
	
}
