package com.snakefish.visms;

import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.snakefish.db.SMSDbAdapter;
import com.snakefish.db.SMSRecord;
import com.snakefish.feedback.CommandAction;
import com.snakefish.feedback.SpeechType;
import com.snakefish.feedback.VoiceCommand;
import com.snakefish.util.ContactNames;

/**
 * Represents the view with which users will send messages in a conversation.
 * 
 * Upon sending the message, this screen will finish() and return to the previous screen, if any.
 * 
 * Further, please make use of the CONVERSATION_LAST_MSG String, if you want to populate the
 * 	upper text box of this screen with previous conversation history.
 * NOTE: make it ArrayList<String> data, with 2 elements.  First element should be the from,
 *  second element should be the message.  (ie "derek" "hey what's up?")
 *
 */
public class TextActivity extends SMSActivity {
	
	public static final int SETTINGS_ID = Menu.FIRST;
	private static final String ACTION_SMS_SENT = "com.snakefish.SMS_SENT_ACTION";
	private static final String SMS_ERROR = "TextActivity";
	
	public static final String THREAD_ID = "com.snakefish.THREAD_ID";
	public static final String THREAD_ADDRESS = "com.snakefish.ADDRESS_TO";
	
	/** The initial text to send, if any. */
	public static final String INITIAL_TEXT = "com.snakefish.INITIAL_TEXT";

	public TextActivity() {
		super(R.xml.text_speech);
	}

	private TextView historyDisplay = null;
	private EditText outgoingDisplay = null;
	private Button btnRead = null;
	private Button btnSend = null;
	private SMSBroadcastReceiver smsReceiver;
	private String toAddress = null;
	private SMSDbAdapter dbHelper;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.text_view);

		historyDisplay = (TextView)findViewById(R.id.text_top);
		outgoingDisplay = (EditText)findViewById(R.id.text_bot);
		btnRead = (Button)findViewById(R.id.text_b1);
		btnSend = (Button)findViewById(R.id.text_b2);

		btnRead.setOnClickListener(new ReadButtonClickListener());
		btnSend.setOnClickListener(new SendButtonClickListener());

		dbHelper = new SMSDbAdapter(this);
		dbHelper.open();

		smsReceiver = new SMSBroadcastReceiver();
		registerReceiver(smsReceiver, new IntentFilter(ACTION_SMS_SENT));

		populateWithIntent(getIntent());
	}

	private void populateWithIntent(Intent intent) {
		int threadId = intent.getIntExtra(THREAD_ID, -1);

		if (threadId != -1) {
			SMSRecord record = dbHelper.fetchLastInThread(threadId);
			
			String setText = ContactNames.get().getDisplayName(this, record.getAddress());
			setText += ": ";
			setText += record.getText();

			historyDisplay.setText(setText);
		}
		else {
			// No conversation history
			historyDisplay.setText(R.string.new_convo);
		}

		String initialMessage = intent.getStringExtra(INITIAL_TEXT);
		if (initialMessage != null && !initialMessage.equals("")) {
			outgoingDisplay.setText(initialMessage);
		}

		toAddress = intent.getStringExtra(THREAD_ADDRESS);
	}

	@Override
	public void onDestroy() {
		this.unregisterReceiver(smsReceiver);
		dbHelper.close();
		super.onDestroy();
	}

	/**
	 * This method will process a voice command and turn it into 
	 *  a command for this specific screen.
	 */
	public boolean processVoice(VoiceCommand command) {

		if (command.getType() == CommandAction.READ) {
			speak(outgoingDisplay.getText().toString(), SpeechType.PERSONAL);

			return true;
		}
		else if (command.getType() == CommandAction.COMPOSE || 
				command.getType() == CommandAction.REPLY) {
			outgoingDisplay.getText().append(command.getTextGroup());

			speak("Waiting to send message", SpeechType.INFO);

			return true;
		}
		else if (command.getType() == CommandAction.SEND) {
			sendMessage();

			return true;
		}
		else if (command.getType() == CommandAction.TEXT) {
			outgoingDisplay.getText().append(" " + command.getTextGroup());

			return true;
		}

		return false;

	}

	private void sendMessage() {
		SmsManager sms = SmsManager.getDefault();

		List<String> messages = sms.divideMessage(outgoingDisplay.getText().toString());

		try {
			for (String message : messages) {
				dbHelper.addMsg(toAddress, -2, System.currentTimeMillis(), message);
				sms.sendTextMessage(toAddress, null, message, 
						PendingIntent.getBroadcast(this, 0, new Intent(ACTION_SMS_SENT), 0), null);
			}
		}
		catch (Exception e) {
			speak("Error: Message not sent", SpeechType.INFO);
			Log.e(SMS_ERROR, "Failure sending message");
		}

		speak("Message sent", SpeechType.INFO, true);

		// TODO can we pause the app better?
		try {
			Thread.sleep(400);
		}
		catch (Exception e) {
			// Well, just did this so it could speak anyway, so
			Log.e(SMS_ERROR, "Problem sleeping to back out after sending msg.");
		}

		setResult(MainChatWindow.MESSAGE_SENT);
		finish();
	}

	private class SMSBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			String message = null;

			switch (getResultCode()) {
			case Activity.RESULT_OK:
				message = "Message sent!";
				break;
			case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
				message = "Error.";
				break;
			case SmsManager.RESULT_ERROR_NO_SERVICE:
				message = "Error: No service.";
				break;
			case SmsManager.RESULT_ERROR_NULL_PDU:
				message = "Error: Null PDU.";
				break;
			case SmsManager.RESULT_ERROR_RADIO_OFF:
				message = "Error: Radio off.";
				break;
			}

			speak(message, SpeechType.INFO);

		}
	}

	private class ReadButtonClickListener implements OnClickListener {

		public void onClick(View arg0) {
			speak(outgoingDisplay.getText().toString(), SpeechType.PERSONAL);
		}

	}

	private class SendButtonClickListener implements OnClickListener{

		public void onClick(View arg0) {
			sendMessage();
		}

	}

}
