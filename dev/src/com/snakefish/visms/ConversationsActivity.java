package com.snakefish.visms;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.snakefish.feedback.CommandAction;
import com.snakefish.feedback.VoiceCommand;

/**
 * The default entry point into the SMS application.
 * 
 * Contains a list of all threads that the user can choose,
 *   in order to converse with the person in that thread.
 *   
 * Alternately, can compose a new message from this screen.
 * 
 */
public class ConversationsActivity extends SMSListActivity {

	public static final int COMPOSE_ID = Menu.FIRST;
	public static final int SETTINGS_ID = Menu.FIRST + 1;
	public static final int OPEN_ID = Menu.FIRST + 2;
	public static final int DELETE_ID = Menu.FIRST + 3;
	private SmsDbAdapter dbHelper;
	private Button btnCompose;

	public ConversationsActivity() {
		super(R.xml.conv_speech);
	}

    /**
     * This method will process a voice command and turn it into 
     *  a command for this specific screen.
     */
	public boolean processVoice(VoiceCommand command) {

		if (command.getType() == CommandAction.COMPOSE) {
			openConvo(-1);
			
			return true;
		}

		return false;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.convo_list);

		btnCompose = (Button)findViewById(R.id.btnCompose);

		btnCompose.setOnClickListener(new ComposeClickListener());

		dbHelper = new SmsDbAdapter(this);
		dbHelper.open();

		fillInbox();
		registerForContextMenu(getListView());
	}

	/**
	 * Occurs when long clicking on any thread in this window.
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, OPEN_ID, 0, R.string.open);
		menu.add(0, DELETE_ID, 0, R.string.delete);
	}

	/**
	 * Occurs an item is selected after long clicking a thread.
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		TextView v = (TextView) info.targetView;
		String address = String.valueOf(v.getText());
		long id = dbHelper.getThreadId(address);
		
		switch (item.getItemId()) {
		case OPEN_ID:
			Log.v("ConversationsActivity",
					"Opening conversation from context menu, thread id: " + id);
			openConvo(id);
			return true;
		case DELETE_ID:
			Log.v("ConversationsActivity", "Deleting conversation from context menu, thread id: " + id);
			return deleteConvo(id);
		}
		return super.onContextItemSelected(item);
	}

	/**
	 * Occurs on a long click for clicking a thread.
	 */
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		TextView clickedThread = (TextView) v;
		String address = String.valueOf(clickedThread.getText());
		long thread_id = dbHelper.getThreadId(address);
		Log.v(this.toString(), "Opening conversation, thread id: " + thread_id);
		openConvo(thread_id);
	}

	private void fillInbox() {
		Log.v(this.toString(), "Filling inbox from database...");
		
		Cursor c = dbHelper.fetchAllThreads();
		startManagingCursor(c);

		String[] from = new String[] { SmsDbAdapter.KEY_ADDRESS };

		int[] to = new int[] { R.id.list_entry };

		SimpleCursorAdapter convos = new SimpleCursorAdapter(this,
				R.layout.list_item, c, from, to);
		setListAdapter(convos);
	}

	/**
	 * Open a conversation.
	 * 
	 * @param id
	 *            the thread_id of the conversation being opened
	 */
	private void openConvo(long id) {
		Intent threadIntent = new Intent();
		threadIntent.setClass(this, MainChatWindow.class);
		threadIntent.putExtra(MainChatWindow.THREAD_ID, (int) id);
		startActivity(threadIntent);
	}
	
	/**
	 * Delete a conversation.
	 * @param id the thread_id of the conversation to delete
	 * @return true if deleted, otherwise false
	 */
	private boolean deleteConvo(long id) {
		boolean deleted = dbHelper.deleteThread(id);
		fillInbox();
		return deleted;
	}

	private class ComposeClickListener implements OnClickListener {

		public void onClick(View arg0) {
			openConvo(-1);
		}

	}

}
