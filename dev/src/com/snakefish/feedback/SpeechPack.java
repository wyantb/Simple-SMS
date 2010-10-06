package com.snakefish.feedback;

import android.content.Context;
import android.content.res.XmlResourceParser;

public class SpeechPack {

	private XmlResourceParser parser;
	private String intro;
	private String tutorial;
	private String list;
	
	public SpeechPack(Context context, int xmlResId) {
		parser = context.getResources().getXml(xmlResId);
		
		try {
			parser.next();                // BEGIN_DOC
			parser.nextTag();             // BEGIN TAG RESOURCES
			parser.nextTag();             // BEGIN TAG ITEM (intro)
			
			intro = parser.nextText();    // END TAG ITEM (intro)
			
			parser.nextTag();             // BEGIN TAG ITEM (tutorial)
			
			tutorial = parser.nextText(); // END TAG ITEM (tutorial)
			
			parser.nextTag();             // BEGIN TAG ITEM (list)
			
			list = parser.nextText();
		}
		catch (Exception e) {
			// TODO better errors
			throw new RuntimeException("--Brian Wyant.  You didn't add in the correct tags to your text XML.  Or I messed up. See SpeechPack constructor.");
		}
	}

	public String getTutorial() {
		return tutorial;
	}
	
	public String getIntro() {
		return intro;
	}
	
	public String getList() {
		return list;
	}

}
