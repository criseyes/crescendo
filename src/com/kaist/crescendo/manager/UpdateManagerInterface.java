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
	
	public boolean register(Context context, String string);	
	public boolean login(Context context, String Uid, String Pw);
}
