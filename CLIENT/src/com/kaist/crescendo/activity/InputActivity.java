package com.kaist.crescendo.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.kaist.crescendo.R;
import com.kaist.crescendo.data.PlanData;
import com.kaist.crescendo.utils.MyPref;
import com.kaist.crescendo.utils.MyStaticValue;


@SuppressLint("SimpleDateFormat")
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
				boolean ret = setHisData(planId, Formatter.format(date), Integer.parseInt(value.getText().toString()));
				if(ret) {
				    updatePlan(planId, Integer.parseInt(value.getText().toString()));
					finish();
				}
			}

            private void updatePlan(int planId, int value) {
                for(int i = 0; i < planArrayList.size() ; i++) {
                    if(planArrayList.get(i).uId == planId) {
                        if(planArrayList.get(i).isSelected == true)
                        {
                            PlanData plan = planArrayList.get(i);
                            int progress;
                            if(plan.hItem == null || plan.hItem.size() == 0 || plan.initValue <value)
                                progress = 0;
                            else
                                progress = (int) (100*Math.abs(plan.initValue - value) / Math.abs(plan.targetValue - plan.initValue));
                            
                            if(plan.targetValue > value)
                                progress = 100;
                            SharedPreferences prefs = getSharedPreferences(MyPref.myPref, MODE_MULTI_PROCESS);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putInt(MyPref.MY_AVATA_PROGRESS, progress );
                            editor.commit();
                            
                            
                            Intent intent = new Intent(MyStaticValue.ACTION_UPDATEWALLPAPER);

                            sendBroadcast(intent);
                        }
                        break;
                    }
                }   
                
            }
		});
	}
}
