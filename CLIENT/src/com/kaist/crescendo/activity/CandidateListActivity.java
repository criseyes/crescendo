package com.kaist.crescendo.activity;

import android.os.Bundle;

import com.kaist.crescendo.R;

public class CandidateListActivity extends UpdateActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_planeditor);
		setTitle(R.string.str_add_newfriend);
	}
}