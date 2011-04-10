package com.snakefish.visms;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.snakefish.db.SMSDbAdapter;
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

	protected boolean headphones = false;
	
	// TODO convert to checkbox
	protected Button btnHeadphones;
	protected Button btnFontUp;
	protected Button btnFontDown;
	protected Button btnColorScheme;
	protected Button btnWipe;
	
	/** The button array for the list item */
	protected Button[] buttonList;
	
	protected SMSDbAdapter dbHelper;

	/**
	 * The constructor used for the options list
	 */
	public OptionsList() {
		super(R.xml.options_speech);
	}

	/** The array used to store each of the options we will be using */
	static final String[] OPTIONS = { "Headphones", "Text Size +",
			"Text Size -", "Color Scheme" };

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}
	
	/**
	 * Creates the options screen. The Android on create method used for all
	 * screen changes
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options_screen);

		btnHeadphones = (Button) findViewById(R.id.btnHeadphones);
		btnFontUp = (Button) findViewById(R.id.btnFontUp);
		btnFontDown = (Button) findViewById(R.id.btnFontDown);
		btnColorScheme = (Button) findViewById(R.id.btnColorScheme);
		btnWipe = (Button) findViewById(R.id.btnWipe);
		
		dbHelper = new SMSDbAdapter(this);
		dbHelper.open();
		
		//Adds listeners to the buttons
		btnHeadphones.setOnClickListener(new HeadphonesListener());
		btnFontUp.setOnClickListener(new FontSizeListener(true));
		btnFontDown.setOnClickListener(new FontSizeListener(false));
		btnColorScheme.setOnClickListener(new ColorListener());
		btnWipe.setOnClickListener(new WipeListener());
		
		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item,
			new String[0]));
	}

    /**
     * This method will process a voice command and turn it into 
     *  a command for this specific screen.
     */
	public boolean processVoice(VoiceCommand command) {
		// All commands handled in base class
		
		return false;
	}
	
	private void headphones_off() {
		// TODO add functionality to turn off headphones
		speak("Headphones off", SpeechType.INFO, true);
	}
	
	private void headphones_on() {
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
				headphones_on();
			}
			else {
				headphones_off();
			}
			
			Log.v("OptionsList", "Headphones toggled");
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
		private boolean willIncrement;
		
		FontSizeListener(boolean doIncrement){
			willIncrement = doIncrement;
		}
		
		public void onClick(View v) {
			if (willIncrement) {
				// TODO raise fontsize
			}
			else {
				// TODO lower fontsize
			}
		}
	}
	
	private class ColorListener implements OnClickListener {

		public void onClick(View v) {
		}

	}
	
	/**
	 * Listener to wipe the database.  Caution: really will.
	 */
	private class WipeListener implements OnClickListener {
		
		public void onClick(View v) {
			dbHelper.recreateDatabase();
		}

	}
}
