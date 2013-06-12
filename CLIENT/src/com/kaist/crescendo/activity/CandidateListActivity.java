package com.kaist.crescendo.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.kaist.crescendo.R;
import com.kaist.crescendo.data.CandidateListAdapter;
import com.kaist.crescendo.data.FriendData;

public class CandidateListActivity extends UpdateActivity {

	private CandidateListAdapter adapter;
	private ListView listView;
	private ArrayList<FriendData> candidateArrayList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_friendselector);
		setTitle(R.string.str_add_newfriend);
		
		listView = (ListView) findViewById(R.id.plans_list);
		candidateArrayList = new ArrayList<FriendData>();
		
		OnClickListener mClickListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				VerifyAndSave();
			}
		};
		
		findViewById(R.id.button_save_new_friend).setOnClickListener(mClickListener);
		
		adapter = new CandidateListAdapter(this);
		
		String result = getCandidateList(candidateArrayList);
		
		if(result.equals("good")) {
			for(int i = 0; i < candidateArrayList.size(); i++) {
				adapter.addItem(candidateArrayList.get(i));
			}
		}
				
		/* TODO ÀÌ°Å Áö±Ý ÇÏ¸é Á×À» ÅÙµ¥.. */
		//adapter.setListItems(lit);
		listView.setAdapter(adapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listView.setItemsCanFocus(false);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapt, View view, int position, long id) {
				FriendData friend = (FriendData) adapter.getItem(position);
				friend.setselected(!friend.getisSelected());
				adapter.notifyDataSetChanged();
			}
		});
	}
	
	private void complete()
	{
		Intent intent = new Intent();
		/*
		 *  TODO if failed, you should insert "false" not "true"
		 */
		intent.putExtra("success", true);
		this.setResult(RESULT_OK, intent); 
		this.finish();
		
	}
	
	protected void VerifyAndSave() {
		//send to server
		ArrayList<FriendData> tmp = new ArrayList<FriendData>();
		for(int i = 0 ; i <adapter.getCount() ; i++) {
			FriendData friend = (FriendData) adapter.getItem(i);
			if(friend.getisSelected() == true) {
				tmp.add(friend);
			}
		}
		
		if(tmp.size() > 0) {
			if(setFriend(tmp).equals("good")) {
				complete();
			}
		}
	}
}