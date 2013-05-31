package com.kaist.crescendo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.kaist.crescendo.R;
import com.kaist.crescendo.activity.UpdateActivity;

public class MainActivity extends UpdateActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
	} 
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/* handling buttons */
	Button.OnClickListener mClickListener = new View.OnClickListener()
	{
	      public void onClick(View v)
	      {
	    	  Intent intent = new Intent();
	           switch (v.getId())
	           {
	           		case R.id.main_plans_list:
	           			intent.setClass(getApplicationContext(), PlanListActivity.class);
	           			startActivity(intent);
	           			break;
	           		case R.id.main_friends_list:
	           			break;
	           		case R.id.main_manage_settings:
	           			break;
	           		case R.id.main_manage_widget:
	           			break;
	           		case R.id.main_view_status:
	           			break;
	           		default:
	           			break;
	           }
	           
	      }
	};
	
	

}
