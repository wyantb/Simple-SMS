package com.snakefish.visms;

import com.snakefish.util.ContactNames;

import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MainCursorAdapter extends SimpleCursorAdapter {

	private Context context;
	
	public MainCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		
		super(context, layout, c, from, to);
		
		this.context = context;
	}
	
	public void setViewText(TextView t, String text) {
		String toDisplay = ContactNames.get().getDisplayName(context, text);
		
		t.setText(toDisplay);
	}

}
