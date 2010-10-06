package com.snakefish.visms;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class ConversationsActivity extends ListActivity {
	public static final int COMPOSE_POSITION = Menu.FIRST;
	public static final int SETTINGS_POSITION = Menu.FIRST + 1;
	
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
    
    /**
     * Fires when an item in the options menu is selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case Menu.FIRST: //Options
           Intent options = new Intent();
           options.setClassName("com.snakefish.visms", "com.snakefish.visms.OptionsList");
           startActivity(options);
           return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
}