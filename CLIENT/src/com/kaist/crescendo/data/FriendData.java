package com.kaist.crescendo.data;

public class FriendData { 
	public String id;
	public String phoneNumber; /* should be YYYY-MM-DD */
	public PlanData plan;
	boolean isSelected;
	boolean isAvata;
	

	public FriendData(String id, String phoneNumber, PlanData plan, boolean isAvata) {
		super();
		this.id = id;
		this.phoneNumber = phoneNumber;
		this.plan = plan;
		this.isAvata = isAvata;
	}
	
}