package com.kaist.crescendo.manager;

public class MsgInfo {
	final static int REGISTER_ID = 0x1000;
	
	final static int LOGIN_ID = 0x2000;
	
	final static int REG_PLAN_ID = 0x3000;
	final static int UPDATE_PLAN_ID = 0x3001;
	final static int DEL_PLAN_ID = 0x3002;
	final static int REQ_PLAN_ID = 0x3003;
	
	final static int REG_FRIEND_ID = 0x4000;
	final static int DEL_FRIEND_ID = 0x4001;
		
	final static int MSG_SEND_VALUE = 1;
	final static int MSG_RECEIVE_VALUE = 2;
	
	final static int STATUS_OK = 0;
	final static int STATUS_DUPLICATED_USERID = 0x0001;
	final static int STATUS_UNREGISTERED_USERID = 0x0003;
	final static int STATUS_INVALID_PASSWORD = 0x0005;
	
	final static String MSGID_LABEL = "msgId";
	final static String MSGDIR_LABEL = "msgDir"; // send or receive
	final static String MSGLEN_LABLE = "msgLen";
	final static String MSGRET_LABEL = "msgRet"; // result from sever about previous request
	
	final static String MSGBODY_LABEL = "msgBody";
}
