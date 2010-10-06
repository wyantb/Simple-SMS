package com.snakefish.visms;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import com.snakefish.feedback.SMSFeedback;
import com.snakefish.feedback.SpeechPack;
import com.snakefish.feedback.VoiceFeedback;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;

public class SMSDelegate implements SMSBase {

	private static final int VOICE_REQUEST_CODE = 1234112;
	
	private List<SMSFeedback> toPlay;
	private TextToSpeech tts;
	private boolean isReady = false;
	private SpeechPack speechPack;
	private Activity callback;
	private SMSBase smsCallback;
	
	public SMSDelegate(SMSBase callback, Activity context, int xmlResId) {
		this.tts = new TextToSpeech(context, this);
		this.smsCallback = callback;
		this.callback = context;
		
		speechPack = new SpeechPack(context, xmlResId);
		
		tts.setLanguage(Locale.US);
		toPlay = new LinkedList<SMSFeedback>();
	}
	
	public boolean onSearchRequested() {
		PackageManager pm = callback.getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities(
				new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		
		if (activities.size() != 0) {
			Intent voiceIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			voiceIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, 
					RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			voiceIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "State a voice command");
			callback.startActivityForResult(voiceIntent, VOICE_REQUEST_CODE);
		}
    	
    	return true;
	}
	
	public void onInit(int arg0) {
		isReady = true;
		
		String tut = speechPack.getIntro();
		
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

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == VOICE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
			List<String> matched = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			String altMatched = data.getDataString();
			
			smsCallback.processVoice(matched);
			smsCallback.processVoice(altMatched);
		}
	}

	public void processVoice(List<String> command) {}
	public void processVoice(String altMatched) {}
	
	

}
