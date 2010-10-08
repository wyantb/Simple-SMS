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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TextActivity extends SMSActivity {

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

        //assert(textTop != null);
        assert(textBot != null);
        assert(butLeft != null);
        assert(butRight != null);
        
        butLeft.setOnClickListener(new LeftButtonClickListener());
        butRight.setOnClickListener(new RightButtonClickListener());
        
        smsReceiver = new SMSBroadcastReceiver();
        registerReceiver(smsReceiver, new IntentFilter(ACTION_SMS_SENT));
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	
    	this.unregisterReceiver(smsReceiver);
    }
    
    public void processVoice(String command) {
    	textBot.setText("Received from voice: " + command);
    }
    
    protected void sendMessage() {
    	SmsManager sms = SmsManager.getDefault();
    	
    	List<String> messages = sms.divideMessage(textBot.getText().toString());
    	String recipient = "";  // TODO insert address here
    	
    	try {
    		for (String message : messages) {
    			sms.sendTextMessage(recipient, null, message, 
    					PendingIntent.getBroadcast(this, 0, new Intent(ACTION_SMS_SENT), 0), null);
    		}
    	}
    	catch (Exception e) {
    		speak("Made a booboo, message almost brought down app.");
    	}
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

            textTop.setText(message);
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
			// TODO actual work
			textBot.setText("SEND MORE!");
			sendMessage();
		}
    	
    }
    
}
