package com.snakefish.visms;

import com.snakefish.feedback.SMSBase;
import com.snakefish.feedback.SMSDelegate;
import com.snakefish.feedback.SMSDelegateCallback;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;

public abstract class SMSListActivity extends ListActivity implements SMSBase, SMSDelegateCallback  {

	private SMSBase delegate;
	private int xmlResId;
	
	public SMSListActivity(int xmlResId) {
		super();
		
		this.xmlResId = xmlResId;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		delegate = new SMSDelegate(this, this, xmlResId);
		delegate.onCreate(savedInstanceState);
	}
    
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
	
	@Override
	public void onStart() {
		if (delegate != null) {
			delegate.onStart();
		}
		
		super.onStart();
	}
	
	@Override
	public void onResume() {
		if (delegate != null) {
			delegate.onResume();
		}

		super.onResume();
	}
	
	@Override
	public void onPause() {
		if (delegate != null) {
			delegate.onPause();
		}
		
		super.onPause();
	}
	
	@Override
	public void onStop() {
		if (delegate != null) {
			delegate.onStop();
		}
		
		super.onStop();
	}

	@Override
	public void onDestroy() {
		if (delegate != null) {
			delegate.onDestroy();
		}
		
		super.onDestroy();
	}

}
