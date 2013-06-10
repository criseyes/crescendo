package com.kaist.crescendo.alarm;

oneway interface IAlarmServiceCallback {
	void alarmExpired(int planId);
}