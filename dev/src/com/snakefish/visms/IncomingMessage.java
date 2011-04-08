package com.snakefish.visms;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.snakefish.feedback.CommandAction;
import com.snakefish.feedback.SpeechType;
import com.snakefish.feedback.VoiceCommand;

/**
 * This class deals with the GUI used for an 
 * incoming message.
 * 
 * @author Team Snakefish
 *
 */
public class IncomingMessage extends SMSActivity {
	
	public static final String SMS_ERROR = "IncomingMessage";
	
	/** The raw number of the message sent to us */
	public static final String SMS_FROM_ADDRESS_EXTRA = "com.snakefish.FROM_EXTRA";

	/** The display name for who sent a message to us */
	public static final String SMS_FROM_DISPLAY_NAME_EXTRA = "com.snakefish.DISPLAY_EXTRA";

	/** The actual content of the message sent to us */
	public static final String SMS_MESSAGE_EXTRA = "com.snakefish.MESSAGE_EXTRA";

	/** Time, in ms, of the message sent */
	public static final String SMS_TIME_SENT_EXTRA = "com.snakefish.TIME_SENT";
	
	/**Used to position buttons */
	public static final int SETTINGS_POSITION = Menu.FIRST;
	/** The button used for viewing the message */
	protected Button btnRead = null;
	/** The button used to ignore messages */
	protected Button btnIgnore = null;
    /** The text view that is the contact information */
    protected TextView messageFrom = null; 
    
    /** The thread this message is going into */
    protected int threadId;
    /** The message data that we received */
    protected String actualMessage;
    /** The contact retrieved name that we got earlier */
    protected String actualFrom;
    
    protected String fromAddress = null;
    
    /** Our helper for messages, options, etc in the database */
    protected SmsDbAdapter dbHelper;
    
    /**
     * Creates a new incoming message
     */
    public IncomingMessage(){
    	super (R.xml.newtext_speech);
    }
    
    /**
     * This method will process a voice command and turn it into 
     *  a command for this specific screen.
     */
	public boolean processVoice(VoiceCommand command) {
		
		if (command.getType() == CommandAction.SEND ||
				command.getType() == CommandAction.REPLY) {
			
			String text = command.getTextGroup();
			skipToReply(text);
			
			return true;
		}
		if (command.getType() == CommandAction.READ) {
			 speak(actualMessage, SpeechType.PERSONAL);
			 
			 return true;
		}
		if (command.getType() == CommandAction.VIEW) {
			openConversationWindow();
			
			return true;
		}
		if (command.getType() == CommandAction.IGNORE) {
			onIgnored();
			
			return true;
		}
		
		return false;
    		
    }
	
	/**
	 * Skip the step where we view the given conversation,
	 *   and instead skip straight to the screen where we will
	 *   send a message to the conversation partner, inserting
	 *   the given text in the message to send.
	 *   
	 * @param text initial text for message to send
	 */
	private void skipToReply(String text) {
		Intent replyIntent = new Intent();
		replyIntent.setClass(this, TextActivity.class);
		
		replyIntent.putExtra(TextActivity.INITIAL_TEXT, text);

		replyIntent.putExtra(TextActivity.CONVERSATION_LAST_MSG_FROM, actualFrom);
		replyIntent.putExtra(TextActivity.CONVERSATION_LAST_MSG_DATA, actualMessage);
		
		replyIntent.putExtra(TextActivity.FROM_ADDRESS, fromAddress);
		
		startActivity(replyIntent);
	}
	
	/**
	 * Switches to the next screen
	 */
	public void openConversationWindow(){
		Intent vText = new Intent();
		vText.setClass(this, MainChatWindow.class);
		vText.putExtra(MainChatWindow.THREAD_ID, threadId);
		vText.putExtra(MainChatWindow.LAST_MESSAGE, actualMessage);
		
		startActivity(vText);
	}
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        // Both landscape and portrait view
        setContentView(R.layout.incoming_message);      
    	
        //Sets btnRead and btnIgnore to the button on the android xml
        btnRead = (Button) findViewById(R.id.btnRead);
        btnIgnore = (Button) findViewById(R.id.btnIgnore);   
        
        //Sets the messageFrom text view
        messageFrom = (TextView) findViewById(R.id.messageFrom);
        
        //Sets listeners to each button
        btnRead.setOnClickListener(new ReadListener());
        btnIgnore.setOnClickListener(new IgnoreListener());
        
        dbHelper = new SmsDbAdapter(this);
        dbHelper.open();
        
        parseMessageData(getIntent());
    }
    
    private void parseMessageData(Intent intent) {
    	String displayName = null;
    	long timeSent = -1;
    	
    	fromAddress = intent.getStringExtra(SMS_FROM_ADDRESS_EXTRA);
    	displayName = intent.getStringExtra(SMS_FROM_DISPLAY_NAME_EXTRA);
    	actualMessage = intent.getStringExtra(SMS_MESSAGE_EXTRA);
    	timeSent = intent.getLongExtra(SMS_TIME_SENT_EXTRA, -1) / 1000;
    	
    	// Deal with the database
    	//  We need the thread from this person, and to put the text into the db
    	if (fromAddress != null) {
    		// TODO get the correct thread id or store it right
    		// Right now we're getting extra msgs in one convo
    		threadId = dbHelper.getThreadId(fromAddress);
    	}
    	
    	if (fromAddress != null && actualMessage != null) {
    		dbHelper.addMsg(threadId, fromAddress, 1, timeSent, actualMessage);
    	}
    	// End database dealing
    	
    	
    	if (displayName != null) {
    		messageFrom.setText(displayName);
    	}
    	else if (fromAddress != null) {
    		messageFrom.setText(fromAddress);
    	}
    	
    }
    
    /**
     * This class will be used if the incoming message
     * is ignored. The program will speak "Message
     * Ignored" and close gracefully.
     */
    public void onIgnored(){
    	//Lets the user know the message has been ignored
    	speak("Message Ignored", SpeechType.INFO, true);
    	
    	// TODO sleep better
    	try {
    		Thread.sleep(400);
    	}
    	catch (Exception e) {
    		Log.e(SMS_ERROR, "Interruption while ignoring.");
    	}
    	
    	finish();
    }
    
    /**
     * The listener used for reading messages. This class
     * will be used to change the screen to the message
     * screen.
     */
    private class ReadListener implements OnClickListener {

    	/**
    	 * Use to call the work method. Similar to the 
    	 * implementation in TextActivity.java
    	 */
		public void onClick(View arg0) {
			openConversationWindow();
		}
    }
    
    /**
     * The listener used for ignoring messages. It will call 
     * the doWork method which will call onFinish.
     */
    private class IgnoreListener implements OnClickListener {

		public void onClick(View arg0) {
			onIgnored();
		}
    }
}
