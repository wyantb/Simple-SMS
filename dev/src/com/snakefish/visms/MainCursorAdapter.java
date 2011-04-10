package com.snakefish.visms;

import com.snakefish.db.SMSRecordHelper;
import com.snakefish.util.ContactNames;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MainCursorAdapter extends SimpleCursorAdapter {

	private Context context;
	
	public MainCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		
		super(context, layout, c, from, to);
		
		this.context = context;
	}
	
	public void bindView(View view, Context context, Cursor cursor) {
		super.bindView(view, context, cursor);
		
		ThreadTextView threadView = (ThreadTextView)view;
		threadView.setThreadId(SMSRecordHelper.getThreadId(cursor));
	}
	
	public void setViewText(TextView t, String text) {
		String toDisplay = ContactNames.get().getDisplayName(context, text);
		
		t.setText(toDisplay);
	}

}
