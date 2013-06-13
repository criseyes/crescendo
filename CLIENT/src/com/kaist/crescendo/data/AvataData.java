package com.kaist.crescendo.data;

import android.graphics.Bitmap;

public class AvataData {
	
	String name;
	int	type;
	int planUId;
	Bitmap img;
	boolean isEnabled;
	
	public AvataData(String name, int type, int planUId, Bitmap img, boolean isEnabled) {
		super();
		this.name = name;
		this.type = type;
		this.planUId = planUId;
		this.img = img;
		this.isEnabled = isEnabled;
	}	
}
