package com.kaist.crescendo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.kaist.crescendo.R;

public class PlanEditorActivity extends UpdateActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_planeditor);
		
		OnClickListener mClickListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 *  TODO Call add New Plan
				 */
				//PlanData plan = new PlanData(type, title, start, end);
				//addNewPlan();
				
			}
		};
		
		findViewById(R.id.button_add_new_plan).setOnClickListener(mClickListener);
	}
	
	private void complete()
	{
		Intent intent = new Intent();
		Bundle extra = new Bundle();
		/*
		 *  TODO if failed, you should insert "false" not "true"
		 */
		extra.putBoolean("success", true);
		intent.putExtras(extra);
		this.setResult(RESULT_OK, intent); 
		this.finish();
		
	}
}
