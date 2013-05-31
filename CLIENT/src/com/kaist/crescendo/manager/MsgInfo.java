package com.kaist.crescendo.manager;

public class MsgInfo {
	final static int REGISTER_ID = 0x1000;
	final static int LOGIN_ID = 0x2000;
	
	final static int MSG_SEND_VALUE = 1;
	final static int MSG_RECEIVE_VALUE = 2;
	
	final static int STATUS_OK = 0;
	
	final static String MSGID_LABEL = "msgId";
	final static String MSGDIR_LABEL = "msgDir"; // send or receive
	final static String MSGLEN_LABLE = "msgLen";
	
	final static String USERID_LABEL = "userId";
	final static String PASSWORD_LABEL = "passWord";
	final static String PHONENUM_LABLE = "phoneNum";
	final static String BIRTHDAY_LABEL = "birthDay";
}
