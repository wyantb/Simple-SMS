package com.snakefish.visms;

import java.util.ArrayList;
import java.util.List;

import com.snakefish.feedback.CommandAction;
import com.snakefish.feedback.SpeechType;
import com.snakefish.feedback.VoiceCommand;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
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


public class MainChatWindow extends SMSListActivity {

	public static final int SETTINGS_ID = Menu.FIRST;
    public static final String THREAD_ID = "com.snakefish.THREAD_ID";
    private TextView textTop;
    private Button compose;
    private SmsDbAdapter mDbHelper;
    private String recipient;
    
    public MainChatWindow() {
    	super(R.xml.mcw_speech);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main_chat_window);
        
        textTop = (TextView)findViewById(R.id.mcw_text_top);
        compose = (Button)findViewById(R.id.mcw_compose);

        assert(textTop != null);
        assert(compose != null);
        
        compose.setOnClickListener(new ComposeClickListener());
        
        mDbHelper = new SmsDbAdapter(this);
        mDbHelper.open();
        
        //TODO Pull actual data
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
    				    			
    	    		//TODO Debug
        	        Log.v("MainChatWindow, populateConversationList", "Thread ID: "+threadID);
    	    		
        	        if (threadID != -1) {
    	    		    Cursor c = mDbHelper.fetchThreadByThreadId(threadID);
    	    		    startManagingCursor(c);
    	    		    String[] from = new String[] {SmsDbAdapter.KEY_BODY};
    	    		    int[] to = new int[] {R.id.list_entry }; //TODO Maybe?
    	    		    //SimpleCursorAdapter thread = new SimpleCursorAdapter(this,
    				//R.layout.list_item, c, from, to);
    	    		    //setListAdapter(thread);
    					setListAdapter(new ConversationAdapter(this, c));
    	    		    textTop.setText("TEST");
    	    		    
    	    		} else {
    	    			Log.e("MainChatWindow, populateConversationList", "Intent missing thread id.");
    	    		}
    	    	}

    	//Start dummy data:
//    	String[] messages = {"hey whus up", "want to get fud?", "lol u there???", "txt me back pls", "Ok fine ignore me"};
//    	setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, messages));
    	//End dummy data.
    }

	public void processVoice(VoiceCommand command) {
    	
    	if (command.getType() == CommandAction.READ) {
    		speak("Ok fine ignore me", SpeechType.PERSONAL);
    	}
    	if (command.getType() == CommandAction.REPLY) {
    		doReply();
    	}
    	
    }
    
    public void doReply() {
    	Intent textIntent = new Intent();
    	textIntent.setClassName("com.snakefish.visms", "com.snakefish.visms.TextActivity");
    	
    	String[] messageData = new String[2];
    	messageData[0] = ("Jason");
    	messageData[1] = ("Ok fine ignore me");
    	
    	textIntent.putExtra(TextActivity.CONVERSATION_LAST_MSG, messageData);
    	
    	startActivity(textIntent);
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

}
