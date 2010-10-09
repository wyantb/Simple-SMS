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
	 * By definition, I don't know what this would be.
	 */
	INFO,
	
	/**
	 * Speech that will have personal data, such as conversation messages
	 */
	PERSONAL;
	
}
