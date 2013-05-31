package com.kaist.crescendo.data;

import java.util.ArrayList;
import java.util.Date;

public class PlanData {

	public int uId; /* unique ID , it's composed by year, month, day and incremental number , 2013,05,31,01... but it's limited by Integer.MAX*/
	public int type; 
	public String title;
	public Date start;
	public Date end;
	public ArrayList<HistoryData> history;
	
	public PlanData(int type, String title, Date start, Date end) {
		super();
		this.type = type;
		this.title = title;
		this.start = start;
		this.end = end;
		history = new ArrayList<HistoryData>();
	}
}


