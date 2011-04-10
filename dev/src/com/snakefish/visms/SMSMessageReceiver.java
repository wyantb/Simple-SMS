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
		
		Intent newMessageIntent = new Intent();
		
		for (String address : messages.keySet()) {
			
			
			long msgId = dbHelper.addMsg(address, person, dateTime, body);
		}
	}
}
