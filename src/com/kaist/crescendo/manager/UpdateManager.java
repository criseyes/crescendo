package com.kaist.crescendo.manager;

import android.content.Context;

public class UpdateManager implements UpdateManagerInterface {
	public boolean register(Context context, String string) {
		boolean result = false;
		new SendAsyncTask(context).execute(string);
		return result;
	}
	
	public boolean login(Context context, String Uid, String Pw) {
		boolean result = false;
		new SendAsyncTask(context).execute(Uid+Pw);
		return result;
	}

}
