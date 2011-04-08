package com.snakefish.visms;

import com.snakefish.feedback.CommandAction;
import com.snakefish.feedback.SpeechType;
import com.snakefish.feedback.VoiceCommand;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * The view of a conversation thread with another partner.
 *   Loads up the messages in that thread, then displays them
 *   in a list.
 */
public class MainChatWindow extends SMSListActivity {

	public static final int SETTINGS_ID = Menu.FIRST;
    public static final String THREAD_ID = "com.snakefish.THREAD_ID";
    public static final String LAST_MESSAGE = "com.snakefish.LAST_MESSAGE";
    
	private static final int PICK_CONTACT_REQUEST = 1;
    private TextView textTop;
    private Button compose;
    private Button contactChooser;
    private SmsDbAdapter dbHelper;
    private String recipient;
    private String lastMessage;
    private int threadId;

    
    public MainChatWindow() {
    	super(R.xml.mcw_speech);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main_chat_window);
        
        textTop = (TextView)findViewById(R.id.mcw_text_top);
        compose = (Button)findViewById(R.id.mcw_compose);
        contactChooser = (Button)findViewById(R.id.mcw_contact_chooser);
        
        contactChooser.setOnClickListener(new OnContactListener());
        compose.setOnClickListener(new ComposeClickListener());
        
        dbHelper = new SmsDbAdapter(this);
        dbHelper.open();
        
