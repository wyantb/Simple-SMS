package com.snakefish.visms;

import android.app.ListActivity;
import android.os.Bundle;

public class SMSListActivity extends ListActivity implements SMSBase {

	private SMSBase delegate;
	
	public SMSListActivity() {
		super();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		delegate = new SMSDelegate(this);
	}
	
	public void onInit(int arg0) {
		if (delegate != null) {
			delegate.onInit(arg0);
		}
	}
	
	public void speak(String text) {
		if (delegate != null) {
			delegate.speak(text);
		}
	}

}
