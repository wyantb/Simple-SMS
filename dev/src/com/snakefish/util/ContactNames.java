package com.snakefish.util;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

public class ContactNames {

	private static final ContactNames INSTANCE = new ContactNames();
	
	private ContactNames() {
		names = new HashMap<String, String>();
	}
	
	public static ContactNames get() {
		return INSTANCE;
	}
	
	private Map<String, String> names;
	
	public String getDisplayName(Context context, String fromAddress) {
		if (names.containsKey(fromAddress)) {
			return names.get(fromAddress);
		}
		else {
			String dispName = getName(context, fromAddress);
			names.put(fromAddress, dispName);
			return dispName;
		}
	}
	
	/**
	 * Talk to contacts db, etc, in order to try to find out who
	 *  sent us this message, instead of just displaying phone number.
	 * @return
	 */
	private String getName(Context context, String fromAddress) {
		String fromDisplayName = fromAddress;
		
		Uri uri;
		String[] projection;

		// If targeting Donut or below, use
		// Contacts.Phones.CONTENT_FILTER_URL and
		// Contacts.Phones.DISPLAY_NAME
		uri = Uri.withAppendedPath(
				ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(fromAddress));
		projection = new String[] { ContactsContract.PhoneLookup.DISPLAY_NAME };

		// Query the filter URI
		Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				fromDisplayName = cursor.getString(0);
			}

			cursor.close();
		}
		
		return fromDisplayName;
	}
}
