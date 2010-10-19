package com.snakefish.feedback;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

public enum CommandAction {
	
	// OPTIONS
	HEADPHONES_ON("headphones (required|on|yes)", true),
	HEADPHONES_OFF("headphones (not required|off|no)", true),
	TEXT_UP("text (increase|up)", true),
	TEXT_DOWN("text (decrease|down)", true),
	OPTIONS("options( menu)*", true),
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
	IGNORE("ignore", true),         // 'snakefish ignore'
	// END CUSTOM COMMANDS
	
	// Keep unrecognized before text!  Important that we catch this case
	UNRECOGNIZED(".*", true),       // 'snakefish ???'
	TEXT(".*", false);              // 'seriously, just about anything'

	private String regex;
	
	private static final String TAG = "CommandAction";
	
	private CommandAction(String regex, boolean hasKeyword) {
		if (hasKeyword) {
			this.regex = VoiceCommand.SNAKEFISH_KEYWORD + regex;
		}
		else {
			this.regex = regex;
		}
	}
	
	public String regex() {
		return regex;
	}
	
	public String toString() {
		return regex();
	}
	
	public static VoiceCommand fromString(String str) {
		Log.v(TAG, "Checking against string: " + str);
		
		for (CommandAction command : CommandAction.values()) {
			Pattern pat = Pattern.compile(command.regex());
			Matcher mat = pat.matcher(str);
			
			if (mat.matches()) {
				Log.v(TAG, "Found match for pattern: " + command);
				return new VoiceCommand(command, mat);
			}
		}
		
		return new VoiceCommand(UNRECOGNIZED);
	}
	
}
