package com.kaist.crescendo.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.kaist.crescendo.R;


public class InputActivity extends UpdateActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_input);
		setTitle(R.string.str_input_value);
		
		
		
		findViewById(R.id.button_ToSend).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				finish();			
			}
		});
	}
}
