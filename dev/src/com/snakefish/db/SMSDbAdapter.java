package com.snakefish.db;

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
public class SMSDbAdapter {

	/* The columns found in the built-in sms database */
	public static final String THREAD_KEY_ROWID = "_id";
	public static final String THREAD_KEY_THREADID = "thread_id";
	public static final String THREAD_KEY_ADDRESS = "address";
	public static final String THREAD_KEY_PERSON = "person";
	public static final String THREAD_KEY_DATE = "date";
	public static final String THREAD_KEY_BODY = "body";
	public static final String ORDER_CHRON = "date DESC";
	
	public static final String DISP_KEY_THREAD = "thread_id";
	public static final String DISP_KEY_PERSON = "disp_name";

	private static final String TAG = "SmsDbAdapter";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	/**
	 * Database creation sql statement
	 */
	private static final String THREAD_DATABASE_CREATE =
		"create table inbox " +
			"(_id integer primary key autoincrement," +
			" thread_id integer not null," +
			" address text not null," +
			" person integer not null," +
			" date integer not null," +
			" body text not null);";
	
	private static final String NAME_DATABASE_CREATE =
		"create table display " +
			"(thread_id integer not null," +
			" disp_name text not null, " +
			" foreign key(thread_id) references inbox(thread_id));";

	private static final String DATABASE_NAME = "snakefish_sms";
	private static final String MSG_DATABASE_TABLE = "inbox";
	private static final String DISP_DATABASE_TABLE = "display";
	private static final int DATABASE_VERSION = 2;

