package com.kaist.crescendo.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;

import com.kaist.crescendo.R;

public class EntranceActivity extends UpdateActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_entrance);
		setTitle(R.string.str_title_welcome);
	}
}
