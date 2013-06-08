package com.kaist.crescendo.activity;

import com.kaist.crescendo.R;

import android.os.Bundle;

public class HelpsActivity extends UpdateActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_helps);
		setTitle(R.string.str_settings_help);
	}
}
