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

public class SMSDelegate implements SMSBase {

	public static String SNAKEFISH_KEYWORD = "snakefish";
	
	private static final int VOICE_REQUEST_CODE = 1234112;
	
	private TextToSpeech tts;
	private SpeechPack speechPack;
	private Activity callback;
	private SMSDelegateCallback smsCallback;
	private List<String> queuedMessages;
	private boolean isHidden;
	private List<CommandAction> commandsRequested;
	
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
		
		synchronized (queuedMessages) {
			for (Iterator<String> msgItr = queuedMessages.iterator();
				msgItr.hasNext(); ) {
				
				int result = tts.speak(msgItr.next(), TextToSpeech.QUEUE_ADD, null);
				
				if (result == TextToSpeech.SUCCESS) {
					msgItr.remove();
				}
			}
		}
		
	}

	public void speak(String text, boolean doFlush) {
		if (doFlush) {
			tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
		}
		else {
			tts.speak(text, TextToSpeech.QUEUE_ADD, null);
		}
	}
	
	public void speak(String text) {
		speak(text, false);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == VOICE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
			List<String> matched = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			
			String spokenWords = matched.get(0);
			spokenWords = processCommands(spokenWords);
			smsCallback.processVoice(spokenWords);
		}
	}
	
	public List<CommandAction> commandsRequested() {
		return commandsRequested;
	}
	
	public String processCommands(String userText) {
		commandsRequested = new ArrayList<CommandAction>();
		
		Scanner textParser = new Scanner(userText);
		String remainingSpeech = "";
		
		while (textParser.hasNext()) {
			String textPart = textParser.next();
			
			if (textPart.equalsIgnoreCase(SNAKEFISH_KEYWORD)) {
				String commandPart = textParser.next();
				
				CommandAction command = CommandAction.fromString(commandPart);
				
				if (command == CommandAction.HELP || command == CommandAction.LIST) {
					speak(speechPack.getTutorial(), true);
				}
				else {
					commandsRequested.add(CommandAction.fromString(commandPart));
				}
			}
			else {
				remainingSpeech += textPart;
			}
		}
		
		return remainingSpeech;
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
		isHidden = true;
	}
	
	public void onResume() {
	}
	
	public void onPause() {
	}
	
	public void onStop() {
		tts.stop();
		
		isHidden = true;
	}
	
	public void onDestroy() {
		tts.shutdown();
	}
	
	private void queueMesssageOnInit(String message) {
		synchronized (queuedMessages) {
			queuedMessages.add(message);
		}
	}
}
