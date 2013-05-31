package com.kaist.crescendo.manager;

import org.json.JSONException;
import org.json.JSONObject;

import com.kaist.crescendo.data.PlanData;

import android.content.Context;

public class UpdateManager implements UpdateManagerInterface {
	private void makeMsgHeader(JSONObject msg, int msgId) {
		try {
			msg.put(MsgInfo.MSGID_LABEL, msgId);
			msg.put(MsgInfo.MSGDIR_LABEL, MsgInfo.MSG_SEND_VALUE);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public int register(Context context, String id, String pw, String phone, String birth) {
		int result = MsgInfo.STATUS_OK;
		JSONObject msg = new JSONObject();
		
		makeMsgHeader(msg, MsgInfo.REGISTER_ID);
				
		try {
			msg.put(MsgInfo.USERID_LABEL, id);
			msg.put(MsgInfo.PASSWORD_LABEL, pw);
			msg.put(MsgInfo.PHONENUM_LABLE, phone);
			msg.put(MsgInfo.BIRTHDAY_LABEL, birth);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		new SendAsyncTask(context).execute(msg);
		return result;
	}
	
	public int login(Context context, String id, String pw) {
		int result = MsgInfo.STATUS_OK;
		JSONObject msg = new JSONObject();
		
		makeMsgHeader(msg, MsgInfo.LOGIN_ID);
				
		try {
			msg.put(MsgInfo.USERID_LABEL, id);
			msg.put(MsgInfo.PASSWORD_LABEL, pw);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		new SendAsyncTask(context).execute(msg);
		return result;
	}

	@Override
	public int addNewPlan(PlanData plan) {
		// TODO Auto-generated method stub
		return 0;
	}

}
