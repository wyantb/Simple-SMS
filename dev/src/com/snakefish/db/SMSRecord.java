package com.snakefish.db;

import android.database.Cursor;

public class SMSRecord {

	public static int INVALID_ID = -2;
	
	private int threadId;
	private String address;
	private int person;
	private long timestamp;
	private String text;
	
	public SMSRecord(Cursor c) {
		threadId = SMSRecordHelper.getThreadId(c);
		
		address = SMSRecordHelper.getAddress(c);

		person = SMSRecordHelper.getPerson(c);
		
		timestamp = SMSRecordHelper.getTime(c);

		text = SMSRecordHelper.getText(c);
	}
	
	public int getThread() {
		return threadId;
	}
	
	public String getAddress() {
		return address;
	}
	
	public int getPerson() {
		return person;
	}
	
	public long getDate() {
		return timestamp;
	}
	
	public String getText() {
		return text;
	}
	
}
