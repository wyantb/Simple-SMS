package com.snakefish.visms;

import java.util.List;

import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class MainChatWindow extends SMSListActivity {

    public static final String THREAD_ID = "com.snakefish.THREAD_ID";
    private TextView textTop;
    private Button compose;
    private SmsDbAdapter DBHelper;
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
        
        //Throwing a null pointer for some reason
        //compose.setOnClickListener(new ButtonClickListener());
        
        //TODO Pull actual data
//        DBHelper = new SmsDbAdapter(this);
//        DBHelper.open();
        
        populateConversationList(getIntent());
    }
    
    /**
     * Pull the conversation id from the intent and populate the conversation list.
     * @param intent
     */
    protected void populateConversationList(Intent intent) {
    	//TODO Pull actual data
//	if (intent != null) {
//    		int threadID = intent.getIntExtra(THREAD_ID,-1);
//    		if (threadID != -1) {
//    		    Cursor c = DBHelper.fetchThreadByThreadID(threadID);
//    		    startManagingCursor(c);
//    		    String[] from = new String[] {SmsDbAdapter.KEY_BODY};
//    		    int[] to = new int[] {R.id.convo_entry };
//    		    SimpleCursorAdapter thread = new SimpleCursorAdapter(this,
//			R.layout.list_item, c, from, to);
//    		    setListAdapter(thread);
//    		    textTop.setText("TEST");
//    		}
//    	}
	
	//Start dummy data:
	String[] messages = {"hey whus up", "want to get fud?", "lol u there???", "txt me back pls", "Ok fine ignore me"};
	setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, messages));
	//End dummy data.
    }
    
    public void processVoice(String command) {
	// TODO Auto-generated method stub

    }
    
    
    private class ButtonClickListener implements OnClickListener {

    	/**
    	 * Do not fill in this method any more!
    	 * Refer to seperate reusable methods instead
    	 */
		public void onClick(View arg0) {
			doWork();
		}
		
		public void doWork() {
			//TODO
		}
    	
    }

}
