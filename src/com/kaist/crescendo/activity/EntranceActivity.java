package com.kaist.crescendo.activity;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.kaist.crescendo.R;
import com.kaist.crescendo.utils.MyPref;

public class EntranceActivity extends UpdateActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_entrance);
		setTitle(R.string.str_title_welcome);
	}
	
	/*  save latest session information */
	private void SaveSessionStatus(boolean status) {
		SharedPreferences prefs = getSharedPreferences("PrefName", MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(MyPref.KEY_SESSION, status);
		editor.commit();
	}
}
