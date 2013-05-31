package com.kaist.crescendo.data;

import java.util.ArrayList;
import java.util.Date;

import android.text.format.Time;

public class PlanData {

	public int uId; /* unique ID, from server */
	public int type; 
	public String title;
	public String start; /* should be YYYY-MM-DD */
	public String end; /* should be YYYY-MM-DD */
	public ArrayList<HistoryData> history;
	
	private static final int not_registerd_uId = -1;
	
	public PlanData(int type, String title, String start, String end, int uId) {
		super();
		this.type = type;
		this.title = title;
		this.start = start;
		this.end = end;
		this.uId = uId;
		history = new ArrayList<HistoryData>();
	}	
	
	public PlanData(int type, String title, String start, String end) {
		super();
		this.type = type;
		this.title = title;
		this.start = start;
		this.end = end;
		this.uId = not_registerd_uId;
		
		history = new ArrayList<HistoryData>();
	}
}


