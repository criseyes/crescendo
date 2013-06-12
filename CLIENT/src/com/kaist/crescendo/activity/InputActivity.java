package com.kaist.crescendo.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.kaist.crescendo.R;
import com.kaist.crescendo.data.PlanData;


public class InputActivity extends UpdateActivity {
	private ArrayList<PlanData> planArrayList;
	private String title;
	private int planId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_input);
		setTitle(R.string.str_input_value);
		
		planId = getIntent().getExtras().getInt("planId");
		
		planArrayList = new ArrayList<PlanData>();
		
		if(getPlanList(planArrayList).equals("good")) {
			for(int i = 0; i < planArrayList.size() ; i++) {
				if(planArrayList.get(i).uId == planId) {
					title = planArrayList.get(i).title;
					break;
				}
			}			
		}
		
		TextView titleView = (TextView) findViewById(R.id.textTitle);
		
		titleView.setText("'" + title + "' 목표에 대해서 입력하세요.");
		
		findViewById(R.id.button_ToSend).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Calendar calendar = Calendar.getInstance();
				Date date = calendar.getTime();
				SimpleDateFormat Formatter = new SimpleDateFormat("yyyy-MM-dd");
				EditText value = (EditText) findViewById(R.id.enterVal);
				boolean ret = setHisData(Formatter.format(date), Integer.parseInt(value.getText().toString()));
				if(ret) {
					finish();
				}
			}
		});
	}
}
