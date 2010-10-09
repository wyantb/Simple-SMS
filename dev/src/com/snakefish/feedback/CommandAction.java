package com.snakefish.feedback;

public enum CommandAction {

	READ("read"),
	SEND("send"),
	IGNORE("ignore"),
	HELP("help"),
	LIST("list"),
	NULL("null");
	
	private String command;
	
	private CommandAction(String command) {
		this.command = command;
	}
	
	public String getCommandString() {
		return command;
	}
	
	public static CommandAction fromString(String str) {
		for (CommandAction command : CommandAction.values()) {
			if (command.getCommandString().equals(str)) {
				return command;
			}
		}
		
		return null;
	}
	
}
