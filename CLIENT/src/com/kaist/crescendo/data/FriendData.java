package com.kaist.crescendo.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FriendData { 
	public String id;
	public String phoneNumber; /* should be YYYY-MM-DD */
	public PlanData plan;
	boolean isAvata;
	

	public FriendData(String id, String phoneNumber, PlanData plan, boolean isAvata) {
		super();
		this.id = id;
		this.phoneNumber = phoneNumber;
		this.plan = plan;
		this.isAvata = isAvata;
	}
	
}