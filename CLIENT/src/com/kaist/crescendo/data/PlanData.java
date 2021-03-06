package com.kaist.crescendo.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PlanData {

	public int uId; /* unique ID, from server */
	public int type; 
	public int dayOfWeek;
	public double targetValue;
	public double initValue;
	public boolean isSelected = false;
	public String title;
	public String start; /* should be YYYY-MM-DD */
	public String end; /* should be YYYY-MM-DD */
	public String alarm; /* should be HH:MM (HH in 24hours format) */
	public ArrayList<HistoryData> hItem;
	
	private static final int not_registerd_uId = -1;
	
	public PlanData(int type, String title, String start, String end, String alarm, int dayOfWeek, float initV, float targetV, int uId) {
		super();
		this.type = type;
		this.title = title;
		this.start = start;
		this.end = end;
		this.uId = uId;
		this.dayOfWeek = dayOfWeek;
		this.initValue = initV;
		this.targetValue = targetV;
		this.alarm = alarm;
		this.hItem = new ArrayList<HistoryData>();
	}	
	
	public PlanData(int type, String title, String start, String end, String alarm, int dayOfWeek, double initV, double targetV) {
		super();
		this.type = type;
		this.title = title;
		this.start = start;
		this.end = end;
		this.uId = not_registerd_uId;
		this.dayOfWeek = dayOfWeek;
		this.initValue = initV;
		this.alarm = alarm;
		this.targetValue = targetV;
		
		this.hItem = new ArrayList<HistoryData>();
	}
	
	public void addHistory(HistoryData history)
	{
		this.hItem.add(history);
		Collections.sort(hItem, comparator);
	}
	
	private final static Comparator<HistoryData> comparator = new Comparator<HistoryData>() {

		@Override
		public int compare(HistoryData lhs, HistoryData rhs) {
			// TODO Auto-generated method stub
		    int left = Integer.parseInt(lhs.date.toString().replace("-", ""));
		    int right = Integer.parseInt(rhs.date.toString().replace("-", ""));
		    
		    if(left < right)
		        return 1;
		    if(right > right)
		        return -1;
			return 0;
		}
	};
	
	public void setHItem(ArrayList<HistoryData> item)
	{
		this.hItem = item;
	}
}


