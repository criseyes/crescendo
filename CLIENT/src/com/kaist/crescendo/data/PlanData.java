package com.kaist.crescendo.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PlanData {

	public int uId; /* unique ID, from server */
	public int type; 
	public String title;
	public String start; /* should be YYYY-MM-DD */
	public String end; /* should be YYYY-MM-DD */
	public ArrayList<HistoryData> hItem;
	
	private static final int not_registerd_uId = -1;
	
	public PlanData(int type, String title, String start, String end, int uId) {
		super();
		this.type = type;
		this.title = title;
		this.start = start;
		this.end = end;
		this.uId = uId;
		this.hItem = new ArrayList<HistoryData>();
	}	
	
	public PlanData(int type, String title, String start, String end) {
		super();
		this.type = type;
		this.title = title;
		this.start = start;
		this.end = end;
		this.uId = not_registerd_uId;
		
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
			return 0;
		}
	};
	
	public void setHItem(ArrayList<HistoryData> item)
	{
		this.hItem = item;
	}
}


