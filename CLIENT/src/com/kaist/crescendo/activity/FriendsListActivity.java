package com.kaist.crescendo.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.kaist.crescendo.R;
import com.kaist.crescendo.data.FriendData;
import com.kaist.crescendo.data.FriendListAdapter;
import com.kaist.crescendo.data.PlanData;
import com.kaist.crescendo.utils.MyStaticValue;

public class FriendsListActivity extends UpdateActivity {
	
	private FriendListAdapter adapter;
	private ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_friendlist);
		setTitle(R.string.str_managing_friend);
		
		
		listView = (ListView) findViewById(R.id.plans_list);
		
		
		OnClickListener mClickListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// ADD NEW PLAN
				Intent intent = new Intent();
				intent.putExtra(MyStaticValue.MODE, MyStaticValue.MODE_NEW);
				
				startActivityForResult(intent.setClass(getApplicationContext(), CandidateListActivity.class), MyStaticValue.REQUESTCODE_ADDNEWPLAN);
			}
		};
		
		findViewById(R.id.button_add_new_plan).setOnClickListener(mClickListener);
		
		/*
		 *  TODO Get Plans List from server
		 *  How to update my list?
		 *  After get list, 
		 */
		getPlanList();
		adapter = new FriendListAdapter(this);
		
		/* 
		 *  temp code 
		 *  TODO remove below codes.
		 */
		SimpleDateFormat Formatter = new SimpleDateFormat("yyyy-MM-dd");
		String date = Formatter.format(new Date());
		
		PlanData plan = new PlanData(MyStaticValue.PLANTYPE_DIET, "Test plan1", date, date);
		PlanData plan1 = new PlanData(MyStaticValue.PLANTYPE_DIET, "Test plan2", date, date);
		PlanData plan2 = new PlanData(MyStaticValue.PLANTYPE_DIET, "Test plan3", date, date);
		FriendData f1 = new FriendData("huiseo@gmail.com", "01022846035", plan, false);
		FriendData f2 = new FriendData("kimtaehee@gmail.com", "01000050125", plan1, false);
		FriendData f3 = new FriendData("songhyegyo@gmail.com", "01012348521", plan2 , false);
		adapter.addItem(f1);
		adapter.addItem(f2);
		adapter.addItem(f3);
		
		/* TODO ÀÌ°Å Áö±Ý ÇÏ¸é Á×À» ÅÙµ¥.. */
		//adapter.setListItems(lit);
		listView.setAdapter(adapter);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		
		switch(requestCode){
			case MyStaticValue.REQUESTCODE_ADDNEWPLAN: 
				if(resultCode == RESULT_OK){ 
					boolean result = data.getExtras().getBoolean("sucess");
					if(result == true) /* user add new plan sucessfully */
					{
						/* 
						 *  TODO  update list once more
						 */
					}
				}
		}
	}
	
}
