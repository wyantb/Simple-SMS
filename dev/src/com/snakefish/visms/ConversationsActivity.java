package com.snakefish.visms;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;

public class ConversationsActivity extends ListActivity {
	public static final int COMPOSE_ID = Menu.FIRST;
	public static final int SETTINGS_ID = Menu.FIRST + 1;
	public static final Uri SMS_INBOX_URI = Uri.parse("content://sms/inbox");

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.convo_list);
//		getInbox();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, COMPOSE_ID, 0, R.string.compose);
		menu.add(0, SETTINGS_ID, 0, R.string.settings);
		return result;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case COMPOSE_ID:
			// Start activity to compose a new message
			return true;
		case SETTINGS_ID:
			// Start activity to edit options
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}

//	private void getInbox() {
//		String SORT_ORDER = "date DESC";
//		// int count = 0;
//		ContentResolver cr = this.getContentResolver();
//		// Cursor c = cr.query(SMS_INBOX_URI, new String[] { "_id", "thread_id",
//		// "address", "person", "date", "body" }, null, null, SORT_ORDER);
//
//		Cursor c = cr.query(SMS_INBOX_URI, null, null, null, null);
//
//		if (c != null) {
//			try {
//				this.startManagingCursor(c);
//
//				String[] from = new String[] { "person" };
//
//				int[] to = new int[] { R.id.convo_entry };
//
//				SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
//						R.layout.convo_list, c, from, to);
//				this.setListAdapter(adapter);
//
//			} finally {
//				c.close();
//			}
//		}
//
//		// if (c != null) {
//		// try {
//		// count = c.getCount();
//		// if (count > 0) {
//		// c.moveToFirst();
//		//					
//		// for (int i = 0; i < count; i++) {
//		// long messageId = c.getLong(0);
//		// long threadId = c.getLong(1);
//		// String address = c.getString(2);
//		// long contactId = c.getLong(3);
//		// String contactId_string = String.valueOf(contactId);
//		// long timestamp = c.getLong(4);
//		//                        
//		// String body = c.getString(5);
//		//                        
//		// SmsMmsMessage smsMessage = new SmsMmsMessage(
//		// context, address, contactId_string, body, timestamp,
//		// threadId, count, messageId, SmsMmsMessage.MESSAGE_TYPE_SMS);
//		//                        
//		// return smsMessage;
//		//
//		// }
//		// }
//		// } finally {
//		// c.close();
//		// }
//		// }
//
//	}

	/**
	 * Read the PDUs out of an {@link #SMS_RECEIVED_ACTION} or a
	 * {@link #DATA_SMS_RECEIVED_ACTION} intent.
	 * 
	 * @param intent
	 *            the intent to read from
	 * @return an array of SmsMessages for the PDUs
	 */
	public static final SmsMessage[] getMessagesFromIntent(Intent intent) {
		Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
		if (messages == null) {
			return null;
		}
		if (messages.length == 0) {
			return null;
		}

		byte[][] pduObjs = new byte[messages.length][];

		for (int i = 0; i < messages.length; i++) {
			pduObjs[i] = (byte[]) messages[i];
		}
		byte[][] pdus = new byte[pduObjs.length][];
		int pduCount = pdus.length;
		SmsMessage[] msgs = new SmsMessage[pduCount];
		for (int i = 0; i < pduCount; i++) {
			pdus[i] = pduObjs[i];
			msgs[i] = SmsMessage.createFromPdu(pdus[i]);
		}
		return msgs;
	}

}