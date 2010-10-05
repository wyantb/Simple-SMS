package com.snakefish.visms;

import android.speech.tts.TextToSpeech;

public interface SMSBase extends TextToSpeech.OnInitListener {

	public void speak(String text);
	
}
