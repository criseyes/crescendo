package com.kaist.crescendo.manager;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class UpdateManager implements UpdateManagerInterface {
	private int asyncTaskResult;
	private int asyncTaskState;
	private Context mContext;
	
	private void showToastPopup(int result) {
		switch(result) {
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
			msg.put(MsgInfo.STATUS_LABEL, MsgInfo.STATUS_OK);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public int register(Context context, String id, String pw, String phone, String birth) {
		int result = MsgInfo.STATUS_OK;
		JSONObject msg = new JSONObject();
		
		mContext = context;
		
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
	
	public int login(Context context, String id, String pw) {
		int result = MsgInfo.STATUS_OK;
		JSONObject msg = new JSONObject();
		
		mContext = context;
		
		makeMsgHeader(msg, MsgInfo.LOGIN_ID);
				
		try {
			msg.put(MsgInfo.USERID_LABEL, id);
			msg.put(MsgInfo.PASSWORD_LABEL, pw);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
	
	public class SendAsyncTask extends AsyncTask<JSONObject, Integer, Integer>{
		private ProgressDialog dialog;
		private int msgId;
		private String userId;
		private String passWord;
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
			
			try {
				msgId = params[0].getInt(MsgInfo.MSGID_LABEL);
				userId = params[0].getString(MsgInfo.USERID_LABEL);
				passWord = params[0].getString(MsgInfo.PASSWORD_LABEL);
				jsonObject = params[0];
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// test code started here
			if(msgId == MsgInfo.REGISTER_ID) {
				FileEmulator fileEmul = new FileEmulator();
				boolean existFlag = false;
				
				existFlag = fileEmul.checkFileExist(userId);
				if(existFlag) {
					result = MsgInfo.STATUS_DUPLICATED_USERID;
				} else {
					fileEmul.parseJONtoString(jsonObject);
				}
				
			} else if(msgId == MsgInfo.LOGIN_ID) {
				FileEmulator fileEmul = new FileEmulator();
				boolean existFlag = false;
				
				existFlag = fileEmul.checkFileExist(userId);
				if(existFlag) {
					String jsonString = "";
					
					jsonString = fileEmul.readJSON(userId);
					
					try {
						jsonObject = new JSONObject(jsonString);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					try {
						if(passWord.equals(jsonObject.getString(MsgInfo.PASSWORD_LABEL)) == false) {
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
