package com.snakefish.visms;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class ConversationsActivity extends ListActivity {
	public static final int COMPOSE_ID = Menu.FIRST;
	public static final int SETTINGS_ID = Menu.FIRST + 1;
	public static final int OPEN_ID = Menu.FIRST + 2;
	public static final int DELETE_ID = Menu.FIRST + 3;
	public static final Uri SMS_INBOX_URI = Uri.parse("content://sms/inbox");
	
	private SmsDbAdapter mDbHelper;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.convo_list);
		
		mDbHelper = new SmsDbAdapter(this);
		mDbHelper.open();
		getInbox();
		registerForContextMenu(getListView());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, COMPOSE_ID, 0, R.string.compose);
		menu.add(0, SETTINGS_ID, 0, R.string.settings);
		return result;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, OPEN_ID, 0, R.string.open);
		menu.add(0, DELETE_ID, 0, R.string.delete);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case COMPOSE_ID:
			// Start activity to compose a new message
			return true;
		case SETTINGS_ID:
			// Start activity to edit options
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case OPEN_ID:
				AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
				Intent i = new Intent(this, TextActivity.class);
				i.putExtra(SmsDbAdapter.KEY_ROWID, info.id);
				startActivity(i);
				return true;
			case DELETE_ID:
//				AdapterContextMenuInfo info =(AdapterContextMenuInfo) item.getMenuInfo();
//				mDbHelper.deleteThread(info.id);
//				getInbox();
				return true;
		}
		return super.onContextItemSelected(item);
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

	private void getInbox() {
		Cursor c = mDbHelper.fetchAllMsgs();
		startManagingCursor(c);
		
		String[] from = new String[] {SmsDbAdapter.KEY_ADDRESS};
		
		int[] to = new int[] {R.id.convo_entry};
		
		SimpleCursorAdapter convos =
			new SimpleCursorAdapter(this, R.layout.list_item, c, from, to);
		setListAdapter(convos);

	}

}