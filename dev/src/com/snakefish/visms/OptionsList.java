package com.snakefish.visms;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.snakefish.feedback.CommandAction;
import com.snakefish.feedback.SMSDelegate;
import com.snakefish.feedback.SpeechType;
import com.snakefish.feedback.VoiceCommand;

/**
 * This class will be used to handle all of the options in the application.
 * These options will let the user change things such as font size, headphone
 * options, font color, ect.
 * 
 * @author Team Snakefish
 * 
 */
public class OptionsList extends SMSListActivity {

	/** Determines if the headphones are set or not */
	protected boolean headphones = false;

	/** The button used to determine headphone options */
	//protected CheckBox btnHeadphones;
	protected Button btnHeadphones;

	/** Button used to determine font size */
	protected Button btnFontUp;

	/** The button used to determine font color */
	protected Button btnFontDown;

	/** The back button */
	protected Button btnColorScheme;
	
	/** The button array for the list item */
	protected Button[] buttonList;

	/**
	 * The constructor used for the options list
	 */
	public OptionsList() {
		super(R.xml.options_speech);
	}

	/** The array used to store each of the options we will be using */
	static final String[] OPTIONS = { "Headphones", "Text Size +",
			"Text Size -", "Color Scheme" };

	/**
	 * Creates the options screen. The Android on create method used for all
	 * screen changes
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options_screen);

		//Creates the new buttons for the list
		//btnHeadphones = (CheckBox) findViewById(R.id.btnHeadPhones);
		btnHeadphones = (Button) findViewById(R.id.btnHeadphones);
		btnFontUp = (Button) findViewById(R.id.btnFontUp);
		btnFontDown = (Button) findViewById(R.id.btnFontDown);
		btnColorScheme = (Button) findViewById(R.id.btnColorScheme);
		
		//Adds listeners to the buttons
		btnHeadphones.setOnClickListener(new HeadphonesListener());
		btnFontUp.setOnClickListener(new FontSizeListener(1));
		btnFontDown.setOnClickListener(new FontSizeListener(-1));
		btnColorScheme.setOnClickListener(new ColorListener());
		
		/*
		//Sets the CharSequence to a null value without using null
		CharSequence tempC = "";
		
		//Loops through and makes buttons with the same names as OPTIONS
		for(int x = 0; x < 4; x++){
			//Sets char sequence to buttonList[x]
			tempC.equals(buttonList[x]);
			//Sets the button name to the name of the text
			buttonList[x].setText(tempC);
			
			//The case statements assign each button to the right listener
			switch(x){
			//Headphones button
			case(0): 
				buttonList[x] = (Button) findViewById(R.id.btnHeadphones);
				buttonList[x].setOnClickListener(new HeadphonesListener());
				break;
			//Font size up
			case(1):
				buttonList[x] = (Button) findViewById(R.id.btnFontUp);
				buttonList[x].setOnClickListener(new FontSizeListener(1));
				break;
			//Font size down
			case(2):
				buttonList[x] = (Button) findViewById(R.id.btnFontDown);
				buttonList[x].setOnClickListener(new FontSizeListener(-1));
				break;
			//Color scheme
			case(3):
				buttonList[x] = (Button) findViewById(R.id.btnColorScheme);
				buttonList[x].setOnClickListener(new ColorListener());
				break;
			}
		}*/
		
		
		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item,
			new String[0]));
	}
	
	/**
	 * Method used to process voice commands for the current screen. Voice
	 * commands will consist allow users to edit each option and can be found in
	 * CommandAction.java.
	 */
	public boolean processVoice(VoiceCommand command) {
		// Already handled elsewhere
		
		return false;
	}

	/*@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		//Toast.makeText(getApplicationContext(), ((TextView) v).getText(),
				//Toast.LENGTH_SHORT).show();
	}*/

	/**
	 * Method used to turn off headphones
	 */
	public void headphones_off() {
		// TODO add functionality to turn off headphones
		speak("Headphones off", SpeechType.INFO, true);
	}

	/**
	 * Method used to turn on headphones
	 */
	public void headphones_on() {
		// TODO Add functionality to turn on headphones
		speak("Headphones on", SpeechType.INFO, true);
	}

	/**
	 * The headphones listener for the application. The listener will determine
	 * if the headphones are set to on or off. If they are off they will be
	 * turned on and if they are on they will be turned off. This will also
	 * effect the headphones button which will state if the headphones are on or
	 * off.
	 * 
	 * @author Team Snakefish
	 * 
	 */
	private class HeadphonesListener implements OnClickListener {

		/**
		 * Called when the button is clicked
		 */
		public void onClick(View v) {
			SMSDelegate.headphoneOption = !SMSDelegate.headphoneOption;
			
			if (SMSDelegate.headphoneOption) {
				speak("Headphones on", SpeechType.INFO, false);
			}
			else {
				speak("Headphones off", SpeechType.INFO, false);
			}
			
			Log.v("OptionsList", "Headphones toggled");
			
			// Calls the do work function
			doWork();
			
		}

		/**
		 * The doWork method for the headphones listener. Determines if the
		 * headphones option was set to true or false. If set to true once the
		 * button is clicked headphones will be set to false. If set to false
		 * once clicked the headphones option will be set to true.
		 */
		public void doWork() {
			/*
			// Headphones are turned off
			if (headphones == false) {
				// Headphones are turned on
				headphones = true;
				// Calls the headphones_on method
				headphones_on();
			} else {
				// Turns headphones off
				headphones = false;
				// Calls the headphones_off method
				headphones_off();
			} */
		}

		/**
		 * Method used to turn off headphones
		 */
		public void headphones_off() {
			// TODO add functionality to turn off headphones
		}

		/**
		 * Method used to turn on headphones
		 */
		public void headphones_on() {
			// TODO Add functionality to turn on headphones
		}
	}

	/**
	 * The listener for the font size. This listener will be used to determine
	 * if the font size button has been clicked and make the changes according
	 * to what the users desires.
	 * 
	 * @author Team Snakefish
	 * 
	 */
	private class FontSizeListener implements OnClickListener {
		
		/** Determines if the font will be increased or decreased */
		private int size = 0;
		
		/** 
		 * Constructor. Negative numbers mean font decrease.
		 */
		FontSizeListener(int incDec){
			size = incDec;
		}
		
		/**
		 * The onClick method used for the listener.
		 * Calls the doWork method
		 */
		public void onClick(View v) {
			// Calls the doWork method
			doWork();
		}

		/**
		 * The doWork method for the font size listener.
		 * 
		 */
		public void doWork() {
			// TODO Should there be a pop up screen here or another option
		}
	}

	/**
	 * The listener for the font size. This listener will be used to determine
	 * if the font size button has been clicked and make the changes according
	 * to what the users desires.
	 * 
	 * @author Team Snakefish
	 * 
	 */
	private class ColorListener implements OnClickListener {

		/**
		 * The onClick method for the FontColorListener
		 */
		public void onClick(View v) {
			// Calls doWork method
			doWork();
		}

		/**
		 * The doWork method for the listener. Will determine what font color
		 * the user wishes to use
		 */
		public void doWork() {
			// TODO Same as FontSizeListener
		}

	}
}
