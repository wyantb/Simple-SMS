package com.snakefish.visms;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SMSActivity extends Activity implements SMSBase {

	private SMSBase delegate;
	private int xmlResId;
	
	public SMSActivity(int xmlResId) {
		super();
		
		this.xmlResId = xmlResId;
	}
	
	// --- UNUSED COMMANDS ---
	public void processVoice(List<String> command) {}
	public void processVoice(String altMatched) {}
    
	/**
	 * Something of a subversion: if they click search, we accept voice input.
	 */
    @Override
    public boolean onSearchRequested() {
    	if (delegate != null) {
    		return delegate.onSearchRequested();
    	}
    	
    	return false;
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (delegate != null) {
    		delegate.onActivityResult(requestCode, resultCode, data);
    	}
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		delegate = new SMSDelegate(this, this, xmlResId);
		
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
