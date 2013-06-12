package com.kaist.crescendo.activity;

import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.kaist.crescendo.R;
import com.kaist.crescendo.data.PlanData;
import com.kaist.crescendo.data.PlanListAdapter;
import com.kaist.crescendo.utils.MyStaticValue;

public class PlanViewActivity extends UpdateActivity {
	
	private int mode;
	private int index;
	Calendar startCalendar = Calendar.getInstance();
	Calendar endCalendar = Calendar.getInstance();
	Calendar alarmTime = Calendar.getInstance();
	
	PlanData plan;
	
	TextView startDay;
	TextView endDay;
	
	TextView initValue;
	TextView targetValue;
	
	TextView alarmTimeValue;
	TextView planType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_planview);
		
		
		startDay = (TextView) findViewById(R.id.editStartDate);
		endDay = (TextView) findViewById(R.id.editEndDate);
		initValue = (TextView) findViewById(R.id.editInitValue);
		targetValue = (TextView) findViewById(R.id.editTargetValue);
		
		alarmTimeValue = (TextView) findViewById(R.id.editAlarmTime);
		planType = (TextView) findViewById(R.id.editPlanType);
				
		mode = getIntent().getExtras().getInt(MyStaticValue.MODE);
		
		if(mode == MyStaticValue.MODE_VIEW) /* user want to update existing plan */
		{
			setTitle(R.string.str_planview);
			// TODO Fill data for each field.
			
			index = getIntent().getExtras().getInt(MyStaticValue.NUMBER);
			PlanListAdapter adapter = PlanListActivity.getAdapterInstance();
			plan = (PlanData) adapter.getItem(index);
			startDay.setText(plan.start);
			endDay.setText(plan.end);
			initValue.setText(Double.toString(plan.initValue));
			targetValue.setText(Double.toString(plan.targetValue));
			((TextView) findViewById(R.id.editTile)).setText(plan.title);
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
				
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
				
		getMenuInflater().inflate(R.menu.plan_view, menu);    
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent();
		
		switch(item.getItemId())
		{
		case R.id.action_editPlan:
			
			intent.putExtra(MyStaticValue.MODE, MyStaticValue.MODE_UPDATE);
			intent.putExtra(MyStaticValue.NUMBER, index);
			break;
		case R.id.action_deletePlan:
			intent.putExtra(MyStaticValue.MODE, MyStaticValue.MODE_DELETE);
			intent.putExtra(MyStaticValue.NUMBER, index);
			break;
			
		case R.id.action_inputPlan:
	    	Intent i = new Intent(this, InputActivity.class);
	    	i.putExtra("planId", plan.uId);
	    	
	    	startActivity(i);
	    	return true;
		}
		
		/*
		 *  TODO if failed, you should insert "false" not "true"
		 */
		intent.putExtra("success", true);
		this.setResult(RESULT_OK, intent); 
		this.finish();
		

		return true;
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
	
		
}
