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
	
	// CUSTOM COMMANDS
	COMPOSE("compose(.*)", true),   // 'snakefish compose Brian Wyant'
	VIEW("view(.*)", true),         // 'snakefish view Brian Wyant'
	REPLY("reply(.*)", true),       // 'snakefish reply hey what's up'
	READ("read", true),             // 'snakefish read'
	SEND("send(.*)", true),         // 'snakefish send hey what's up'
	IGNORE("ignore( message)*", true),         // 'snakefish ignore'
	// END CUSTOM COMMANDS
	
	// Keep unrecognized before text!  Important that we catch this case
	UNRECOGNIZED(".*", true, true),       // 'snakefish ???'
	TEXT(".*", false);              // 'seriously, just about anything'

	private String regex;    // With 'snakefish '
	private String regexAlt; // Without 'snakefish '
	private boolean forceAlt;
	private boolean forceKeyword = false;
	
	private static final String TAG = "CommandAction";
	
	private CommandAction(String regex, boolean hasKeyword) {
		this.regex = VoiceCommand.SNAKEFISH_KEYWORD + regex;
		this.regexAlt = regex;
		
		forceAlt = hasKeyword;
	}
	
	private CommandAction(String regex, boolean hasKeyword, boolean keywordOnly) {
		this.regex = VoiceCommand.SNAKEFISH_KEYWORD + regex;
		this.regexAlt = regex;
		
		forceAlt = !hasKeyword;
		forceKeyword = keywordOnly;
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
				return new VoiceCommand(command, mat, matAlt, matchesMain, matchesAlt);
			}
		}
		
		return new VoiceCommand(UNRECOGNIZED);
	}
	
}
