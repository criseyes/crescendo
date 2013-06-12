package com.kaist.crescendo.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.os.Bundle;
import android.widget.TextView;

import com.kaist.crescendo.R;

public class HelpsActivity extends UpdateActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_helps);
		setTitle(R.string.str_settings_help);
		
		
		try {
			showText("helps.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	private void showText(String file) throws IOException {
		TextView text = (TextView) findViewById(R.id.textViewHelps);
		InputStream is = getAssets().open(file);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, "ksc5601"));
		StringBuilder sb = new StringBuilder();
		String line;
		
		while((line = reader.readLine()) != null) {
			sb.append(line).append("\n");
		}
		
		is.close();
		
		text.setText(sb.toString());
	}
}
