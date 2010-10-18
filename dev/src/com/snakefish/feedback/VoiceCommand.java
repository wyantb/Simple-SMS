package com.snakefish.feedback;

import java.util.regex.Matcher;

/**
 * Is a thin wrapper around two objects:
 *  First, the CommandAction that was matched when the user spoke something.
 *  Second, the Matcher for the regex that matched.
 * With this data, we can get specific parts of what they stated,
 *  or the raw mapper.
 * Be careful because we can't have named groups, only group numbers.
 */
public class VoiceCommand {

	public static String SNAKEFISH_KEYWORD = "snakefish ";
	
	private CommandAction command;
	private Matcher matched;
	
	public VoiceCommand(CommandAction command, Matcher matcher) {
		this.command = command;
		this.matched = matcher;
	}
	
	public VoiceCommand(CommandAction action) {
		this(action, null);
	}
	
	public CommandAction getType() {
		return command;
	}
	
	public Matcher getMatcher() {
		return matched;
	}
	
	public String getGroup(int i) {
		if (matched != null) {
			return matched.group(i);
		}
		
		return null;
	}
	
}
