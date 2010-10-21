package com.snakefish.feedback;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;

public class SMSDelegate implements SMSBase {

	public static String SNAKEFISH_KEYWORD = "snakefish";
	
	private static final int VOICE_REQUEST_CODE = 1234112;
	
	@Deprecated
	public static boolean HEADPHONES_REQUIRED = false;
	
	private TextToSpeech tts;
	private SpeechPack speechPack;
	private Activity callback;
	private SMSDelegateCallback smsCallback;
	private List<String> queuedMessages;
	private boolean isHidden;
	
	public SMSDelegate(SMSDelegateCallback callback, Activity context, int xmlResId) {
		this.tts = new TextToSpeech(context, this);
		this.smsCallback = callback;
		this.callback = context;
		
		speechPack = new SpeechPack(context, xmlResId);
		
		tts.setLanguage(Locale.US);
		tts.setSpeechRate(4);
		tts.speak("", TextToSpeech.QUEUE_FLUSH, null);
		
		queuedMessages = new ArrayList<String>();
		isHidden = true;
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
		
		// TODO Uncomment the following lines to re-enable TTS
		synchronized (queuedMessages) {
			for (Iterator<String> msgItr = queuedMessages.iterator();
				msgItr.hasNext(); ) {
				
				int result = tts.speak(msgItr.next(), TextToSpeech.QUEUE_FLUSH, null);
				
				if (result == TextToSpeech.SUCCESS) {
					msgItr.remove();
				}
			}
		}
		
	}

	public void speak(String text, SpeechType type, boolean doFlush) {
		if (type == SpeechType.PERSONAL && HEADPHONES_REQUIRED) {
			return;
		}
		
		if (doFlush) {
			tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
		}
		else {
			tts.speak(text, TextToSpeech.QUEUE_ADD, null);
		}
	}
	
	public void speak(String text, SpeechType type) {
		speak(text, type, false);
	}
	
	public void speak(String text) {
		speak(text, SpeechType.PERSONAL, false);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == VOICE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
			List<String> matched = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			
			String spokenWords = matched.get(0);
			Log.v("SMSDelegate", "Received speech: " + spokenWords);
			VoiceCommand command = CommandAction.fromString(spokenWords);
			boolean handled = handleCommand(command);
			
			if (!handled) {
				smsCallback.processVoice(command);
			}
		}
		else if (requestCode == Activity.RESULT_OK || requestCode == Activity.RESULT_FIRST_USER) {
			speak(speechPack.getIntro(), SpeechType.INTRO, true);
		}
	}
	
	public boolean handleCommand(VoiceCommand command) {
		if (command.getType() == CommandAction.HELP) {
			speak(speechPack.getTutorial(), SpeechType.TUTORIAL_REQUESTED, true);
			return true;
		}
		if (command.getType() == CommandAction.LIST) {
			speak(speechPack.getList(), SpeechType.TUTORIAL_REQUESTED, true);
			return true;
		}
		if (command.getType() == CommandAction.UNRECOGNIZED) {
			speak("Unrecognized command, please try again.", SpeechType.INFO, false);
			return true;
		}
		if (command.getType() == CommandAction.HEADPHONES_TOGGLE) {
			// TODO handle
		}
		if (command.getType() == CommandAction.HEADPHONES_OFF) {
			// TODO handle
		}
		if (command.getType() == CommandAction.HEADPHONES_ON) {
			// TODO handle
		}
		if (command.getType() == CommandAction.TEXT_DOWN) {
			// TODO handle
		}
		if (command.getType() == CommandAction.TEXT_UP) {
			// TODO handle
		}
		if (command.getType() == CommandAction.OPTIONS) {
			Intent optionsIntent = new Intent();
			optionsIntent.setClassName("com.snakefish.visms", 
					"com.snakefish.visms.OptionsList");
			callback.startActivity(optionsIntent);
			
			return true;
		}
		
		return false;
	}

	public Object onRetainNonConfigurationInstance() {
		isHidden = false;
		
		return isHidden;
	}
	
	public void setHidden(Object o) {
		if (o != null && o instanceof Boolean) {
			isHidden = (Boolean)o;
		}
	}
	
	public void onCreate(Bundle savedInstanceState) {
		if (isHidden) {
			queueMesssageOnInit(speechPack.getIntro());
			
			isHidden = false;
		}
	}
	
	public void onStart() {
	}
	
	public void onRestart() {
		tts = new TextToSpeech(callback, this);
		isHidden = true;
	}
	
	public void onResume() {
	}
	
	public void onPause() {
	}
	
	public void onStop() {
		tts.shutdown();
		
		isHidden = true;
	}
	
	public void onDestroy() {
	}
	
	private void queueMesssageOnInit(String message) {
		synchronized (queuedMessages) {
			queuedMessages.add(message);
		}
	}
}
