package com.snakefish.db;

import android.database.Cursor;

public class SMSRecord {

	public static int INVALID_ID = -2;
	
	private int threadId;
	private String address;
	
	/** I think this field is useless TODO */
	private int person;
	
	private long timestamp;
	private String text;
	
	public SMSRecord(Cursor c) {
		int threadIdLoc = c.getColumnIndex(SMSDbAdapter.THREAD_KEY_THREADID);
		threadId = c.getInt(threadIdLoc);
		
		int addressLoc = c.getColumnIndex(SMSDbAdapter.THREAD_KEY_ADDRESS);
		address = c.getString(addressLoc);

		int personLoc = c.getColumnIndex(SMSDbAdapter.THREAD_KEY_PERSON);
		person = c.getInt(personLoc);
		
		int timestampLoc = c.getColumnIndex(SMSDbAdapter.THREAD_KEY_DATE);
		timestamp = c.getLong(timestampLoc);

		int textLoc = c.getColumnIndex(SMSDbAdapter.THREAD_KEY_BODY);
		text = c.getString(textLoc);
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
