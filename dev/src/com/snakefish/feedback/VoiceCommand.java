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
	private Matcher matcherMain;
	private Matcher matcherAlt;
	private boolean matchesMain;
	private boolean matchesAlt;
	private int textGroup;
	
	public VoiceCommand(CommandAction command, Matcher matcher, Matcher matcherAlt, boolean matchesMain, boolean matchesAlt, int textGroup) {
		this.command = command;
		
		this.matcherMain = matcher;
		this.matcherAlt = matcherAlt;
		
		this.matchesMain = matchesMain;
		this.matchesAlt = matchesAlt;
		
		this.textGroup = textGroup;	
	}
	
	public VoiceCommand(CommandAction command, Matcher matcher, Matcher matcherAlt, boolean matchesMain, boolean matchesAlt) {
		this(command, matcher, matcherAlt, matchesMain, matchesAlt, -1);
	}
	
	public VoiceCommand(CommandAction action) {
		this(action, null, null, false, false);
	}
	
	public CommandAction getType() {
		return command;
	}
	
	public Matcher getMatcher() {
		return matcherMain;
	}
	
	public Matcher getAltMatcher() {
		return matcherAlt;
	}
	
	public boolean matchesMain() {
		return matchesMain;
	}
	
	public boolean matchesAlt() {
		return matchesAlt;
	}
	
	public String getTextGroup() {
		if (textGroup != -1) {
			return getRawGroup(textGroup);
		}
		else {
			return "";
		}
	}
	
	public String getRawGroup(int i) {
		if (matchesMain && matcherMain != null) {
			return matcherMain.group(i);
		}
		else if (matchesAlt && matcherAlt != null) {
			return matcherAlt.group(i);
		}
		
		return null;
	}
	
}
