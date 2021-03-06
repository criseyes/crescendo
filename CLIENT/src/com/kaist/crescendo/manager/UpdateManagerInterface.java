/**
 * 
 */
package com.kaist.crescendo.manager;

import java.util.ArrayList;

import android.content.Context;

import com.kaist.crescendo.data.FriendData;
import com.kaist.crescendo.data.PlanData;
import com.kaist.crescendo.data.UserData;

/**
 * @author S525
 *
 */
public interface UpdateManagerInterface {
	
	public int register(Context context, UserData uData);
	public int login(Context context, UserData uData);
	
	public int addNewPlan(Context context, PlanData plan);
	public int updatePlan(Context context, PlanData plan);
	public int deletePlan(Context context, int plan_uId);
	public int getPlanList(Context context, ArrayList<PlanData> planArrayList);
	
	public int getFriend(Context context, ArrayList<FriendData> friendArrayList);
	public int getCandidate(Context context, ArrayList<FriendData> candidateArrayList);
	public int addNewFriend(Context context, ArrayList<FriendData> friendArrayList);
	public int delFriend(Context context, String friendUserId);
	public int setAvataFriend(Context context, String friendUserId);
	
	public int changePassword(Context context, String curPassword, String newPassword);
	
	public int setHisData(Context context, int planId, String date, int value);
}
