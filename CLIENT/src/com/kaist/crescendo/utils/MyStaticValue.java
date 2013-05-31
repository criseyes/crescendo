package com.kaist.crescendo.utils;

public class MyStaticValue {
	
	/* MODE number */
	public static final String MODE = "MODE";
	public static final int MODE_UPDATE = 0;
	public static final int MODE_NEW = 1;
	
	/* REQ codes, between activities */
	public static final int REQUESTCODE_REGISTER = 1000;
	public static final int REQUESTCODE_ADDNEWPLAN = 1001;
	
	/* 
	 *  PLAN types, it's started by 10001.
	 */
	public static final int PLANTYPE_DIET = 10001;
	public static final int PLANTYPE_READING_BOOK = 10002;
}
