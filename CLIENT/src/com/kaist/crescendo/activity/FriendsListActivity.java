package com.kaist.crescendo.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.kaist.crescendo.R;
import com.kaist.crescendo.data.FriendData;
import com.kaist.crescendo.data.FriendListAdapter;
import com.kaist.crescendo.data.PlanData;
import com.kaist.crescendo.utils.MyPref;
import com.kaist.crescendo.utils.MyStaticValue;

public class FriendsListActivity extends UpdateActivity {
	
	private FriendListAdapter adapter;
	private ListView listView;
	static private ArrayList<FriendData> friendArrayList;
	
	private final int MENU_ID_DELETE = Menu.FIRST;
	private final int MENU_ID_CANCEL = Menu.FIRST + 1;
	private final int MENU_ID_AVATA = Menu.FIRST + 2;
	
	public static PlanData getFriendsPlan(int friendIndex)
	{
		if(friendIndex < friendArrayList.size())
			return friendArrayList.get(friendIndex).plan;
		return null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_friendlist);
		setTitle(R.string.str_managing_friend);
		
		
		listView = (ListView) findViewById(R.id.plans_list);
		listView.setOnItemClickListener(mListClickListener);
		
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
		
		menu.setHeaderTitle(R.string.str_manage_friend);
		menu.add(0, MENU_ID_DELETE, Menu.NONE, R.string.str_delete);
		menu.add(0, MENU_ID_CANCEL, Menu.NONE, R.string.str_cancel);
		menu.add(0, MENU_ID_AVATA, Menu.NONE, R.string.str_setmyAvata);
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
		case MENU_ID_AVATA:
			menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
			FriendData friend1 = (FriendData) adapter.getItem(menuInfo.position);
			String ret1 = setAvata(friend1.id);
			
			if(ret1 == "good") {
				ret = getFriendList(friendArrayList);
				
				if(ret.equals("good")) {
					adapter.clearAllItems();
					for(int i = 0; i < friendArrayList.size(); i++) {
						adapter.addItem(friendArrayList.get(i));
					}
					adapter.notifyDataSetChanged();
					updateAvataFriend(adapter.getAvataFriend());
					
				}
			}
			break;
		case MENU_ID_CANCEL:
			break;
		}
		return true;
	}
	
	private void updateAvataFriend(FriendData friend)
	{
		/* save info. to preference */
		
		SharedPreferences prefs = getSharedPreferences(MyPref.myPref, MODE_MULTI_PROCESS);
		SharedPreferences.Editor editor = prefs.edit();
		
		if(friend == null)
			editor.putInt(MyPref.FRIEND_AVATA_UID, 0);
		else
			editor.putInt(MyPref.FRIEND_AVATA_UID, friend.plan.uId); /* none */
		editor.commit();
		
		if(friend == null)
			editor.putInt(MyPref.FRIEND_AVATA_TYPE, 0);
		else
			editor.putInt(MyPref.FRIEND_AVATA_TYPE, friend.plan.type);
		editor.commit();
		
		if(friend == null)
			editor.putString(MyPref.FRIEND_AVATA_NAME, "");
		else
			editor.putString(MyPref.FRIEND_AVATA_NAME, friend.plan.title);
		editor.commit();
		
		if(friend == null)
			editor.putInt(MyPref.MY_AVATA_PROGRESS, 0);
		else {
			int progress = 0;
			

			editor.putInt(MyPref.MY_AVATA_PROGRESS, progress );
		}
		editor.commit();
		
		
		sendBroadCasetIntent();
		adapter.notifyDataSetChanged();
	}
	
	private void sendBroadCasetIntent()
	{
		Intent intent = new Intent(MyStaticValue.ACTION_UPDATEWALLPAPER);

		sendBroadcast(intent);
	}
	
	OnItemClickListener mListClickListener = new OnItemClickListener() {
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent();
			intent.putExtra(MyStaticValue.MODE, MyStaticValue.MODE_FRIEND_VIEW);
			intent.putExtra(MyStaticValue.NUMBER, position);
			
			startActivityForResult(intent.setClass(getApplicationContext(), PlanViewActivity.class), MyStaticValue.REQUESTCODE_VIEWPLAN);
			
		}
	};
	
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
