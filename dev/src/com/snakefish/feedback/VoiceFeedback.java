package com.snakefish.feedback;

import android.speech.tts.TextToSpeech;

public class VoiceFeedback implements SMSFeedback {

	private TextToSpeech tts;
	private String text;
	
	public VoiceFeedback(TextToSpeech tts, String text) {
		this.tts = tts;
		this.text = text;
	}
	
	public void play() {
		tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	}
	
}
