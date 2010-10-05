package com.snakefish.visms;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import com.snakefish.feedback.SMSFeedback;
import com.snakefish.feedback.SpeechPack;
import com.snakefish.feedback.VoiceFeedback;

import android.content.Context;
import android.speech.tts.TextToSpeech;

public class SMSDelegate implements SMSBase {

	private List<SMSFeedback> toPlay;
	private TextToSpeech tts;
	private boolean isReady = false;
	private SpeechPack speechPack;
	
	
	
	public SMSDelegate(Context context, int xmlResId) {
		tts = new TextToSpeech(context, this);
		
		speechPack = new SpeechPack(context, xmlResId);
		
		tts.setLanguage(Locale.US);
		toPlay = new LinkedList<SMSFeedback>();
	}
	
	public void onInit(int arg0) {
		isReady = true;
		
		String tut = speechPack.getTutorial();
		
		if (tut != null) {
			tts.speak(tut, TextToSpeech.QUEUE_FLUSH, null);
		}
		
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
