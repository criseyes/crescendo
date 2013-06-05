package com.kaist.crescendo.manager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.kaist.crescendo.R;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.kaist.crescendo.data.FriendData;
import com.kaist.crescendo.data.HistoryData;
import com.kaist.crescendo.data.PlanData;
import com.kaist.crescendo.data.UserData;
import com.kaist.crescendo.utils.MyStaticValue;

public class UpdateManager implements UpdateManagerInterface {
	private String asyncTaskResult;
	private int asyncTaskState;
	private Context mContext;
	
	private void showToastPopup(int result) {
		switch(result) {
		case MsgInfo.STATUS_OK:
			Toast.makeText(mContext, mContext.getString(R.string.str_success), Toast.LENGTH_LONG).show();
			break;
		case MsgInfo.STATUS_DUPLICATED_USERID:
			Toast.makeText(mContext, mContext.getString(R.string.str_duplicated_user), Toast.LENGTH_LONG).show();
			break;
		case MsgInfo.STATUS_INVALID_PASSWORD:
			Toast.makeText(mContext, mContext.getString(R.string.str_invalid_password), Toast.LENGTH_LONG).show();
			break;
		case MsgInfo.STATUS_UNREGISTERED_USERID:
			Toast.makeText(mContext, mContext.getString(R.string.str_unresigered_user), Toast.LENGTH_LONG).show();
			break;
		default:
				break;
		}
	}
	
