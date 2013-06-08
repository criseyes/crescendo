package com.kaist.crescendo.activity;

import java.util.ArrayList;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.kaist.crescendo.R;
import com.kaist.crescendo.data.PlanData;
import com.kaist.crescendo.data.PlanListAdapter;
import com.kaist.crescendo.utils.MyPref;
import com.kaist.crescendo.utils.MyStaticValue;

public class PlanListActivity extends UpdateActivity {
	
	private static PlanListAdapter adapter;
	private ListView listView;
	private ArrayList<PlanData> planArrayList;
	
	private final int UPDATE_ID = Menu.FIRST;
	private final int DELETE_ID = Menu.FIRST + 1;
	private final int SETDEFAULT_ID = Menu.FIRST + 2;	
	
	public final static PlanListAdapter getAdapterInstance() 
	{
		return adapter;
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		
		super.onCreateContextMenu(menu, v, menuInfo);
		
		menu.setHeaderTitle(R.string.str_plan_context_menu);
		
		menu.add(0, UPDATE_ID, 0, R.string.str_modify_plan2);
		menu.add(0, DELETE_ID, 0, R.string.str_delete_plan);
		menu.add(0, SETDEFAULT_ID, 0, R.string.str_set_default_plan);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo menuInfo;
		menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		int index = 0;
		boolean result = false;
		
		switch(item.getItemId())
		{
		case DELETE_ID:
			
			index = menuInfo.position;
			result = deletePlan(((PlanData) adapter.getItem(index)).uId);
			if(result == true)
			{
				adapter.clearAllItems();
				String ret = getPlanList(planArrayList);
				if(ret.equals("good")) {
					for(int i = 0 ; i < planArrayList.size() ; i++) {
						adapter.addItem(planArrayList.get(i));
					}
					adapter.notifyDataSetChanged();
				}
			}
			return true;
		case UPDATE_ID:
			index = menuInfo.position;
			//result = updatePlan(((PlanData) adapter.getItem(index)).uId);
			// ADD NEW PLAN
			Intent intent = new Intent();
			intent.putExtra(MyStaticValue.MODE, MyStaticValue.MODE_UPDATE);
			intent.putExtra(MyStaticValue.NUMBER, index);
			
			startActivityForResult(intent.setClass(getApplicationContext(), PlanEditorActivity.class), MyStaticValue.REQUESTCODE_UPDATEPLAN);
			return true;
		case SETDEFAULT_ID:
			PlanData plan = (PlanData) adapter.getItem(menuInfo.position);
			adapter.clearSelectedPlan();
			plan.isSelected = true;
			
			if(updatePlan(plan) == true) {
				
			}
			
			/* save info. to preference */
				
			SharedPreferences prefs = getSharedPreferences(MyPref.myPref, MODE_MULTI_PROCESS);
			SharedPreferences.Editor editor = prefs.edit();
			
			editor.putInt(MyPref.MY_AVATA_UID, plan.uId);
			editor.commit();
			
			editor.putInt(MyPref.MY_AVATA_TYPE, plan.type);
			editor.commit();
			
			editor.putString(MyPref.MY_AVATA_TITLE, plan.title);
			editor.commit();
			
			sendBroadCasetIntent();
			adapter.notifyDataSetChanged();
			return true;
		}
		return super.onContextItemSelected(item);
	}
	
	private void sendBroadCasetIntent()
	{
		Intent intent = new Intent(MyStaticValue.ACTION_UPDATEWALLPAPER);

		sendBroadcast(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_planslist);
		setTitle(R.string.str_managing_plans);
		
		
		listView = (ListView) findViewById(R.id.plans_list);
		planArrayList = new ArrayList<PlanData>();		
		
		OnClickListener mClickListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// ADD NEW PLAN
				Intent intent = new Intent();
				intent.putExtra(MyStaticValue.MODE, MyStaticValue.MODE_NEW);
				
				startActivityForResult(intent.setClass(getApplicationContext(), PlanEditorActivity.class), MyStaticValue.REQUESTCODE_ADDNEWPLAN);
			}
		};
		
		findViewById(R.id.button_add_new_plan).setOnClickListener(mClickListener);
		//listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setItemsCanFocus(false);
		listView.setClickable(false);
		listView.setFocusable(false);
		
		//listView.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> adapt, View view, int position, long id) {
//				PlanData plan = (PlanData) adapter.getItem(position);
//				adapter.clearSelectedPlan();
//				plan.isSelected = true;
//				
//				if(updatePlan(plan) == true) {
//					
//				}
//				
//				adapter.notifyDataSetChanged();
//			}
//		});
		
		
		
		/*
		 *  TODO Get Plans List from server
		 *  How to update my list?
		 *  After get list, 
		 */
		adapter = new PlanListAdapter(this);
		
		String result = getPlanList(planArrayList);
		
		if(result.equals("good")) {
			for(int i = 0; i < planArrayList.size(); i++) {
				adapter.addItem(planArrayList.get(i));
			}
		}
	
		/* TODO ÀÌ°Å Áö±Ý ÇÏ¸é Á×À» ÅÙµ¥.. */
		//adapter.setListItems(lit);
		listView.setAdapter(adapter);
		registerForContextMenu(listView);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		
		switch(requestCode){
			case MyStaticValue.REQUESTCODE_ADDNEWPLAN: 
				if(resultCode == RESULT_OK){ 
					boolean result = data.getExtras().getBoolean("success");
					if(result == true) /* user add new plan sucessfully */
					{
						adapter.clearAllItems();
						String ret = getPlanList(planArrayList);
						if(ret.equals("good")) {
							for(int i = 0 ; i < planArrayList.size() ; i++) {
								adapter.addItem(planArrayList.get(i));
							}
							adapter.notifyDataSetChanged();
						}
					}
				}
				break;
			case MyStaticValue.REQUESTCODE_UPDATEPLAN: 
				if(resultCode == RESULT_OK){ 
					boolean result = data.getExtras().getBoolean("success");
					if(result == true) /* user add new plan sucessfully */
					{
						adapter.clearAllItems();
						String ret = getPlanList(planArrayList);
						if(ret.equals("good")) {
							for(int i = 0 ; i < planArrayList.size() ; i++) {
								adapter.addItem(planArrayList.get(i));
							}
						
							adapter.notifyDataSetChanged();
							sendBroadCasetIntent();
						}
					}
				}
				break;
		}
	}
	
}
