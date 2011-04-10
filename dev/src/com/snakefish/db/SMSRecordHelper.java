package com.snakefish.db;

import android.database.Cursor;

public class SMSRecordHelper {
	
	public static int getThreadId(Cursor c) {
		int threadIdLoc = c.getColumnIndex(SMSDbAdapter.THREAD_KEY_THREADID);
		return c.getInt(threadIdLoc);
	}
	
	public static String getAddress(Cursor c) {
		int addressLoc = c.getColumnIndex(SMSDbAdapter.THREAD_KEY_ADDRESS);
		return c.getString(addressLoc);
	}
	
	public static int getPerson(Cursor c) {
		int personLoc = c.getColumnIndex(SMSDbAdapter.THREAD_KEY_PERSON);
		return c.getInt(personLoc);
	}
	
	public static long getTime(Cursor c) {
		int timestampLoc = c.getColumnIndex(SMSDbAdapter.THREAD_KEY_DATE);
		return c.getLong(timestampLoc);
	}
	
	public static String getText(Cursor c) {
		int textLoc = c.getColumnIndex(SMSDbAdapter.THREAD_KEY_BODY);
		return c.getString(textLoc);
	}

}
