/**
 * 
 */
package com.kaist.crescendo.manager;

import android.content.Context;

import com.kaist.crescendo.data.PlanData;
import com.kaist.crescendo.data.UserData;

/**
 * @author S525
 *
 */
public interface UpdateManagerInterface {
	
	public int register(Context context, UserData uData);
	public int login(Context context, UserData uData);
	public int addNewPlan(PlanData plan);
}