	private final Context mCtx;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			db.execSQL(THREAD_DATABASE_CREATE);
			db.execSQL(NAME_DATABASE_CREATE);
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			
			db.execSQL("DROP TABLE IF EXISTS inbox");
			db.execSQL("DROP TABLE IF EXISTS display");
			
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
	public SMSDbAdapter(Context ctx) {
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
	public SMSDbAdapter open() throws SQLException {
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
	public long addMsg(int thread_id, String address, int person,
			long dateTime, String body) {
		Log.v("SmsDbAdapter", "Entering value with threadId = " + thread_id);
		
		ContentValues initialValues = new ContentValues();
		initialValues.put(THREAD_KEY_THREADID, thread_id);
		initialValues.put(THREAD_KEY_ADDRESS, address);
		initialValues.put(THREAD_KEY_PERSON, person);
		initialValues.put(THREAD_KEY_DATE, dateTime);
		initialValues.put(THREAD_KEY_BODY, body);

		return mDb.insert(MSG_DATABASE_TABLE, null, initialValues);
	}
	
	public long addMsg(String address, int person, long dateTime, String body) {
		int threadId = getThreadId(address);
		
		return addMsg(threadId, address, person, dateTime, body);
	}
	
	public long addMsg(SMSRecord record) {
		int threadId = record.getThread();
		
		if (threadId == SMSRecord.INVALID_ID) {
			threadId = getThreadId(record.getAddress());
		}
		
		String address = record.getAddress();
		int person = record.getPerson();
		long time = record.getDate();
		String body = record.getText();
		
		return addMsg(threadId, address, person, time, body);
	}

	/**
	 * Delete the message with the given rowId
	 * 
	 * @param rowId
	 *            id of message to delete
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteMsg(long rowId) {

		return mDb.delete(MSG_DATABASE_TABLE, THREAD_KEY_ROWID + "=" + rowId, null) > 0;
	
	}
	
	public boolean deleteMsg(SMSRecord record) {
		
		return deleteMsg(record.getThread());
		
	}

	/**
	 * Delete an entire thread with the given thread id
	 * 
	 * @param threadId
	 *            the thread_id of the conversation to delete
	 * @return true if deleted, otherwise false
	 */
	public boolean deleteThread(long threadId) {

		return mDb.delete(MSG_DATABASE_TABLE, THREAD_KEY_THREADID + "=" + threadId, null) > 0;
	
	}
	
	public boolean deleteThread(SMSThread thread) {
		
		return deleteThread(thread.getThreadId());
		
	}

	/**
	 * Return a Cursor over the list of all messages in the database
	 * 
	 * @return Cursor over all messages
	 */
	public Cursor fetchAllMsgs() {

		return mDb.query(MSG_DATABASE_TABLE, new String[] { THREAD_KEY_ROWID,
				THREAD_KEY_THREADID, THREAD_KEY_ADDRESS, THREAD_KEY_PERSON, THREAD_KEY_DATE, THREAD_KEY_BODY },
				null, null, null, null, null);
	}

	/**
	 * TODO Fetch all the individual threads in the inbox Presently this just
	 * does the same as fetchAllMsgs()
	 * 
	 * @return A Cursor over all the threads in the database
	 */
	public Cursor fetchAllThreads() {

		Cursor c = mDb.query(MSG_DATABASE_TABLE, new String[] { THREAD_KEY_ROWID,
				THREAD_KEY_THREADID, THREAD_KEY_ADDRESS, THREAD_KEY_PERSON, THREAD_KEY_DATE }, null, null,
				THREAD_KEY_THREADID, "MAX(" + THREAD_KEY_DATE + ")", THREAD_KEY_DATE + " DESC");

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

		Cursor mCursor = mDb.query(true, MSG_DATABASE_TABLE, new String[] {
				THREAD_KEY_ROWID, THREAD_KEY_THREADID, THREAD_KEY_ADDRESS, THREAD_KEY_PERSON, THREAD_KEY_DATE,
				THREAD_KEY_BODY }, THREAD_KEY_ROWID + "=" + rowId, null, null, null, null,
				null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}
	
	public SMSRecord fetchRecord(long rowId) throws SQLException {
		
		return new SMSRecord(fetchMsg(rowId));
		
	}

	/**
	 * Returns a Cursor over a thread when given a threadId
	 * 
	 * @param threadId
	 *            the thread_id of the thread we want to fetch
	 * @return a Cursor over the result set containing all messages in the given
	 *         thread
	 */
	public Cursor fetchThreadByThreadId(int threadId) {
		System.out.println(mDb);
		Cursor c = mDb.query(MSG_DATABASE_TABLE, new String[] { THREAD_KEY_ROWID,
				THREAD_KEY_THREADID, THREAD_KEY_ADDRESS, THREAD_KEY_PERSON, THREAD_KEY_DATE, THREAD_KEY_BODY },
				THREAD_KEY_THREADID + "=" + String.valueOf(threadId), null, null,
				null, ORDER_CHRON);
		return c;
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

	public long getThreadId(long rowId) {
		long threadId = -1;
		mDb.query(MSG_DATABASE_TABLE, new String[] { THREAD_KEY_ROWID, THREAD_KEY_THREADID },
				THREAD_KEY_ROWID + "=" + rowId, null, null, null, null, null);
		return threadId;
	}

	/**
	 * Returns the thread_id of the conversation thread with the specified
	 * addressee
	 * 
	 * @param address
	 *            String representation of the address (phone number) of the
	 *            recipient
	 * @return thread_id of conversation with that person
	 */

	public int getThreadId(String address) {
		Cursor c = mDb.query(MSG_DATABASE_TABLE, new String[] { THREAD_KEY_THREADID },
				THREAD_KEY_ADDRESS + "=" + address, null, null, null, null);

		boolean hasFirst = c.moveToFirst();

		if (hasFirst) {
			return c.getInt(0);
		} else {
			c = mDb.query(MSG_DATABASE_TABLE, new String[] { THREAD_KEY_THREADID }, null,
					null, null, null, THREAD_KEY_THREADID + " desc");
			if (c.getCount() == 0) {
				return 0;
			} else if (c.moveToFirst()) {
				return c.getInt(c.getColumnIndex(THREAD_KEY_THREADID)) + 1;
			} else {
				Log.e(this.toString(),
						"Could not find thread id corresponding to given address: "
								+ address);
				return -1;
			}
		}
	}

	/**
	 * This is a utility method that wipes the entire inbox. Use with caution:
	 * This action cannot be undone.
	 * 
	 * @return the number of rows deleted; zero means unsuccessful
	 */
	public int deleteInbox() {
		
		mDb.delete(DISP_DATABASE_TABLE, null, null);
		return mDb.delete(MSG_DATABASE_TABLE, null, null);

	}
	
	/**
	 * Wipes and recreases the databases.  Use with caution, cannot be
	 *   undone.
	 */
	public void recreateDatabase() {
		
		mDbHelper.onUpgrade(mDb, 0, 1);
		
	}

}
