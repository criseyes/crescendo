/**
 * 
 */
package com.kaist.crescendo.manager;

import android.content.Context;

/**
 * @author S525
 *
 */
public interface UpdateManagerInterface {
	
	public int register(Context context, String id, String pw, String phone, String birth);	
	public int login(Context context, String id, String pw);
}
