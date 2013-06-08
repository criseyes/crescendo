package com.kaist.crescendo.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.kaist.crescendo.data.FriendData;
import com.kaist.crescendo.data.PlanData;
import com.kaist.crescendo.data.UserData;
import com.kaist.crescendo.manager.UpdateManager;
import com.kaist.crescendo.manager.UpdateManagerInterface;
import com.kaist.crescendo.utils.MyPref;

public class UpdateActivity extends Activity {
	private final static UpdateManagerInterface mManager = new UpdateManager(); // singleton
	private Context mContext;
	
	public static UpdateManagerInterface getInstance() 
	{
	     return mManager;
	}
	
	protected String login(String id, String pw)
	{		
		String sessionId = new String();
		int ret = -1;
		sessionId = "bad";
		/*
		 *  TODO call manager's method to login.
		 */
		// test
		ret = mManager.login(mContext, new UserData(id, pw));
		if(ret == 0) {
			sessionId = "good";
		}
		return sessionId;
	}
	
	protected String register(String id, String pw, String phone, String birth)
	{
		String result = new String();
		int ret = -1;
		result = "bad";
		/*
		 *  TODO call manager's method to register
		 */
		
		ret = mManager.register(mContext, new UserData(id, pw, phone, birth));
		if(ret == 0) {
			result = "good";
		}
		return result;
	}
	
	protected String addNewPlan(PlanData plan)
	{
		String result = new String();
		int ret = -1;
		/*
		 *  TODO call manager's method to register
		 */
		
		ret = mManager.addNewPlan(mContext, plan);
		if(ret == 0) {
			result = "good";
		}
		return result;
	}
	
	protected boolean deletePlan(int uid)
	{
		boolean result = false;
		int ret = -1;
		/*
		 * TODO call manager's method to delete a plan
		 */
		ret = mManager.deletePlan(mContext, uid);
		if(ret == 0) {
			result = true;
		}
		return result;
	}
	
	protected boolean updatePlan(PlanData plan)
	{
		boolean result = false;
		int ret = -1;
		/*
		 * TODO call manager's method to update a plan
		 */
		ret = mManager.updatePlan(mContext, plan);
		if(ret == 0) {
			result = true;
		}
		return result;
	}
	
	protected String getPlanList(ArrayList<PlanData> planArrayList)
	{
		String result = new String();
		int ret = -1;
		
		planArrayList.clear();
		
		ret = mManager.getPlanList(mContext, planArrayList);
		if(ret == 0) {
			result = "good";
		}
		return result;
	}
	
	protected String getFriendList(ArrayList<FriendData> friendArrayList) {
		String result = new String();
		int ret = -1;
		
		friendArrayList.clear();
		
		ret = mManager.getFriend(mContext, friendArrayList);
		
		if(ret == 0) {
			result = "good";
		}
		
		return result;
	}
	
	protected String getCandidateList(ArrayList<FriendData> candidateArrayList) {
		String result = new String();
		int ret = -1;
		
		ret = mManager.getCandidate(mContext, candidateArrayList);
		
		if(ret == 0) {
			result = "good";
		}
		
		return result;
	}
	
	protected String setFriend(ArrayList<FriendData> friendArrayList) {
		String result = new String();
		int ret = -1;
		
		ret = mManager.addNewFriend(mContext, friendArrayList);
				
		if(ret == 0) {
			result = "good";
		}
		
		return result;
	}
	
	protected String delFriend(String uid) {
		String result = new String();
		int ret = -1;
		
		ret = mManager.delFriend(mContext, uid);
				
		if(ret == 0) {
			result = "good";
		}
		
		return result;
	}
	
	protected boolean changePassword(String curPassword, String newPassword) {
		boolean result = false;
		int ret = -1;
		
		ret = mManager.changePassword(mContext, curPassword, newPassword);
		
		if(ret == 0) {
			result = true;
		}
		
		return result;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this; /* save context for each activity */
	}
	
	@Override
	public void finish() {
		// override to show the animation effect
		super.finish();
		//makeEffect();
	}
	
	@Override
	public void startActivity(Intent intent) {
		// override to show the animation effect
		super.startActivity(intent);
		makeEffect();
	}
	
	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		// override to show the animation effect
		super.startActivityForResult(intent, requestCode);
		makeEffect();
	}
	
	private void makeEffect()
	{
		//overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_in_left);
	}
	
	protected String getMyID()
	{
		SharedPreferences prefs = getSharedPreferences(MyPref.myPref, MODE_PRIVATE);
		String myId = prefs.getString(MyPref.KEY_MYID, "");
		return myId;
	}
}
