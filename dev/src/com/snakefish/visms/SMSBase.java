package com.snakefish.visms;

import java.util.List;

import android.content.Intent;
import android.speech.tts.TextToSpeech;

public interface SMSBase extends TextToSpeech.OnInitListener {

	public void speak(String text);

	public boolean onSearchRequested();

	public void onActivityResult(int requestCode, int resultCode, Intent data);

	public void processVoice(List<String> command);
	public void processVoice(String altMatched);
	
}
