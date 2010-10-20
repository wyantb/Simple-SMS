package com.snakefish.visms;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.snakefish.feedback.CommandAction;
import com.snakefish.feedback.VoiceCommand;

public class ConversationsActivity extends SMSListActivity {
	/** The user who you are communicating with */
	public static final String CONVERSATION_CONTACT = "com.snakefish.CONTACT";

	public static final int COMPOSE_ID = Menu.FIRST;
	public static final int SETTINGS_ID = Menu.FIRST + 1;
	public static final int OPEN_ID = Menu.FIRST + 2;
	public static final int DELETE_ID = Menu.FIRST + 3;
	private SmsDbAdapter mDbHelper;
	private TextView btnCompose;

	public ConversationsActivity() {
		super(R.xml.conv_speech);
	}

	public void processVoice(VoiceCommand command) {

		if (command.getType() == CommandAction.COMPOSE) {
			doCompose();
		}

	}

	public void doCompose() {
		// TODO do we want to compose with some other id?
		openConvo(-1);
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.convo_list);

		btnCompose = (TextView) findViewById(R.id.btnCompose);

		btnCompose.setOnClickListener(new ComposeClickListener());

		mDbHelper = new SmsDbAdapter(this);
		mDbHelper.open();

		// TODO remove this shit
		// **TESTING DATABASE, REMOVE WHEN DONE
		// Adding dummy conversations to db
		mDbHelper.deleteInbox();
		mDbHelper.addMsg(1, "1-570-400-0104", 1, 1286456244, "Yo what's up?");
		mDbHelper.addMsg(1, "1-570-400-0104", 1, 1286456844, "R u thar?");
		mDbHelper.addMsg(2, "1-203-733-8028", 2, 1286551147,
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
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, OPEN_ID, 0, R.string.open);
		menu.add(0, DELETE_ID, 0, R.string.delete);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case COMPOSE_ID:
			doCompose();
			return true;
		case SETTINGS_ID:
			Intent options = new Intent(this, OptionsList.class);
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
			// Intent i = new Intent(this, TextActivity.class);
			// i.putExtra(SmsDbAdapter.KEY_ROWID, info.id);
			// startActivity(i);
			TextView v = (TextView) this.findViewById((int) info.id);
			String address = String.valueOf(v.getText());
			long id = mDbHelper.getThreadId(address);

			openConvo(id);
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
		TextView tv = (TextView) v;
		String address = String.valueOf(tv.getText());
		long thread_id = mDbHelper.getThreadId(address);
		openConvo(thread_id);

	}

	private void fillInbox() {
		Cursor c = mDbHelper.fetchAllThreads();
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
		Intent thread = new Intent();
		thread.setClassName("com.snakefish.visms",
				"com.snakefish.visms.MainChatWindow");
		thread.putExtra(MainChatWindow.THREAD_ID, (int) id);
		startActivity(thread);
	}

	private class ComposeClickListener implements OnClickListener {

		public void onClick(View arg0) {
			doCompose();
		}

	}

}
