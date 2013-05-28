package com.kaist.crescendo.activity;

import com.kaist.crescendo.R;

import android.os.Bundle;
import android.view.Window;

public class RegisterEditorActivity extends UpdateActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_registereditor);
	}
}
