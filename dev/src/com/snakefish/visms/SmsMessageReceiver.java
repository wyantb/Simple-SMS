package com.snakefish.visms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsMessageReceiver extends BroadcastReceiver {

	private static final String LOG = "SmsMessageReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle extras = intent.getExtras();

		// If there's no messages...
		if (extras == null) {
			return;
		}

		// Sanity check, what we get better be an Object[]
		if (!(extras.get("pdus") instanceof Object[])) {
			Log.w(LOG, "Received extras not text messages?");
			return;
		}

		Object[] pdus = (Object[]) extras.get("pdus");

		for (int i = 0; i < pdus.length; i++) {
			SmsMessage message = SmsMessage.createFromPdu((byte[]) pdus[i]);
			String fromAddress = message.getOriginatingAddress();
			long timeSent = message.getTimestampMillis();

			String displayName = getDisplayName(context, fromAddress);

			if (i == 0) {
				Intent newMessageIntent = new Intent();
				newMessageIntent.setClassName("com.snakefish.visms", "com.snakefish.visms.IncomingMessage");

				newMessageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

				newMessageIntent.putExtra(IncomingMessage.SMS_FROM_ADDRESS_EXTRA, fromAddress);
				newMessageIntent.putExtra(IncomingMessage.SMS_FROM_DISPLAY_NAME_EXTRA, displayName);
				newMessageIntent.putExtra(IncomingMessage.SMS_TIME_SENT_EXTRA, timeSent);
				newMessageIntent.putExtra(IncomingMessage.SMS_MESSAGE_EXTRA, message.getMessageBody().toString());

				context.startActivity(newMessageIntent);

				break;
			}
			else {
				// TODO handle extra messages
			}
		}
	}

	/**
	 * Talk to contacts db, etc, in order to try to find out who
	 *  sent us this message, instead of just displaying phone number.
	 * @return
	 */
	public String getDisplayName(Context context, String fromAddress) {
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
