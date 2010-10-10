package com.snakefish.feedback;

import java.util.List;

public interface SMSDelegateCallback {

	public void processVoice(List<CommandAction> commands, String text);
	
}
