package com.snakefish.visms;

import java.util.HashMap;
import java.util.Map;

import com.snakefish.db.SMSDbAdapter;
import com.snakefish.util.ContactNames;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSMessageReceiver extends BroadcastReceiver {

	private static final String LOG = "SmsMessageReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		SMSDbAdapter dbHelper = new SMSDbAdapter(context).open();
		
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
		Log.v(LOG, "Received actual messages.");

		String addressTo = null;
		Map<String, String> messages = new HashMap<String, String>();
		Map<String, Long> times = new HashMap<String, Long>();
		
		// Obtain info from all incoming pdus and aggregate
		for (int i = 0; i < pdus.length; i++) {
			SmsMessage message = SmsMessage.createFromPdu((byte[]) pdus[i]);
			String messageBody = message.getMessageBody().toString();
			String fromAddress = message.getOriginatingAddress();
			long timeSent = message.getTimestampMillis();

			times.put(fromAddress, timeSent);
			
			String oldMsg = messages.put(fromAddress, messageBody);
			if (oldMsg != null) {
				messages.put(fromAddress, oldMsg + messageBody);
			}
			
			if (i == 0) {
				addressTo = fromAddress;
			}
		}
		
		// Prepare intent
		Intent newMessageIntent = new Intent();
		newMessageIntent.setClassName("com.snakefish.visms", "com.snakefish.visms.IncomingMessage");
		newMessageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		
		// Insert messages into DB, and store the useful msgId to pass to IncomingMessage
		for (String address : messages.keySet()) {
			String message = messages.get(address);
			long dateTime = times.get(address);
			
			int person = ContactNames.get().getPerson(context, address);
			
			long msgId = dbHelper.addMsg(address, person, dateTime, message);
			
			if (address == addressTo) {
				newMessageIntent.putExtra(IncomingMessage.SMS_MSG_ID, msgId);
			}
		}
		
		context.startActivity(newMessageIntent);
	}
}
