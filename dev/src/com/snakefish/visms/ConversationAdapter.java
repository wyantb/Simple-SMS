package com.snakefish.visms;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.database.Cursor;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class ConversationAdapter extends CursorAdapter {

	private Context parentContext;
	public static LayoutInflater layoutInflater;
	
	public ConversationAdapter(Context context, Cursor c) {
		super(context, c);
		
		parentContext = context;
		layoutInflater = (LayoutInflater)parentContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		int bodyColumn = cursor.getColumnIndex(SmsDbAdapter.KEY_BODY);
		String bodyValue = cursor.getString(bodyColumn);
		
		((TextView)view).setText(bodyValue);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup vg) {
		int fromColumn = cursor.getColumnIndex(SmsDbAdapter.KEY_PERSON);
		int fromValue = cursor.getInt(fromColumn);
		
		int bodyColumn = cursor.getColumnIndex(SmsDbAdapter.KEY_BODY);
		String bodyValue = cursor.getString(bodyColumn);
		
		// If sent by me...
		if (fromValue == -1) {
			TextView newView = (TextView)layoutInflater.inflate(R.layout.conversation_me, null);
			newView.setText(bodyValue);
			return newView;
		}
		
		// If sent by you...
		else {
			TextView newView = (TextView)layoutInflater.inflate(R.layout.conversation_you, null);
			newView.setText(bodyValue);
			return newView;
		}
	}
}
