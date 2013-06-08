package com.kaist.crescendo.utils;

public class MyStaticValue {
	
	/* MODE number */
	public static final String MODE = "MODE";
	public static final String NUMBER = "NUMBER";
	
	public static final int MODE_UPDATE = 0;
	public static final int MODE_NEW = 1;
	
	/* Avata */
	public static final String AVATA_FILNENAME = "myAvata.png";
	public static final int AVATA_WIDTH = 90;
	public static final int AVATA_HIGHT = 120;
	
	/* Day of Week */
	public static final int MONDAY = 	0x1;
	public static final int TUESDAY = 	0x10;
	public static final int WEDNESDAY = 0x100;
	public static final int THURSDAY = 	0x1000;
	public static final int FRIDAY = 	0x10000;
	public static final int SATURDAY = 	0x100000;
	public static final int SUNDAY = 	0x1000000;
	
	/* REQ codes, between activities */
	public static final int REQUESTCODE_REGISTER = 1000;
	public static final int REQUESTCODE_ADDNEWPLAN = 1001;
	public static final int REQUESTCODE_UPDATEPLAN = 1002;
	public static final int REQUESTCODE_ADDNEWFRIEND = 1003;
	
	public static final int REQUESTCODE_GETAVATAIMAGE = 1050;
	
	/* String value, between activities */
	
	public static final String RESULT_REGISTER = "ResultToRegister";
	public static final String RESULT_ID = "NewID";
	public static final String RESULT_PW = "NewPW";
	public static final String RESULT_PHONE = "PhoneNo";
	public static String myId = "";
	
	/* 
	 *  PLAN types, it's started by 10001.
	 */
	public static final int PLANTYPE_DIET = 10001;
	public static final int PLANTYPE_READING_BOOK = 10002;

}
