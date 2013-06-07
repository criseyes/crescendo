package com.kaist.crescendo.activity;

import com.kaist.crescendo.R;
import com.kaist.crescendo.data.GraphView;
import com.kaist.crescendo.utils.MyStaticValue;

import android.os.Bundle;

public class StatusActivity extends UpdateActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_status);
		setTitle(R.string.str_show_status);
		
		float[] values = new float[] { 2.0f,1.5f, 2.5f, 1.0f , 3.0f, 30.f };
		String[] verlabels = new String[] { "great", "ok", "bad", "nice", "good" };
		String[] horlabels = new String[] { "today", "tomorrow", "next week", "next month" };
		GraphView graphView = new GraphView(this, values, MyStaticValue.myId+"¥‘¿« ¡¯√¥µµ" ,horlabels, verlabels, GraphView.LINE);
		setContentView(graphView);
		
	}

}
