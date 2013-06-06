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
	public int getPlan(Context context, PlanData plan[]);
	
	public int getFriend(Context context, ArrayList<FriendData> friendArrayList);
	public int getCandidate(Context context, ArrayList<FriendData> candidateArrayList);
	public int addNewFriend(Context context, FriendData friend);
	public int delFriend(Context context, String friendUserId);
	public int setAvataFriend(Context context, String friendUserId);
}
