package com.snakefish.visms;

import android.os.Bundle;
import android.view.Menu;

public class ConversationsActivity extends SMSListActivity {
	public static final int COMPOSE_POSITION = Menu.FIRST;
	public static final int SETTINGS_POSITION = Menu.FIRST + 1;
	
	public ConversationsActivity() {
		super(R.xml.conv_speech);
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.convo_list);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, COMPOSE_POSITION, 0, R.string.compose);
        menu.add(0, Menu.FIRST, 0, R.string.settings);
        return result;
    }
}