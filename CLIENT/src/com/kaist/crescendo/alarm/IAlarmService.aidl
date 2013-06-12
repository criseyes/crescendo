package com.kaist.crescendo.alarm;

import com.kaist.crescendo.alarm.IAlarmServiceCallback;

interface IAlarmService {
	int addAlarm(int planId, int dayOfWeek, String alarmTime);
	int updateAlarm(int planId, int dayOfWeek, String alarmTime);
	int delAlarm(int planId);
	int resetAlarm();
	boolean registerCallback(IAlarmServiceCallback callback);
	boolean unreigsterCallback(IAlarmServiceCallback callback);
}