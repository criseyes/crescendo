package com.kaist.crescendo.data;

public class UserData {
	public String id; // must be e-mail address
	public String password;
	public String phoneNum;
	public String birthDay;
	
	public UserData(String id, String password, String phoneNum, String birthDay) {
		this.id = id;
		this.password = password;
		this.phoneNum = phoneNum;
		this.birthDay = birthDay;
	}
	
	public UserData(String id, String password) {
		this.id = id;
		this.password = password;
	}
}
