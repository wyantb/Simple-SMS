package com.snakefish.feedback;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

public enum CommandAction {
	
	// OPTIONS
	HEADPHONES_TOGGLE("headphones", true),
	HEADPHONES_ON("headphones (required|on|yes)", true),
	HEADPHONES_OFF("headphones (not required|off|no)", true),
	TEXT_UP("text (increase|up)", true),
	TEXT_DOWN("text (decrease|down)", true),
	OPTIONS("((options( menu)*)|(settings( menu)*))", true),
	// END OPTIONS
	
	// GLOBAL COMMANDS
	HELP("help", true),             // 'snakefish help'
	LIST("list", true),             // 'snakefish list'
	// END GLOBAL COMMANDS

	//---------------------
	// CUSTOM COMMANDS
	//---------------------
	
	// 'snakefish compose Brian Wyant'
	COMPOSE("compose\\s?(.*)", true, 1),
	
	// 'snakefish view Brian Wyant'
	VIEW("view\\s?(.*)", true, 1),
	
	// 'snakefish reply hey what's up'
	REPLY("(reply|respond)\\s?(.*)", true, 2),
	
	// 'snakefish read'
	READ("(read|speak|say|open|display|repeat|play)\\s?(.*)", true, 2),

	// 'snakefish send hey what's up'
	SEND("send\\s?(.*)", true, 1),
	
	// 'snakefish ignore'
	IGNORE("(ignore|discard|delete)\\s?(message)*", true),
	
	//---------------------
	// END CUSTOM COMMANDS
	//---------------------
	
	// Keep unrecognized before text!  Important that we catch this case
	UNRECOGNIZED(".*", true, true),       // 'snakefish ???'
	TEXT(".*", false);              // 'seriously, just about anything'

	private String regex;    // With 'snakefish '
	private String regexAlt; // Without 'snakefish '
	private boolean forceAlt;
	private boolean forceKeyword = false;
	private int textGroup = -1;
	
	private static final String TAG = "CommandAction";
	
	private CommandAction(String regex, boolean hasKeyword) {
		this.regex = VoiceCommand.SNAKEFISH_KEYWORD + regex;
		this.regexAlt = regex;
		
		this.forceAlt = !hasKeyword;
	}
	
	private CommandAction(String regex, boolean hasKeyword, int textGroup) {
		this.regex = VoiceCommand.SNAKEFISH_KEYWORD + regex;
		this.regexAlt = regex;
		
		this.forceAlt = !hasKeyword;
		
		this.textGroup = textGroup;
	}
	
	private CommandAction(String regex, boolean hasKeyword, boolean keywordOnly) {
		this.regex = VoiceCommand.SNAKEFISH_KEYWORD + regex;
		this.regexAlt = regex;
		
		this.forceAlt = !hasKeyword;
		this.forceKeyword = keywordOnly;
	}
	
	public String regex() {
		return regex;
	}
	
	public String regexAlt() {
		return regexAlt;
	}
	
	public String toString() {
		return regex();
	}
	
	public static VoiceCommand fromString(String str) {
		Log.v(TAG, "Checking against string: " + str);
		
		for (CommandAction command : CommandAction.values()) {
			Pattern pat = Pattern.compile(command.regex());
			Matcher mat = pat.matcher(str);
			
			boolean matchesMain = mat.matches();
			if (matchesMain) {
				Log.v(TAG, "Found match for user command: " + command.regex());
			}
			
			Pattern patAlt = Pattern.compile(command.regexAlt());
			Matcher matAlt = patAlt.matcher(str);
			
			boolean matchesAlt = matAlt.matches();
			if (matchesAlt) {
				Log.v(TAG, "Found match for user command: " + command.regexAlt());
			}
			
			// TODO
			// Integrate the matchesAlt with that.
			// It's useful information.
			if ((matchesMain && !command.forceAlt) || (matchesAlt && !command.forceKeyword)) {
				return new VoiceCommand(command, mat, matAlt, matchesMain, matchesAlt, command.textGroup);
			}
		}
		
		return new VoiceCommand(UNRECOGNIZED);
	}
	
}
