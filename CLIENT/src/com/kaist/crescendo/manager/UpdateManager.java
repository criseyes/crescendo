package com.kaist.crescendo.manager;

import org.json.JSONException;
import org.json.JSONObject;
import android.app.ProgressDialog;
import com.kaist.crescendo.data.PlanData;
import com.kaist.crescendo.data.UserData;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class UpdateManager implements UpdateManagerInterface {
	private int asyncTaskResult;
	private int asyncTaskState;
	private Context mContext;
	
	private void showToastPopup(int result) {
		switch(result) {
		case MsgInfo.STATUS_OK:
			Toast.makeText(mContext, "Success!!!", Toast.LENGTH_LONG).show();
			break;
		case MsgInfo.STATUS_DUPLICATED_USERID:
			Toast.makeText(mContext, "your id is duplicated, please register using another id", Toast.LENGTH_LONG).show();
			break;
		case MsgInfo.STATUS_INVALID_PASSWORD:
			Toast.makeText(mContext, "Please Input valid passowrd!", Toast.LENGTH_LONG).show();
			break;
		case MsgInfo.STATUS_UNREGISTERED_USERID:
			Toast.makeText(mContext, "Unregisted id, Please register your id!", Toast.LENGTH_LONG).show();
			break;
		default:
				break;
		}
	}
	
	private void makeMsgHeader(JSONObject msg, int msgId) {
		try {
			msg.put(MsgInfo.MSGID_LABEL, msgId);
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
		
		JSONObject temp_body = new JSONObject();
		
		try {
			msgId = msg.getInt(MsgInfo.MSGID_LABEL);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		switch(msgId) {
		case MsgInfo.REGISTER_ID:
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
		case MsgInfo.LOGIN_ID:
			uData = (UserData)body;
			try {
				temp_body.put(MsgInfo.USERID_LABEL, uData.id);
				temp_body.put(MsgInfo.PASSWORD_LABEL, uData.password);
				
				msg.put(MsgInfo.MSGBODY_LABEL, temp_body);
			} catch (Exception e) {
				// TODO: handle exception
			}
			break;
		default:
			break;
		}
		
		
	}
	
	public int register(Context context, UserData uData) {
		int result = MsgInfo.STATUS_OK;
		JSONObject msg = new JSONObject();
		
		mContext = context;
		
		makeMsgHeader(msg, MsgInfo.REGISTER_ID);
		
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
		result = asyncTaskResult;
		
		showToastPopup(result);
		
		return result;
	}
	
	public int login(Context context, UserData uData) {
		int result = MsgInfo.STATUS_OK;
		JSONObject msg = new JSONObject();
		
		mContext = context;
		
		makeMsgHeader(msg, MsgInfo.LOGIN_ID);
		
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
		result = asyncTaskResult;
		
		showToastPopup(result);
		
		return result;
	}
	
	@Override
	public int addNewPlan(PlanData plan) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public class SendAsyncTask extends AsyncTask<JSONObject, Integer, Integer>{
		private ProgressDialog dialog;
		private int msgId;
		private JSONObject jsonObject;
		
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
			int result = MsgInfo.STATUS_OK;
			String userId = null;
			String passWord = null;
			
			try {
				msgId = params[0].getInt(MsgInfo.MSGID_LABEL);
				jsonObject = params[0];
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// test code started here
			if(msgId == MsgInfo.REGISTER_ID) {
				FileEmulator fileEmul = new FileEmulator();
				boolean existFlag = false;
				JSONObject uData = null;
				
				try {
					uData = (JSONObject) jsonObject.get(MsgInfo.MSGBODY_LABEL);
					userId = uData.getString(MsgInfo.USERID_LABEL);
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				existFlag = fileEmul.checkFileExist(userId);
				if(existFlag) {
					result = MsgInfo.STATUS_DUPLICATED_USERID;
				} else {
					fileEmul.parseJONtoString(userId, jsonObject);
				}
				
			} else if(msgId == MsgInfo.LOGIN_ID) {
				FileEmulator fileEmul = new FileEmulator();
				boolean existFlag = false;
				JSONObject uData = null;
				JSONObject RevData;
				JSONObject revJsonObj = null;
				
				try {
					uData = (JSONObject) jsonObject.get(MsgInfo.MSGBODY_LABEL);
					userId = uData.getString(MsgInfo.USERID_LABEL);
					passWord = uData.getString(MsgInfo.PASSWORD_LABEL);
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				existFlag = fileEmul.checkFileExist(userId);
				if(existFlag) {
					String jsonString = "";
					
					jsonString = fileEmul.readJSON(userId);
					
					try {
						revJsonObj = new JSONObject(jsonString);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					try {
						RevData = (JSONObject) revJsonObj.get(MsgInfo.MSGBODY_LABEL);
						if(passWord.equals(RevData.get(MsgInfo.PASSWORD_LABEL)) == false) {
							result = MsgInfo.STATUS_INVALID_PASSWORD;
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				} else {
					result = MsgInfo.STATUS_UNREGISTERED_USERID;
				}
			}
			// test code ended here
			
			dialog.dismiss();
			
			asyncTaskState = 0;
			asyncTaskResult = result;
			
			//return result from here to postExecute()
			return result;
		}
	}
}
