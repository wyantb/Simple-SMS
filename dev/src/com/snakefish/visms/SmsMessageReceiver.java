package com.snakefish.visms;

import com.snakefish.util.ContactNames;

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

			String displayName = ContactNames.get().getDisplayName(context, fromAddress);

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

}
