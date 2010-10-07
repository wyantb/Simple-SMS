package com.snakefish.feedback;

import android.content.Intent;
import android.speech.tts.TextToSpeech;

public interface SMSBase extends TextToSpeech.OnInitListener {

	public void speak(String text);

	public boolean onSearchRequested();

	public void onActivityResult(int requestCode, int resultCode, Intent data);
	
	public void onResume();
	public void onPause();
	public void onStop();
	public void onDestroy();
	
}
