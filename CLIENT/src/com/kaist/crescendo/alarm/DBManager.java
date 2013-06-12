package com.kaist.crescendo.alarm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager extends SQLiteOpenHelper {
	public final static String DB_NAME = "alarmDb";

	public DBManager(Context context, String name, CursorFactory factory, int version) {
		super(context, DB_NAME, null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE " + DB_NAME 
				+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " 
				+ "planId TEXT, dayOfWeek INTEGER, alarmTime TEXT);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
}
