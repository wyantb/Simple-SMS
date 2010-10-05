package com.snakefish.visms;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import com.snakefish.feedback.SMSFeedback;
import com.snakefish.feedback.VoiceFeedback;

import android.content.Context;
import android.speech.tts.TextToSpeech;

public class SMSDelegate implements SMSBase {

	private List<SMSFeedback> toPlay;
	private TextToSpeech tts;
	private boolean isReady = false;
	
	public SMSDelegate(Context context) {
		tts = new TextToSpeech(context, this);
		tts.setLanguage(Locale.US);
		toPlay = new LinkedList<SMSFeedback>();
	}
	
	public void onInit(int arg0) {
		isReady = true;
		
		for (SMSFeedback fb : toPlay) {
			fb.play();
		}
	}

	public void speak(String text) {
		if (isReady) {
			tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
		}
		else {
			toPlay.add(new VoiceFeedback(tts, text));
		}
	}
	
	

}
