package com.snakefish.util;

import java.util.HashMap;
import java.util.Map;

import com.snakefish.visms.ContactInfo;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

public class ContactNames {

	private static final ContactNames INSTANCE = new ContactNames();
	
	private ContactNames() {
		names = new HashMap<String, ContactInfo>();
	}
	
	public static ContactNames get() {
		return INSTANCE;
	}
	
	private Map<String, ContactInfo> names;
	
	public String getDisplayName(Context context, String fromAddress) {
		if (names.containsKey(fromAddress)) {
			return names.get(fromAddress).getDisplayName();
		}
		else {
			ContactInfo contact = getContact(context, fromAddress);
			names.put(fromAddress, contact);
			return contact.getDisplayName();
		}
	}
	
	public int getPerson(Context context, String fromAddress) {
		if (names.containsKey(fromAddress)) {
			return names.get(fromAddress).getId();
		}
		else {
			ContactInfo contact = getContact(context, fromAddress);
			names.put(fromAddress, contact);
			return contact.getId();
		}
	}
	
	/**
	 * Talk to contacts db, etc, in order to try to find out who
	 *  sent us this message, instead of just displaying phone number.
	 * @return
	 */
	private ContactInfo getContact(Context context, String fromAddress) {
		ContactInfo contact = new ContactInfo();
		
		Uri uri;
		String[] projection;

		uri = Uri.withAppendedPath(
				ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(fromAddress));
		projection = new String[] { ContactsContract.PhoneLookup.DISPLAY_NAME,
				ContactsContract.PhoneLookup._ID};

		// Query the filter URI
		Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				contact.setDisplayName(cursor.getString(0));
				contact.setId(cursor.getInt(1));
			}

			cursor.close();
		}
		
		return contact;
	}
}
