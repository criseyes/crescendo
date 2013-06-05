package com.kaist.crescendo.manager;

public class MsgInfo {
	//msgId value, this is also command value to server
	final static int REGISTER_USER = 0x1000;	
	
	final static int SYS_LOGIN = 0x2000;
	
	final static int ADD_NEW_PLAN = 0x3000; //add new plan to server
	final static int UPDATE_PLAN = 0x3001; //edit registered plan
	final static int DEL_PLAN = 0x3002; //delete registered plan
	final static int GET_PLAN_CNT = 0x3003; //get the count of the plan data from server
	final static int GET_PLAN = 0x3004; //get plan data from server
	
	final static int GET_FRIEND_CNT = 0x4000; //get the count of the friend list from server
	final static int GET_FRIEND = 0x4001; //get friend data from server
	final static int ADD_FRIEND = 0x4002;
	final static int DEL_FRIEND = 0x4003; //delete friend which selected before
	final static int SEL_AVATA_FRIEND = 0x4004;
	
	//msgDir value
	final static int MSG_SEND_VALUE = 1;
	final static int MSG_RECEIVE_VALUE = 2;
	
	//msgRet value
	final static int STATUS_OK = 0;
	final static int STATUS_DUPLICATED_USERID = 0x0001;
	final static int STATUS_UNREGISTERED_USERID = 0x0002;
	final static int STATUS_INVALID_PASSWORD = 0x0004;
	final static int STATUS_NETWORK_ERROR = 0x0008;
	
	//JSONObject Key Value
	final static String MSGID_LABEL = "msgId";
	final static String MSGUID = "msgUId"; //this is same as userId, when registering new id this field must be null
	final static String MSGDIR_LABEL = "msgDir"; // send or receive
	final static String MSGLEN_LABLE = "msgLen";
	final static String MSGRET_LABEL = "msgRet"; // result from sever about previous request
	
	final static String MSGBODY_LABEL = "msgBody";
	
	final static String USERID_LABEL = "userId";
	final static String PASSWORD_LABEL = "passWord";
	final static String PHONENUM_LABEL = "phoneNum";
	final static String BIRTHDAY_LABEL = "birthDay";
	
	final static String PLAN_UID_LABEL = "planUId";
	final static String PLAN_TYPE_LABEL = "planType";
	final static String PLAN_SDATE_LABEL = "planStartDate";
	final static String PLAN_EDATE_LABEL = "planEndDate";
	final static String PLAN_TITLE_LABEL = "planTitle";
	final static String PLAN_DAYOFWEEK_LABEL = "planDayofWeek";
	
	final static String PLAN_HISTORY_LABEL = "planHistory";
	final static String PLAN_HISDATE_LABEL = "planHisDate";
	final static String PLAN_HISVAL_LABEL = "planHisVal";
	
	final static String FRIEND_IS_AVATA_LABEL = "isAvata";
}
