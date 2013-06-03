package com.kaist.crescendo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.kaist.crescendo.R;

public class SettingsActivity extends UpdateActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_settings);
		setTitle(R.string.str_settings_title);
		
		findViewById(R.id.setttings_changepw).setOnClickListener(mClickListener);
		findViewById(R.id.settings_help).setOnClickListener(mClickListener);
		
		((Switch) findViewById(R.id.settings_setalarm)).setOnCheckedChangeListener(mSwitchListener);
		((Switch) findViewById(R.id.settings_setwidget)).setOnCheckedChangeListener(mSwitchListener);
		/*
		 *  TODO, set initial value
		 */
	}
	
	public Switch.OnCheckedChangeListener mSwitchListener = new Switch.OnCheckedChangeListener()

	{

		@Override
		public void onCheckedChanged(CompoundButton v, boolean isChecked) {
			// TODO Auto-generated method stub
			switch (v.getId())
           {
           		case R.id.settings_setalarm:
           			/* 
           			 * TODO
           			 */
           			break;
           		case R.id.settings_setwidget:
           			/*
           			 * TODO
           			 */
           			
           			break;
           }
		}

	};
	
	
	/* handling buttons */
	Button.OnClickListener mClickListener = new View.OnClickListener()
	{
	      public void onClick(View v)
	      {
	    	  Intent intent = new Intent();
	           switch (v.getId())
	           {
	           		case R.id.setttings_changepw:
	           			/* 
	           			 * TODO
	           			 */
	           			break;
	           		case R.id.settings_help:
	           			//intent.setClass(getApplicationContext(), FriendsListActivity.class);
	           			/* 
	           			 * TODO
	           			 */
	           			break;
	           }
	           //startActivity(intent);
	      }
	};
}
