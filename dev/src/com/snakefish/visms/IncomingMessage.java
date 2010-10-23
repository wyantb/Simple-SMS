package com.snakefish.visms;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
	
	public static final String SMS_FROM_ADDRESS_EXTRA = "com.snakefish.FROM_EXTRA";
	public static final String SMS_FROM_DISPLAY_NAME_EXTRA = "com.snakefish.DISPLAY_EXTRA";
	public static final String SMS_MESSAGE_EXTRA = "com.snakefish.MESSAGE_EXTRA";
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
    protected int threadId = -1;
    /** The message data that we received */
    protected String actualMessage = "";
    /** The contact retrieved name that we got earlier */
    // TODO example data
    protected String actualFrom = "Jason Buoni";
    
    protected String fromAddress = null;
    
    /** Our helper for messages, options, etc in the database */
    protected SmsDbAdapter dbHelper;
    
    /**
     * Creates a new incoming message
     */
    public IncomingMessage(){
    	super (R.xml.newtext_speech);
    }
    
    /** Gets the partner of this conversation */
    public int getThreadId() {
    	return threadId;
    }
    
    /**
     * This method will process the voice command and turn it into 
     * a command for the application
     */
	public boolean processVoice(VoiceCommand command) {
    	/** These commands may be updated at a later time */
    	
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
		
    	//If the command mimics the ignore button
		if (command.getType() == CommandAction.IGNORE) {
			onIgnored();
			
			return true;
		}
		
		return false;
    		
    }
	
	private void skipToReply() {
		skipToReply("");
	}
	
	private void skipToReply(String text) {
		Intent replyIntent = new Intent();
		replyIntent.setClass(this, TextActivity.class);
		
		replyIntent.putExtra(TextActivity.INITIAL_TEXT, text);
		
		String[] fromData = new String[2];
		fromData[0] = actualFrom;
		fromData[1] = actualMessage;
		
		replyIntent.putExtra(TextActivity.CONVERSATION_LAST_MSG, fromData);
		
		replyIntent.putExtra(TextActivity.FROM_ADDRESS, fromAddress);
		
		startActivity(replyIntent);
	}
	
	/**
	 * Switches to the next screen
	 */
	public void openConversationWindow(){
		
		//Makes a new TextActivity
		Intent vText = new Intent();
		
		//Sets the intent to class activity
		vText.setClassName("com.snakefish.visms", "com.snakefish.visms.MainChatWindow");
		
		// Puts the thread id in for MainChatWindow
		vText.putExtra(MainChatWindow.THREAD_ID, getThreadId());
		
		vText.putExtra(MainChatWindow.LAST_MESSAGE, actualMessage);
		
		//Starts the activity
		startActivity(vText);
	}
    
    /** Called when the activity is first created. */
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
     * The pop up menu that allows the user to adjust
     * the setting of the application
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
		Intent i = new Intent(this, OptionsList.class);
		startActivity(i);
		
        boolean result = super.onCreateOptionsMenu(menu);
        //menu.add(0, Menu.FIRST, 0, R.string.settings);
        return result;
    }
    
    /**
     * Sets up the options button
     */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case SETTINGS_POSITION:
			Intent options = new Intent();
			options.setClassName("com.snakefish.visms",
					"com.snakefish.visms.OptionsList");
			startActivity(options);
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}

    /**
     * Pauses incoming message
     */
    @Override
    public void onPause(){
    	super.onPause();
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
    		// same as above
    	}
    	
    	finish();
    }
    
    /**
     * The stopping method used for the program. 
     * I really don't know if we need to use this or 
     * not.
     */
    public void onStop(){
    	super.onStop();
    }
    
    /**
     * The destroy method for the 
     * class.
     */
    public void onDestroy(){
    	super.onDestroy();
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

    	/**
    	 * Use to call the work method. Similar to the 
    	 * implementation in TextActivity.java
    	 */
		public void onClick(View arg0) {
			// Calls the work method
			doWork();
		}
    	
		/**
		 * Calls the onFinish method to end
		 * the application.
		 */
		public void doWork(){
			//Calls onFinish
			onIgnored();
		}
    }
}
