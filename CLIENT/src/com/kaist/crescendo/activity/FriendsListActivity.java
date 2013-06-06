package com.kaist.crescendo.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.kaist.crescendo.R;
import com.kaist.crescendo.data.FriendData;
import com.kaist.crescendo.data.FriendListAdapter;
import com.kaist.crescendo.utils.MyStaticValue;

public class FriendsListActivity extends UpdateActivity {
	
	private FriendListAdapter adapter;
	private ListView listView;
	private ArrayList<FriendData> friendArrayList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_friendlist);
		setTitle(R.string.str_managing_friend);
		
		
		listView = (ListView) findViewById(R.id.plans_list);
		friendArrayList = new ArrayList<FriendData>();
		
		OnClickListener mClickListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// ADD NEW PLAN
				Intent intent = new Intent();
				intent.putExtra(MyStaticValue.MODE, MyStaticValue.MODE_NEW);
				
				startActivityForResult(intent.setClass(getApplicationContext(), CandidateListActivity.class), MyStaticValue.REQUESTCODE_ADDNEWFRIEND);
			}
		};
		
		findViewById(R.id.button_add_new_plan).setOnClickListener(mClickListener);
		
		/*
		 *  TODO Get Plans List from server
		 *  How to update my list?
		 *  After get list, 
		 */		
		adapter = new FriendListAdapter(this);
		
		String result = getFriendList(friendArrayList);
		
		if(result.equals("good")) {
			for(int i = 0; i < friendArrayList.size(); i++) {
				adapter.addItem(friendArrayList.get(i));
			}
		}
		
		/* TODO ÀÌ°Å Áö±Ý ÇÏ¸é Á×À» ÅÙµ¥.. */
		//adapter.setListItems(lit);
		listView.setAdapter(adapter);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		
		switch(requestCode){
			case MyStaticValue.REQUESTCODE_ADDNEWFRIEND: 
				if(resultCode == RESULT_OK){ 
					boolean result = data.getExtras().getBoolean("success");
					if(result == true) /* user add new plan sucessfully */
					{
						String ret = getFriendList(friendArrayList);
						
						if(ret.equals("good")) {
							for(int i = 0; i < friendArrayList.size(); i++) {
								adapter.addItem(friendArrayList.get(i));
								adapter.notifyDataSetChanged();
							}
						}
					}
				}
		}
	}
	
}
