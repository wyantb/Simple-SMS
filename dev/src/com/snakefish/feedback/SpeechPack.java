package com.snakefish.feedback;

import android.content.Context;
import android.content.res.XmlResourceParser;

/**
 * Given an xml resource, parses the file (using an
 *  XmlPullParser), looking for the data of (currently)
 *  three tags: intro, tutorial, and list.
 * These are the screen dependent packs of speech that
 *  we use for various things.  Intro is used, for example,
 *  every time the user enters a screen.  Tutorial can be
 *  spoken automatically based on options, or when the user
 *  commands CommandAction.HELP.  List is only read when the
 *  user command CommandAction.LIST.
 * If this class fails, the xml file it loads doesn't have the
 *  data that we need.
 *
 */
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
