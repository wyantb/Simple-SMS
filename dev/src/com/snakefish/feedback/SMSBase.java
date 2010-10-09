package com.snakefish.feedback;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

public interface SMSBase extends TextToSpeech.OnInitListener {

	public void speak(String text, boolean doFlush);
	public void speak(String text);

	public boolean onSearchRequested();

	public void onActivityResult(int requestCode, int resultCode, Intent data);
	
	public void setHidden(Object o);
	
	public List<CommandAction> commandsRequested();
	public String processCommands(String text);
	
	public Object onRetainNonConfigurationInstance();
	public void onCreate(Bundle savedInstanceState);
	public void onStart();
	public void onResume();
	public void onPause();
	public void onStop();
	public void onDestroy();
	public void onRestart();
	
}
