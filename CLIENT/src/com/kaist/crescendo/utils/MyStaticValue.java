package com.kaist.crescendo.utils;

public class MyStaticValue {
	
	/* MODE number */
	public static final String MODE = "MODE";
	public static final int MODE_UPDATE = 0;
	public static final int MODE_NEW = 1;
	
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
	
	/* 
	 *  PLAN types, it's started by 10001.
	 */
	public static final int PLANTYPE_DIET = 10001;
	public static final int PLANTYPE_READING_BOOK = 10002;
}
