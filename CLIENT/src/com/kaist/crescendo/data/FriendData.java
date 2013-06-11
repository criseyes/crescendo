package com.kaist.crescendo.data;

public class FriendData { 
	public String id;
	public String phoneNumber; /* should be YYYY-MM-DD */
	public PlanData plan;
	boolean isSelected;
	boolean isAvata;

	public FriendData(String id, String phoneNumber, PlanData plan, boolean isSelected, boolean isAvata) {
		super();
		this.id = id;
		this.phoneNumber = phoneNumber;
		this.plan = plan;
		this.isSelected = isSelected;
		this.isAvata = isAvata;
	}
	
	public boolean getisSelected() {
		return isSelected;
	}
	
	public boolean setselected(boolean value) {
		return isSelected = value;
	}
	
}