	private void makeMsgHeader(JSONObject msg, int msgId) {
		try {
			msg.put(MsgInfo.MSGID_LABEL, msgId);
			msg.put(MsgInfo.MSGUID, MyStaticValue.myId);
			msg.put(MsgInfo.MSGDIR_LABEL, MsgInfo.MSG_SEND_VALUE);
			msg.put(MsgInfo.MSGLEN_LABLE, 0);
			msg.put(MsgInfo.MSGRET_LABEL, MsgInfo.STATUS_OK);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	private void makeMsgBody(JSONObject msg, Object body) {
		int msgId = 0;
		UserData uData = null;
		PlanData pData = null;
		JSONArray HisArray = null;
		
		JSONObject temp_body = new JSONObject();
		
		try {
			msgId = msg.getInt(MsgInfo.MSGID_LABEL);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		switch(msgId) {
		case MsgInfo.REGISTER_USER:
			uData = (UserData)body;
			try {
				temp_body.put(MsgInfo.USERID_LABEL, uData.id);
				temp_body.put(MsgInfo.PASSWORD_LABEL, uData.password);
				temp_body.put(MsgInfo.PHONENUM_LABEL, uData.phoneNum);
				temp_body.put(MsgInfo.BIRTHDAY_LABEL, uData.birthDay);
				
				msg.put(MsgInfo.MSGBODY_LABEL, temp_body);
			} catch (Exception e) {
				// TODO: handle exception
			}
			break;
		case MsgInfo.SYS_LOGIN:
			uData = (UserData)body;
			try {
				temp_body.put(MsgInfo.USERID_LABEL, uData.id);
				temp_body.put(MsgInfo.PASSWORD_LABEL, uData.password);
				
				msg.put(MsgInfo.MSGBODY_LABEL, temp_body);
			} catch (Exception e) {
				// TODO: handle exception
			}
			break;
		case MsgInfo.ADD_NEW_PLAN:
		case MsgInfo.UPDATE_PLAN:
			pData = (PlanData)body;
			try {
				temp_body.put(MsgInfo.PLAN_UID_LABEL, pData.uId);
				temp_body.put(MsgInfo.PLAN_TYPE_LABEL, pData.type);
				temp_body.put(MsgInfo.PLAN_TITLE_LABEL, pData.title);
				temp_body.put(MsgInfo.PLAN_DAYOFWEEK_LABEL, pData.dayOfWeek);
				temp_body.put(MsgInfo.PLAN_SDATE_LABEL, pData.start);
				temp_body.put(MsgInfo.PLAN_EDATE_LABEL, pData.end);
				
				if(pData.hItem.size() != 0) {
					HisArray = new JSONArray();
				}
				
				for(int i = 0 ; i < pData.hItem.size() ; i++) {
					//add plan history data using JSONArray
					JSONObject hData = new JSONObject();
					hData.put(MsgInfo.PLAN_HISDATE_LABEL, ((HistoryData)pData.hItem.get(i)).Date);
					hData.put(MsgInfo.PLAN_HISVAL_LABEL, ((HistoryData)pData.hItem.get(i)).value);
					HisArray.put(i, hData);
				}
				
				if(HisArray != null) {
					temp_body.put(MsgInfo.PLAN_HISTORY_LABEL, HisArray);
				}
				
				msg.put(MsgInfo.MSGBODY_LABEL, temp_body);
			} catch (Exception e) {
				// TODO: handle exception
			}			
			break;
		default:
			break;
		}
		
		
	}
	
	private void makeMsgBody(JSONObject msg, int planId) {
		int msgId = 0;
		
		JSONObject temp_body = new JSONObject();
		
		try {
			msgId = msg.getInt(MsgInfo.MSGID_LABEL);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		if(msgId == MsgInfo.DEL_PLAN) {
			try {
				temp_body.put(MsgInfo.PLAN_UID_LABEL, planId);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void makeMsgBody(JSONObject msg, String userId) {
		int msgId = 0;
		
		JSONObject temp_body = new JSONObject();
		
		try {
			msgId = msg.getInt(MsgInfo.MSGID_LABEL);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		if(msgId == MsgInfo.DEL_FRIEND || msgId == MsgInfo.SEL_AVATA_FRIEND) {
			try {
				temp_body.put(MsgInfo.USERID_LABEL, userId);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public int register(Context context, UserData uData) {
		int result = MsgInfo.STATUS_OK;
		JSONObject msg = new JSONObject();
		JSONObject revMsg = null;
		
		mContext = context;
		
		makeMsgHeader(msg, MsgInfo.REGISTER_USER);
		
		makeMsgBody(msg, uData);
		
		new SendAsyncTask().execute(msg);
		
		while(asyncTaskState == -1) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			revMsg = new JSONObject(asyncTaskResult);
			result = revMsg.getInt(MsgInfo.MSGRET_LABEL);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		showToastPopup(result);
		
		return result;
	}
	
	public int login(Context context, UserData uData) {
		int result = MsgInfo.STATUS_OK;
		JSONObject msg = new JSONObject();
		JSONObject revMsg = null;
		
		mContext = context;
		
		makeMsgHeader(msg, MsgInfo.SYS_LOGIN);
		
		makeMsgBody(msg, uData);
		
		new SendAsyncTask().execute(msg);
		
		while(asyncTaskState == -1) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
		}
		
		try {
			revMsg = new JSONObject(asyncTaskResult);
			result = revMsg.getInt(MsgInfo.MSGRET_LABEL);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		showToastPopup(result);
		
		return result;
	}
	
	@Override
	public int addNewPlan(Context context, PlanData plan) {
		int result = MsgInfo.STATUS_OK;
		JSONObject msg = new JSONObject();
		JSONObject revMsg = null;
		
		mContext = context;
		
		makeMsgHeader(msg, MsgInfo.ADD_NEW_PLAN);
		
		makeMsgBody(msg, plan);
		
		new SendAsyncTask().execute(msg);
		
		while(asyncTaskState == -1) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
		}
		
		try {
			revMsg = new JSONObject(asyncTaskResult);
			result = revMsg.getInt(MsgInfo.MSGRET_LABEL);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		showToastPopup(result);
		
		return result;
	}
	
	@Override
	public int updatePlan(Context context, PlanData plan) {
		int result = MsgInfo.STATUS_OK;
		JSONObject msg = new JSONObject();
		JSONObject revMsg = null;
		
		mContext = context;
		
		makeMsgHeader(msg, MsgInfo.UPDATE_PLAN);
		
		makeMsgBody(msg, plan);
		
		new SendAsyncTask().execute(msg);
		
		while(asyncTaskState == -1) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
		}
		
		try {
			revMsg = new JSONObject(asyncTaskResult);
			result = revMsg.getInt(MsgInfo.MSGRET_LABEL);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		showToastPopup(result);
		
		return result;
	}

	@Override
	public int deletePlan(Context context, int plan_uId) {
		int result = MsgInfo.STATUS_OK;
		JSONObject msg = new JSONObject();
		JSONObject revMsg = null;
		
		mContext = context;
		
		makeMsgHeader(msg, MsgInfo.SYS_LOGIN);
		
		makeMsgBody(msg, plan_uId);
		
		new SendAsyncTask().execute(msg);
		
		while(asyncTaskState == -1) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
		}
		
		try {
			revMsg = new JSONObject(asyncTaskResult);
			result = revMsg.getInt(MsgInfo.MSGRET_LABEL);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		showToastPopup(result);
		
		return result;
	}

	@Override
	public int getPlan(Context context, PlanData[] plan) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getFriend(Context context, FriendData[] friend) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int addNewFriend(Context context, FriendData friend) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delFriend(Context context, String friendUserId) {
		int result = MsgInfo.STATUS_OK;
		JSONObject msg = new JSONObject();
		JSONObject revMsg = null;
		
		mContext = context;
		
		makeMsgHeader(msg, MsgInfo.DEL_FRIEND);
		
		makeMsgBody(msg, friendUserId);
		
		new SendAsyncTask().execute(msg);
		
		while(asyncTaskState == -1) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
		}
		
		try {
			revMsg = new JSONObject(asyncTaskResult);
			result = revMsg.getInt(MsgInfo.MSGRET_LABEL);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		showToastPopup(result);
		
		return result;
	}

	@Override
	public int setAvataFriend(Context context, String friendUserId) {
		int result = MsgInfo.STATUS_OK;
		JSONObject msg = new JSONObject();
		JSONObject revMsg = null;
		
		mContext = context;
		
		makeMsgHeader(msg, MsgInfo.SEL_AVATA_FRIEND);
		
		makeMsgBody(msg, friendUserId);
		
		new SendAsyncTask().execute(msg);
		
		while(asyncTaskState == -1) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
		}
		
		try {
			revMsg = new JSONObject(asyncTaskResult);
			result = revMsg.getInt(MsgInfo.MSGRET_LABEL);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		showToastPopup(result);
		
		return result;
	}
	
	public class SendAsyncTask extends AsyncTask<JSONObject, Integer, Integer>{
		private ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			asyncTaskState = -1;
			dialog = new ProgressDialog(mContext);
			dialog.setTitle("Networking");
			dialog.setMessage("Please wait while sending");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			dialog.show();
			super.onPreExecute();
		}
		
		@Override
		protected void onProgressUpdate(Integer... progress) {
			
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			dialog.dismiss();
			super.onPostExecute(result);
		}

		@Override
		protected Integer doInBackground(JSONObject... params) {
			ComManager manager  = new ComManager();
			int result = MsgInfo.STATUS_OK;
			
			asyncTaskResult = manager.processMsg(params[0]);
			
			dialog.dismiss();
			
			asyncTaskState = 0;
			
			return result;
		}
	}
}
