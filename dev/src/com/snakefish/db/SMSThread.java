package com.snakefish.db;

import android.database.Cursor;

public class SMSThread {

	private int threadId;
	private String displayName;
	
	public SMSThread(Cursor c) {
		int threadIdLoc = c.getColumnIndex(SMSDbAdapter.DISP_KEY_THREAD);
		threadId = c.getInt(threadIdLoc);
		
		int displayNameLoc = c.getColumnIndex(SMSDbAdapter.DISP_KEY_PERSON);
		displayName = c.getString(displayNameLoc);
	}
	
	public int getThreadId() {
		return threadId;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
}
