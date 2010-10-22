package com.snakefish.visms;

import java.util.ArrayList;
import java.util.List;

import com.snakefish.feedback.CommandAction;
import com.snakefish.feedback.SpeechType;
import com.snakefish.feedback.VoiceCommand;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.snakefish.feedback.CommandAction;
import com.snakefish.feedback.SpeechType;
import com.snakefish.feedback.VoiceCommand;


public class MainChatWindow extends SMSListActivity {

	public static final int SETTINGS_ID = Menu.FIRST;
    public static final String THREAD_ID = "com.snakefish.THREAD_ID";
	private static final int PICK_CONTACT_REQUEST = 1;
    private TextView textTop;
    private Button compose;
    private Button contactChooser;
    private SmsDbAdapter mDbHelper;
    private String recipient;
    private ContactInfo contactResult;
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

        assert(textTop != null);
        assert(compose != null);
        assert(contactChooser != null);
        
        contactChooser.setOnClickListener(new OnContactListener());
        compose.setOnClickListener(new ComposeClickListener());
        
        mDbHelper = new SmsDbAdapter(this);
        mDbHelper.open();
        
        populateConversationList(getIntent());
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, SETTINGS_ID, 0, R.string.settings);
		return result;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		
		case SETTINGS_ID:
			Intent options = new Intent(this, OptionsList.class);
			options.setClassName("com.snakefish.visms",
					"com.snakefish.visms.OptionsList");
			startActivity(options);
			return true;
		}
		
		return super.onMenuItemSelected(featureId, item);
	}
    
    /**
     * Pull the conversation id from the intent and populate the conversation list.
     * @param intent
     */
    protected void populateConversationList(Intent intent) {
        
    		if (intent != null) {
    	        
    	    		int threadID = intent.getIntExtra(THREAD_ID,-1);
    				this.threadId = threadID;
    	    		
    	    		//TODO Debug
        	        Log.v("MainChatWindow, populateConversationList", "Thread ID: "+threadID);
    	    		
        	        if (threadID != -1) {
    	    		    Cursor c = mDbHelper.fetchThreadByThreadId(threadID);
    	    		    startManagingCursor(c);
    	    		    if (c.moveToFirst()) {
    	    		    	recipient = c.getString(c.getColumnIndex(SmsDbAdapter.KEY_ADDRESS));
    	    		    } else {
    	    		    	Log.e(this.toString(), "Cursor is empty.");
    	    		    }
    	    		    if (false) { // If the address is in our contacts...
    	    		    	// Set recipient to contact name
    	    		    } else { // Just use phone number
    	    		    	textTop.setText(recipient);
    	    		    }
    	    		    
    					setListAdapter(new ConversationAdapter(this, c));
    					
    	    		    
    	    		    contactChooser.setVisibility(View.GONE);
    	    		    textTop.setVisibility(View.VISIBLE);
    	    		    compose.setEnabled(true);
    	    		} else {
    	    			Log.e("MainChatWindow, populateConversationList", "Intent missing thread id.");
    	    			
    	    			compose.setEnabled(false);
    	    		}
    	    	}

    	//Start dummy data:
//    	String[] messages = {"hey whus up", "want to get fud?", "lol u there???", "txt me back pls", "Ok fine ignore me"};
//    	setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, messages));
    	//End dummy data.
    }

	public boolean processVoice(VoiceCommand command) {
    	
    	if (command.getType() == CommandAction.READ) {
    		if (threadId != -1) {

        		// TODO don't assume the last message in convo
    			Cursor c = mDbHelper.fetchThreadByThreadId(threadId);
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
    		doReply(command.getTextGroup());
    		
    		return true;
    	}
    	
    	return false;
    	
    }
    
    public void doReply() {    	
    	doReply(null);
    }
    
    private class OnContactListener implements OnClickListener {

		public void onClick(View arg0) {
			compose.setEnabled(true);
			startActivityForResult(getContactIntent(), PICK_CONTACT_REQUEST);
		}
    	
    }
    
    private class ComposeClickListener implements OnClickListener {

    	/**
    	 * Do not fill in this method any more!
    	 * Refer to seperate reusable methods instead
    	 */
		public void onClick(View arg0) {
			doReply();
		}
    	
    }
    
    public void doReply(String text) {
    	Intent textIntent = new Intent();
    	textIntent.setClassName("com.snakefish.visms", "com.snakefish.visms.TextActivity");
    	
    	String[] messageData = new String[2];
    	
    	messageData = grabLastData();
    	
    	if (messageData != null) {
    		textIntent.putExtra(TextActivity.CONVERSATION_LAST_MSG, messageData);
    	}
    	
    	if (text != null && !text.equals("")) {
    		textIntent.putExtra(TextActivity.INITIAL_TEXT, text);
    	}
    	
    	startActivity(textIntent);
    }
    
    private String[] grabLastData() {
    	// TODO don't be a prick about DB access
    	if (threadId != -1) {
    		Cursor c = mDbHelper.fetchThreadByThreadId(threadId);
    		boolean didLast = c.moveToLast();
    		
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

    /////////////////////////////////////////
    //Code for picking contacts
    /////////////////////////////////////////
    
    //TODO Use this to start the contact picker:
    //			startActivityForResult(getContactIntent(), PICK_CONTACT_REQUEST);

    
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
				////////////////////////////////
				//TODO Code to set recipient
				////////////////////////////////
			}
		};

		task.execute(contact);
	}
	

}
