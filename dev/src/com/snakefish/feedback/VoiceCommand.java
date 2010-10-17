package com.snakefish.feedback;

import java.util.regex.Matcher;

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
	
	public String getGroup(int i) {
		if (matched != null) {
			return matched.group(i);
		}
		
		return null;
	}
	
}
