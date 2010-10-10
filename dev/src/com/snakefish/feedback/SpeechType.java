package com.snakefish.feedback;

public enum SpeechType {

	/**
	 * A basic intro message, such as "Message Screen"
	 */
	INTRO,
	
	/**
	 * A lengthy tutorial message.  See 'tutorial' in any *speech.xml
	 */
	TUTORIAL,
	
	/**
	 * See description for TUTORIAL.  However, the user has requested this,
	 *   so it should pass through.
	 */
	TUTORIAL_REQUESTED,
	
	/**
	 * Just basic information for the user.  Should likely always pass through.
	 */
	INFO,
	
	/**
	 * Speech that will have personal data, such as conversation messages
	 */
	PERSONAL;
	
}
