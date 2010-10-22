package com.snakefish.visms;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.snakefish.feedback.CommandAction;
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
	protected Button btnHeadphones;

	/** Button used to determine font size */
	protected Button btnFontSize;

	/** The button used to determine font color */
	protected Button btnFontColor;

	/** The back button */
	protected Button btnBack;

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

		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item,
				OPTIONS));
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

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// When clicked, show a toast with the TextView text
		Toast.makeText(getApplicationContext(), ((TextView) v).getText(),
				Toast.LENGTH_SHORT).show();
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
			}
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
	private class FontColorListener implements OnClickListener {

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

	/**
	 * The listener for the back button in the program. Will return listener to
	 * the previous page they were at.
	 * 
	 * @author Team Snakefish
	 * 
	 */
	private class BackListener implements OnClickListener {

		/**
		 * The onClick method used for the listener
		 */
		public void onClick(View v) {
			// Calls the doWork method
			doWork();
		}

		/**
		 * Returns the user to the original point that they were at in the
		 * application.
		 */
		public void doWork() {
			// TODO Needs to go back to the previous page, whichever that may be
		}
	}
}
