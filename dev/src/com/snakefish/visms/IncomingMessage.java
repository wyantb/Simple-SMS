package com.snakefish.visms;

import com.snakefish.feedback.CommandAction;
import com.snakefish.feedback.SpeechType;
import com.snakefish.feedback.VoiceCommand;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * This class deals with the GUI used for an 
 * incoming message.
 * 
 * @author Team Snakefish
 *
 */
public class IncomingMessage extends SMSActivity {
	/**Used to position buttons */
	public static final int SETTINGS_POSITION = Menu.FIRST;
	/** The button used for viewing the message */
	protected Button btnRead = null;
	/** The button used to ignore messages */
	protected Button btnIgnore = null;
	/** Used to get the name of the contact */
	protected String name = new String();
    /** The text view that is the contact information */
    protected TextView messageFrom = null; 
    
    /**
     * Creates a new incoming message
     */
    public IncomingMessage(){
    	super (R.xml.newtext_speech);
    	//Sets the contact name to the name of the person
    	// sending the message.
    }
    
    /**
     * This method will process the voice command and turn it into 
     * a command for the application
     */
	public void processVoice(VoiceCommand command) {
    	/** These commands may be updated at a later time */
    	
		if (command.getType() == CommandAction.READ || command.getType() == CommandAction.REPLY
				|| command.getType() == CommandAction.VIEW) {
			
			//Makes a new TextActivity
			Intent vText = new Intent();
			//Sets the intent to class activity
			vText.setClassName("com.snakefish.visms", "com.snakefish.visms.MainChatWindow");
			//Starts the activity
			startActivity(vText);
			//Sends in the name of the contact
			vText.putExtra("com.snakefish.CONTACT", name);
		}
		
    	//If the command mimics the ignore button
		if (command.getType() == CommandAction.IGNORE) {
			onIgnored();
		}
    		
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
        
        // TODO get name from intent
    	//messageFrom.setText("Jason", TextView.BufferType.NORMAL);
        
        //Sets listeners to each button
        btnRead.setOnClickListener(new ReadListener());
        btnIgnore.setOnClickListener(new IgnoreListener());
    } 
    
    /**
     * The pop up menu that allows the user to adjust
     * the setting of the application
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, Menu.FIRST, 0, R.string.settings);
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
     * Sets the name string
     * 
     * @param name - the name of the person texting 
     */
    public void setName(String name){
    	this.name = name;
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
			// Calls the work method
			doWork();
		}
    	
		/**
		 * Switches to the next screen
		 */
		public void doWork(){
			
			//Makes a new TextActivity
			Intent vText = new Intent();
			//Sets the intent to class activity
			vText.setClassName("com.snakefish.visms", "com.snakefish.visms.MainChatWindow");
			//Starts the activity
			startActivity(vText);
			
			// TODO dangit Jason, this doesn't go after you launch the intent
			//Sends in the name of the contact
			vText.putExtra(name, name);
			
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
