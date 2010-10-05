package com.snakefish.visms;

import android.app.Activity;
import android.os.Bundle;

public class SMSActivity extends Activity implements SMSBase {

	private SMSBase delegate;
	
	public SMSActivity() {
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
