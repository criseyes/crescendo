package com.kaist.crescendo.manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Environment;
import android.util.Log;

public class FileEmulator implements CommunicationInterface{
	
	private int prevResult;
	private int msgId;
	private String userId;
	private String passWord;
	private String jsonString;
	private JSONObject oriMsg;
	
	static private JSONArray mFriendJSONArray;
	static private JSONArray mPlanJSONArray;
	static int planUid;
	
	public FileEmulator() {
		
		if(mFriendJSONArray == null) {
			// TODO Auto-generated constructor stub
			mFriendJSONArray = new JSONArray();
			mPlanJSONArray = new JSONArray(); 
			
			planUid = 0;			
		}
	}
	
	private boolean checkFileExist(String fileName){
		boolean result = false;
		String path = Environment.getExternalStorageDirectory().getAbsolutePath();
		
		File file = new File(path+"/"+fileName);
		
		if(file.exists()) {
			result = true;
		}
		
		return result;
	}
	
	private void parseJONtoString(String uId, JSONObject jObject) {
		String jsonString = "";
		String fileName;
		jsonString = jObject.toString();
		
		try {
			String path = Environment.getExternalStorageDirectory().getAbsolutePath();
			fileName = uId;
			File jsonFile = new File(path+"/"+fileName);
			jsonFile.createNewFile();
			FileOutputStream fOut = new FileOutputStream(jsonFile);
			OutputStreamWriter OutWriter = new OutputStreamWriter(fOut);
			
			OutWriter.append(jsonString);
			OutWriter.close();
			fOut.close();
		} catch (Exception e) {
			// TODO: handle exception
			Log.v("FileErr","File error" + e.toString());
		}
	}
	
	private String readJSON(String fileName) {
		String buffer = "";
		String jsonString = "";
		
		try {
			String path = Environment.getExternalStorageDirectory().getAbsolutePath();
			File file = new File(path+"/"+fileName);
			FileInputStream fIn = new FileInputStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(fIn));
			
			String dataRow = "";
			
			while((dataRow = reader.readLine()) != null) {
				buffer += dataRow + "\n";
			}
			reader.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		jsonString = buffer;
		return jsonString;
	}

	@Override
	public int write(JSONObject msg) {
		// TODO Auto-generated method stub
		int result = MsgInfo.STATUS_OK;
		prevResult = MsgInfo.STATUS_OK;
		boolean existFlag = false;
		JSONObject uData = null; 
		
		oriMsg = msg;
		
		try {
			msgId = msg.getInt(MsgInfo.MSGID_LABEL);
			uData = (JSONObject) msg.get(MsgInfo.MSGBODY_LABEL);
			userId = uData.getString(MsgInfo.USERID_LABEL);
			passWord = uData.getString(MsgInfo.PASSWORD_LABEL);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		if(msgId == MsgInfo.REGISTER_USER) {			
			existFlag = checkFileExist(userId);
			if(existFlag) {
				prevResult = MsgInfo.STATUS_DUPLICATED_USERID;
			} else {
				parseJONtoString(userId, msg);
			}			
		}
		
		return result;
	}

	@Override
	public String read() {
		// TODO Auto-generated method stub
		int result = MsgInfo.STATUS_OK;
		
		if(msgId == MsgInfo.REGISTER_USER) {
			try {
				oriMsg.put(MsgInfo.MSGRET_LABEL, prevResult);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			jsonString = oriMsg.toString();
		} else if(msgId == MsgInfo.SYS_LOGIN) {	
			
			boolean existFlag = false;
			JSONObject RevData = null;
			JSONObject revJsonObj = null;
			
			existFlag = checkFileExist(userId);
			if(existFlag) {
				String jsonString = "";
				
				jsonString = readJSON(userId);
				
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
			
			try {
				RevData.put(MsgInfo.MSGRET_LABEL, result);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			jsonString = RevData.toString();			
		} else if(msgId == MsgInfo.ADD_NEW_PLAN) {
			try {
				oriMsg.put(MsgInfo.MSGRET_LABEL, MsgInfo.STATUS_OK);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			JSONObject obj = null;
			try {
				obj = oriMsg.getJSONObject(MsgInfo.MSGBODY_LABEL);
				obj.put(MsgInfo.PLAN_UID_LABEL, planUid++);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			mPlanJSONArray.put(obj);
			
			jsonString = oriMsg.toString();
			
		} else if(msgId == MsgInfo.UPDATE_PLAN) {
			for(int i = 0; i < mPlanJSONArray.length(); i++) {
				int planId = -1;
				int compareId = -2;
				JSONObject obj = null;
				try {
					planId = oriMsg.getJSONObject(MsgInfo.MSGBODY_LABEL).getInt(MsgInfo.PLAN_UID_LABEL);
					obj = mPlanJSONArray.getJSONObject(i);
					compareId = obj.getInt(MsgInfo.PLAN_UID_LABEL);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(planId == compareId) {
					try {
						JSONObject msgBody = oriMsg.getJSONObject(MsgInfo.MSGBODY_LABEL);
						mPlanJSONArray.put(i, msgBody);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}			
			
			try {
				oriMsg.put(MsgInfo.MSGRET_LABEL, MsgInfo.STATUS_OK);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			jsonString = oriMsg.toString();
			
		} else if(msgId == MsgInfo.DEL_PLAN) {
			for(int i = 0; i < mPlanJSONArray.length(); i++) {
				int planId = -1;
				int compareId = -2;
				JSONObject obj = null;
				try {
					planId = oriMsg.getJSONObject(MsgInfo.MSGBODY_LABEL).getInt(MsgInfo.PLAN_UID_LABEL);
					obj = mPlanJSONArray.getJSONObject(i);
					compareId = obj.getInt(MsgInfo.PLAN_UID_LABEL);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(planId == compareId) {
					try {
						mPlanJSONArray.put(i, null);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}			
			
			try {
				oriMsg.put(MsgInfo.MSGRET_LABEL, MsgInfo.STATUS_OK);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			jsonString = oriMsg.toString();
			
			
		} else if(msgId == MsgInfo.GET_PLAN) {
			
			try {
				oriMsg.put(MsgInfo.MSGBODY_LABEL, mPlanJSONArray);
				oriMsg.put(MsgInfo.MSGRET_LABEL, MsgInfo.STATUS_OK);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			jsonString = oriMsg.toString();
			
		} else if(msgId == MsgInfo.ADD_FRIEND) {
			
		} else if(msgId == MsgInfo.DEL_FRIEND) {
			
		} else if(msgId == MsgInfo.CHANGE_PASSWORD) {
			
		}
		
		return jsonString;
	}
}
