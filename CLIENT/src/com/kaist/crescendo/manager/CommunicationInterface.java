package com.kaist.crescendo.manager;

import org.json.JSONObject;

public interface CommunicationInterface {
	public abstract int write(JSONObject msg);
	public abstract String read();
}
