package com.snakefish.visms;

import java.util.List;

import com.snakefish.feedback.CommandAction;
import com.snakefish.feedback.SpeechType;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
 * @author Brian
 *
 */
public class TextActivity extends SMSActivity {

	public static final String CONVERSATION_LAST_MSG = "com.snakefish.LAST_MESSAGE";
	public static final String ACTION_SMS_SENT = "com.snakefish.SMS_SENT_ACTION";
	
	public TextActivity() {
		super(R.xml.text_speech);
	}
	
	private TextView textTop = null;
	private EditText textBot = null;
	private Button butLeft = null;
	private Button butRight = null;
	private SMSBroadcastReceiver smsReceiver;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.text_view);
        
        textTop = (TextView)findViewById(R.id.text_top);
        textBot = (EditText)findViewById(R.id.text_bot);
        butLeft = (Button)findViewById(R.id.text_b1);
        butRight = (Button)findViewById(R.id.text_b2);

        assert(textTop != null);
        assert(textBot != null);
        assert(butLeft != null);
        assert(butRight != null);
        
        butLeft.setOnClickListener(new LeftButtonClickListener());
        butRight.setOnClickListener(new RightButtonClickListener());
        
        smsReceiver = new SMSBroadcastReceiver();
        registerReceiver(smsReceiver, new IntentFilter(ACTION_SMS_SENT));
        
        populateWithIntent(getIntent());
    }
    
    protected void populateWithIntent(Intent intent) {
    	if (intent != null) {
    		List<String> convData = intent.getStringArrayListExtra(CONVERSATION_LAST_MSG);
    		
    		if (convData != null) {
    			if (convData.size() == 2) {
    				textTop.setText(convData.get(0) + ":");
    				textTop.append(" " + convData.get(1));
    			}
    			if (convData.size() == 1) {
    				// TODO this isn't an ideal case, we shouldn't hit it
    				textTop.setText(convData.get(0));
    			}
    		}
    	}
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	
    	this.unregisterReceiver(smsReceiver);
    }
    
    public void processVoice(String command) {
    	List<CommandAction> commands = this.commandsRequested();
    	
    	if (commands.contains(CommandAction.READ)) {
    		speak(textBot.getText().toString());
    	}
    	else {
    		textBot.getText().append(command);
    		
    		if (commands.contains(CommandAction.SEND)) {
    			sendMessage();
    		}
    	}
    }
    
    protected void sendMessage() {
    	SmsManager sms = SmsManager.getDefault();
    	
    	List<String> messages = sms.divideMessage(textBot.getText().toString());
    	String recipient = "";  // TODO insert address here
    	
    	// TODO actually send message
    	//  For now, we'll just report success anyway
    	//  and close immdiately
    	try {
    		//for (String message : messages) {
    		//	sms.sendTextMessage(recipient, null, message, 
    		//			PendingIntent.getBroadcast(this, 0, new Intent(ACTION_SMS_SENT), 0), null);
    		//}
    	}
    	catch (Exception e) {
    		speak("Error: Message not sent");
    	}
    	
    	speak("Message sent", SpeechType.INFO, true);
    	
    	// TODO can we pause the app better?
    	try {
    		Thread.sleep(400);
    	}
    	catch (Exception e) {
    		// Well, just did this so it could speak anyway, so
    	}
    	
    	finish();
    }
    
    // TODO
    // WITH DUMMY IMPLEMENTATION OF SEND(), THIS WILL NOT OCCUR
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

            speak(message);

    	}
    }
    
    private class LeftButtonClickListener implements OnClickListener {

    	/**
    	 * Do not fill in this method any more!
    	 * Refer to seperate reusable methods instead
    	 */
		public void onClick(View arg0) {
			doWork();
		}
		
		public void doWork() {
			speak(textBot.getText().toString());
		}
    	
    }
    
    private class RightButtonClickListener implements OnClickListener{

    	/**
    	 * Do not fill in this method any more!
    	 * Refer to seperate reusable methods instead
    	 */
		public void onClick(View arg0) {
			doWork();
		}
		
		public void doWork() {
			sendMessage();
		}
    	
    }
    
}
