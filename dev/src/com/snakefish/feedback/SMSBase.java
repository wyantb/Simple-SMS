package com.snakefish.feedback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

/**
 * All the work that can be delegated to SMSDelegate.
 * It is intended that our custom base Activity classes
 *  (SMSActivity, SMSListActivity, ...) implement this
 *  interface, but pass all the work along to SMSDelegate.
 * 
 * <br><br>
 * Note: setHidden(Object) is used so that we don't repeat
 *  speech every time the user rotates their screen, pull
 *  out the keyboard, etc.
 * 
 * @author Brian
 *
 */
public interface SMSBase extends TextToSpeech.OnInitListener {

	public void speak(String text, SpeechType type, boolean doFlush);
	public void speak(String text, SpeechType type);

	public boolean onSearchRequested();

	public void onActivityResult(int requestCode, int resultCode, Intent data);
	
	public void setHidden(Object o);
	
	public Object onRetainNonConfigurationInstance();
	
	public void finishFromChild(Activity child);
	
	public void onCreate(Bundle savedInstanceState);
	public void onStart();
	public void onResume();
	public void onPause();
	public void onStop();
	public void onDestroy();
	public void onRestart();
	
}
