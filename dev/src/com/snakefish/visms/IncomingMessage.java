package com.snakefish.visms;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.snakefish.db.SMSDbAdapter;
import com.snakefish.db.SMSRecord;
import com.snakefish.feedback.CommandAction;
import com.snakefish.feedback.SpeechType;
import com.snakefish.feedback.VoiceCommand;
import com.snakefish.util.ContactNames;

/**
 * This class deals with the GUI used for an 
 * incoming message.
 * 
 * @author Team Snakefish
 *
 */
public class IncomingMessage extends SMSActivity {
	
	public static final String SMS_ERROR = "IncomingMessage";
	
	/** The message in our database that was received */
	public static final String SMS_MSG_ID = "com.snakefish.MSG_ID";
	
	/**Used to position buttons */
	public static final int SETTINGS_POSITION = Menu.FIRST;
	/** The button used for viewing the message */
	protected Button btnRead = null;
	/** The button used to ignore messages */
	protected Button btnIgnore = null;
    /** The text view that is the contact information */
    protected TextView messageFrom = null; 
    
    protected long msgId;
    
    /** The thread this message is going into */
    protected int threadId;
    /** The message data that we received */
    protected String actualMessage;
    /** The contact retrieved name that we got earlier */
    protected String actualFrom;
    
    protected String fromAddress = null;
    
    /** Our helper for messages, options, etc in the database */
    protected SMSDbAdapter dbHelper;
    
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
        
        btnRead = (Button) findViewById(R.id.btnRead);
        btnIgnore = (Button) findViewById(R.id.btnIgnore);
        messageFrom = (TextView) findViewById(R.id.messageFrom);
        
        btnRead.setOnClickListener(new ReadListener());
        btnIgnore.setOnClickListener(new IgnoreListener());
        
        dbHelper = new SMSDbAdapter(this);
        dbHelper.open();
        
        parseMessageData(getIntent());
    }
    
    private void parseMessageData(Intent intent) {
    	msgId = intent.getLongExtra(SMS_MSG_ID, -1);
    	
    	SMSRecord record = dbHelper.getMsg(msgId);
    	
    	messageFrom.setText(ContactNames.get().getDisplayName(this, record.getAddress()));
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
