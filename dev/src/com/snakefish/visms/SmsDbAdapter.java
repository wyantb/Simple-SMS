/*
 * Copyright (C) 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.snakefish.visms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Simple sms inbox database access helper class. Defines the basic CRUD
 * operations based on the Google notepad example, and gives the ability to list
 * all inbox messages as well as retrieve a specific message.
 * 
 * This database is built to mirror the built-in sms database and act as a
 * replacement for Snakefish SMS, as we're having difficulty interacting with
 * the built-in database.
 */
public class SmsDbAdapter {

	/* The columns found in the built-in sms database */
	public static final String KEY_ROWID = "_id";
	public static final String KEY_THREADID = "thread_id";
	public static final String KEY_ADDRESS = "address";
	public static final String KEY_PERSON = "person";
	public static final String KEY_DATE = "date";
	public static final String KEY_BODY = "body";
	public static final String ORDER_CHRON = "date DESC";

	private static final String TAG = "SmsDbAdapter";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	/**
	 * Database creation sql statement
	 */
	private static final String DATABASE_CREATE = "create table inbox (_id integer primary key autoincrement, "
			+ "thread_id integer not null, address text not null, person integer not null, "
			+ "date integer not null, body text not null);";

	private static final String DATABASE_NAME = "snakefish_sms";
	private static final String DATABASE_TABLE = "inbox";
	private static final int DATABASE_VERSION = 2;

	private final Context mCtx;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS notes");
			onCreate(db);
		}
	}

	/**
	 * Constructor - takes the context to allow the database to be
	 * opened/created
	 * 
	 * @param ctx
	 *            the Context within which to work
	 */
	public SmsDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	/**
	 * Open the sms database. If it cannot be opened, try to create a new
	 * instance of the database. If it cannot be created, throw an exception to
	 * signal the failure
	 * 
	 * @return this (self reference, allowing this to be chained in an
	 *         initialization call)
	 * @throws SQLException
	 *             if the database could be neither opened or created
	 */
	public SmsDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	/**
	 * Create a new sms in the database using the information provided. If the
	 * message is successfully created return the new rowId for that note,
	 * otherwise return a -1 to indicate failure.
	 * 
	 * @param thread_id
	 *            the id of the thread the message is in
	 * @param address
	 *            the phone number of the person sent/receiving the message
	 * @param person
	 *            the integer representing the contact
	 * @param date
	 *            the timestamp on the message
	 * @param body
	 *            the body of the message
	 * @return rowId or -1 if failed
	 */
	public long addMsg(int thread_id, String address, int person, int date,
			String body) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_THREADID, thread_id);
		initialValues.put(KEY_ADDRESS, address);
		initialValues.put(KEY_PERSON, person);
		initialValues.put(KEY_DATE, date);
		initialValues.put(KEY_BODY, body);

		return mDb.insert(DATABASE_TABLE, null, initialValues);
	}

	/**
	 * Delete the message with the given rowId
	 * 
	 * @param rowId
	 *            id of message to delete
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteMsg(long rowId) {

		return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	/**
	 * Find the id of the thread to which a particular message belongs, given
	 * the message id.
	 * 
	 * @param rowId
	 *            the id of the message in question
	 * @return the id of the thread to which that message belongs; -1 means the
	 *         thread could not be found
	 */

	public long findThreadId(long rowId) {
		long threadId = -1;
		mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_THREADID },
				KEY_ROWID + "=" + rowId, null, null, null, null, null);
		return threadId;
	}

	/**
	 * Return a Cursor over the list of all messages in the database
	 * 
	 * @return Cursor over all messages
	 */
	public Cursor fetchAllMsgs() {

		return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID,
				KEY_THREADID, KEY_ADDRESS, KEY_PERSON, KEY_DATE, KEY_BODY },
				null, null, null, null, null);
	}

	/**
	 * TODO Fetch all the individual threads in the inbox Presently this just
	 * does the same as fetchAllMsgs()
	 * 
	 * @return A Cursor over all the threads in the database
	 */
	public Cursor fetchAllThreads() {
		// Cursor c = mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID,
		// KEY_THREADID}, null, null, null,
		// null, null);

		Cursor c = mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID,
				KEY_THREADID, KEY_ADDRESS, KEY_PERSON, KEY_DATE }, null, null,
				KEY_THREADID, "MAX(" + KEY_DATE + ")", KEY_DATE + " DESC");

		return c;
	}

	/**
	 * Return a Cursor positioned at the message that matches the given rowId
	 * 
	 * @param rowId
	 *            id of message to retrieve
	 * @return Cursor positioned to matching message, if found
	 * @throws SQLException
	 *             if message could not be found/retrieved
	 */
	public Cursor fetchMsg(long rowId) throws SQLException {

		Cursor mCursor = mDb.query(true, DATABASE_TABLE, new String[] {
				KEY_ROWID, KEY_THREADID, KEY_ADDRESS, KEY_PERSON, KEY_DATE,
				KEY_BODY }, KEY_ROWID + "=" + rowId, null, null, null, null,
				null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	/**
	 * Returns a Cursor over a thread when given a threadId
	 * 
	 * @param threadId the thread_id of the thread we want to fetch
	 * @return	a Cursor over the result set containing all messages in the given thread
	 */
	public Cursor fetchThreadByThreadId(int threadId) {
		Cursor c = mDb.query(DATABASE_TABLE, 
				new String[] {KEY_ROWID, KEY_THREADID, KEY_ADDRESS, KEY_PERSON, KEY_DATE, KEY_BODY}, 
				KEY_THREADID + "=" + String.valueOf(threadId), null, null, null, 
				ORDER_CHRON);
		return c;
	}

	/**
	 * This is a utility method that wipes the entire inbox. Use with caution:
	 * This action cannot be undone.
	 * 
	 * @return the number of rows deleted; zero means unsuccessful
	 */
	public int deleteInbox() {
		return mDb.delete(DATABASE_TABLE, null, null);

	}

}
