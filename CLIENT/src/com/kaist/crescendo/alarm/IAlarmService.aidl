package com.kaist.crescendo.alarm;

import com.kaist.crescendo.alarm.IAlarmServiceCallback;

interface IAlarmService {
	int AddAlarm(int planId, int dayOfWeek, String alarmTime);
	int DelAlarm(int planId);
	boolean registerCallback(IAlarmServiceCallback callback);
	boolean unreigsterCallback(IAlarmServiceCallback callback);
}