        populateConversationList(getIntent());
    }
    
    /**
     * Pull the conversation id from the intent and populate the conversation list.
     * @param intent
     */
    protected void populateConversationList(Intent intent) {
        
    		if (intent != null) {
    	        	this.lastMessage = intent.getStringExtra(LAST_MESSAGE);
    	        	
    	    		int threadID = intent.getIntExtra(THREAD_ID,-1);
    				this.threadId = threadID;
    	    		
    	    		//TODO Debug
        	        Log.v("MainChatWindow, populateConversationList", "Thread ID: "+threadID);
    	    		
        	        if (threadID != -1) {
    	    		    Cursor c = dbHelper.fetchThreadByThreadId(threadID);
    	    		    startManagingCursor(c);
    	    		    
    	    		    if (c.moveToFirst()) {
    	    		    	recipient = c.getString(c.getColumnIndex(SmsDbAdapter.KEY_ADDRESS));
    	    		    } else {
    	    		    	Log.e(this.toString(), "Cursor is empty.");
    	    		    }
    	    		    
    	    		    // If the address is in our contacts...
    	    		    // Set recipient to contact name
    	    		    // TODO
    	    		    
    	    		    // Otherwise, just use phone number
    	    		    textTop.setText(recipient);
    	    		    
    	    		    setListAdapter(new ConversationAdapter(this, c));
    					
    	    		    
    	    		    contactChooser.setVisibility(View.GONE);
    	    		    textTop.setVisibility(View.VISIBLE);
    	    		    compose.setEnabled(true);
    	    		} else {
    	    			Log.e("MainChatWindow, populateConversationList", "Intent missing thread id.");
    	    			
    	    			compose.setEnabled(false);
    	    		}
    	    	}
    }

    /**
     * This method will process a voice command and turn it into 
     *  a command for this specific screen.
     */
	public boolean processVoice(VoiceCommand command) {
    	
		Log.v("MainChatWindow", "Processing voice in window.");
		
    	if (command.getType() == CommandAction.READ) {
    		if (threadId != -1) {

        		// TODO don't assume the last message in convo
    			Cursor c = dbHelper.fetchThreadByThreadId(threadId);
    			c.moveToLast();
    			
    			int bodyColumn = c.getColumnIndex(SmsDbAdapter.KEY_BODY);
    			String bodyValue = c.getString(bodyColumn);
    			
    			speak(bodyValue, SpeechType.PERSONAL);
    		}
    		else {
    			speak("No messages in this conversation", SpeechType.PERSONAL);
    		}
    		
    		return true;
    	}
    	if (command.getType() == CommandAction.REPLY ||
    			command.getType() == CommandAction.COMPOSE) {
    		Log.v("MainChatWindow", "Doing reply on voice command.");
    		
    		doReply(command.getTextGroup());
    		
    		return true;
    	}
    	
    	return false;
    	
    }
    
    private class OnContactListener implements OnClickListener {

		public void onClick(View arg0) {
			startActivityForResult(getContactIntent(), PICK_CONTACT_REQUEST);
		}
    	
    }
    
    private class ComposeClickListener implements OnClickListener {

		public void onClick(View arg0) {
			doReply(null);
		}
    	
    }
    
    public void doReply(String text) {
    	Intent textIntent = new Intent();
    	textIntent.setClass(this, TextActivity.class);
    	
    	String[] messageData = new String[2];
    	
    	messageData = grabLastData();
    	
    	if (messageData != null) {
    		textIntent.putExtra(TextActivity.CONVERSATION_LAST_MSG_FROM, messageData[0]);
    		textIntent.putExtra(TextActivity.CONVERSATION_LAST_MSG_DATA, messageData[1]);
    	}
    	
    	if (text != null && !text.equals("")) {
    		textIntent.putExtra(TextActivity.INITIAL_TEXT, text);
    	}
    	
    	if (recipient != null) {
    		textIntent.putExtra(TextActivity.FROM_ADDRESS, recipient);
    	}
    	
    	startActivity(textIntent);
    }
    
    private String[] grabLastData() {
    	// TODO don't be a prick about DB access
    	if (lastMessage != null) {
    		return new String[] { "Jason", lastMessage};
    	}
    	
    	if (threadId != -1) {
    		Cursor c = dbHelper.fetchThreadByThreadId(threadId);
    		c.moveToLast();
    		
    		int bodyColumn = c.getColumnIndex(SmsDbAdapter.KEY_BODY);
    		String bodyValue = c.getString(bodyColumn);
    		
    		// TODO pull person from cursor and contacts api
    		
    		return new String[] {"Jason", bodyValue};
    	}
    	else {
    		// TODO make sure this case doesn't happen
    		return null;
    	}
    }
    
    /**
     * Invoked when the contact picker is completed.
     */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PICK_CONTACT_REQUEST && resultCode == RESULT_OK) {
			processContactInfo(data.getData());
		}else {
			Log.e("MainChatWindow", "Error choosing contact");
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * Returns an Intent to be used as the Contact picker
	 */
	protected Intent getContactIntent(){
		return new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
	}


	/**
	 * Process the contact info
	 */
	public void processContactInfo(Uri contact ){
		AsyncTask<Uri, Void, ContactInfo> task = new AsyncTask<Uri, Void, ContactInfo>() {

			@Override
			protected ContactInfo doInBackground(Uri... uris) {

				ContentResolver contentResolver = getContentResolver();
				Uri contactUri = uris[0];

				ContactInfo contactInfo = new ContactInfo();
				long contactId = -1;

				// Load the display name for the specified person
				Cursor cursor = contentResolver.query(contactUri,
						new String[]{Contacts._ID, Contacts.DISPLAY_NAME}, null, null, null);
				try {
					if (cursor.moveToFirst()) {
						contactId = cursor.getLong(0);
						contactInfo.setDisplayName(cursor.getString(1));
					}
				} finally {
					cursor.close();
				}

				// Load the phone number (if any).
				cursor = contentResolver.query(Phone.CONTENT_URI,
						new String[]{Phone.NUMBER},
						Phone.CONTACT_ID + "=" + contactId, null, Phone.IS_SUPER_PRIMARY + " DESC");
				try {
					if (cursor.moveToFirst()) {
						contactInfo.setPhoneNumber(cursor.getString(0));
					}
				} finally {
					cursor.close();
				}

				return contactInfo;       	
			}

			/**
			 * Called after contact info is processed. 
			 * @param result 	The final result containing the Contact name and number
			 */
			@Override
			protected void onPostExecute(ContactInfo result) {
				recipient = result.getPhoneNumber();

				// TODO handle getting the better visible version
				if (recipient != null && !recipient.equals("")) {
					contactChooser.setVisibility(View.GONE);
					compose.setEnabled(true);
					textTop.setVisibility(View.VISIBLE);

					textTop.setText(recipient);
				}
				else {
					speak("That contact has no number", SpeechType.INFO);
				}
			}
		};

		task.execute(contact);
	}
	

}
