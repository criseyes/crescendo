package com.kaist.crescendo.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kaist.crescendo.R;
import com.kaist.crescendo.data.FriendData;
import com.kaist.crescendo.data.FriendListAdapter;
import com.kaist.crescendo.utils.MyStaticValue;

public class FriendsListActivity extends UpdateActivity {
	
	private FriendListAdapter adapter;
	private ListView listView;
	private ArrayList<FriendData> friendArrayList;
	
	private final int MENU_ID_DELETE = Menu.FIRST;
	private final int MENU_ID_CANCEL = Menu.FIRST + 1;

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
		
		adapter = new FriendListAdapter(this);
		
		
		//get friend list from server
		String result = getFriendList(friendArrayList);
		
		if(result.equals("good")) {
			for(int i = 0; i < friendArrayList.size(); i++) {
				adapter.addItem(friendArrayList.get(i));
			}
		}
		
		/* TODO ÀÌ°Å Áö±Ý ÇÏ¸é Á×À» ÅÙµ¥.. */
		//adapter.setListItems(lit);
		listView.setAdapter(adapter);
		
		registerForContextMenu(listView);

	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		
		menu.setHeaderTitle(R.string.str_delete_friend);
		menu.add(0, MENU_ID_DELETE, Menu.NONE, R.string.str_delete);
		menu.add(0, MENU_ID_CANCEL, Menu.NONE, R.string.str_cancel);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		super.onContextItemSelected(item);
		AdapterView.AdapterContextMenuInfo menuInfo;
		int index;
		switch(item.getItemId()) {
		case MENU_ID_DELETE:
			menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
			index = menuInfo.position;
			FriendData friend = (FriendData) adapter.getItem(index);
			String ret = delFriend(friend.id);
			
			if(ret == "good") {
				ret = getFriendList(friendArrayList);
				
				if(ret.equals("good")) {
					adapter.clearAllItems();
					for(int i = 0; i < friendArrayList.size(); i++) {
						adapter.addItem(friendArrayList.get(i));
					}
					adapter.notifyDataSetChanged();
				}
			}
			
			break;
		case MENU_ID_CANCEL:
			break;
		}
		return true;
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
							adapter.clearAllItems();
							for(int i = 0; i < friendArrayList.size(); i++) {
								adapter.addItem(friendArrayList.get(i));
							}
							adapter.notifyDataSetChanged();
						}
					}
				}
		}
	}
	
}
