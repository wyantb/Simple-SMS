package com.snakefish.visms;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class ThreadTextView extends TextView {

	private int threadId;
	
	public ThreadTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setThreadId(int threadId) {
		this.threadId = threadId;
	}
	
	public int getThreadId() {
		return threadId;
	}

}
