package com.snakefish.visms;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class ConversationsActivity extends SMSListActivity {
	/** The user who you are communicating with */
	public static final String CONVERSATION_CONTACT = "com.snakefish.CONTACT";
	
	public static final int COMPOSE_ID = Menu.FIRST;
	public static final int SETTINGS_ID = Menu.FIRST + 1;
	public static final int OPEN_ID = Menu.FIRST + 2;
	public static final int DELETE_ID = Menu.FIRST + 3;
	public static final Uri SMS_INBOX_URI = Uri.parse("content://sms/inbox");
	private SmsDbAdapter mDbHelper;

	public ConversationsActivity() {
		super(R.xml.conv_speech);
	}

	public void processVoice(String command) {
		// TODO
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.convo_list);

		mDbHelper = new SmsDbAdapter(this);
		mDbHelper.open();

		// **TESTING DATABASE, REMOVE WHEN DONE
		// Adding dummy conversations to db
		mDbHelper.deleteInbox();
		mDbHelper.addMsg(1, "1-570-400-0104", 2, 1286456244, "Yo what's up?");
		mDbHelper.addMsg(1, "1-570-400-0104", 2, 1286456844, "R u thar?");
		mDbHelper.addMsg(2, "1-203-733-8028", 3, 1286551147,
				"Are you done yet?");
		// ** TESTING DATABASE, REMOVE WHEN DONE

		fillInbox();
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
//			Intent compose = new Intent();
//			compose.setClassName("com.snakefish.visms", 
//					"com.snakefish.visms.TextActivity");
//			startActivity(compose);
			return true;
		case SETTINGS_ID:
			Intent options = new Intent(this, OptionsList.class);
//			options.setClassName("com.snakefish.visms",
//					"com.snakefish.visms.OptionsList");
			startActivity(options);
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case OPEN_ID:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
					.getMenuInfo();
//			Intent i = new Intent(this, TextActivity.class);
//			i.putExtra(SmsDbAdapter.KEY_ROWID, info.id);
//			startActivity(i);
			
			openConvo(info.id);
			return true;
		case DELETE_ID:
			// AdapterContextMenuInfo info =(AdapterContextMenuInfo)
			// item.getMenuInfo();
			// mDbHelper.deleteThread(info.id);
			// getInbox();
			return true;
		}
		return super.onContextItemSelected(item);
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        
        openConvo(id);
        
    }


	private void fillInbox() {
		Cursor c = mDbHelper.fetchAllMsgs();
		startManagingCursor(c);

		String[] from = new String[] { SmsDbAdapter.KEY_ADDRESS };

		int[] to = new int[] { R.id.convo_entry };

		SimpleCursorAdapter convos = new SimpleCursorAdapter(this,
				R.layout.list_item, c, from, to);
		setListAdapter(convos);

	}
	
	private void openConvo(long id) {
		/*
		 * START hard-coded transition to dummy message screen
		 */
		Intent thread = new Intent();
		thread.setClassName("com.snakefish.visms", 
				"com.snakefish.visms.MainChatWindow");
		System.out.println(id);
		thread.putExtra(MainChatWindow.THREAD_ID, (int)id);
		startActivity(thread);
		/*
		 * END
		 */
	}

}
