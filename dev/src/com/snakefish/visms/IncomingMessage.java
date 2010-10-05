package com.snakefish.visms;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;

import java.awt.*;

/**
 * This class deals with the GUI used for an 
 * incoming message.
 * 
 * @author Team Snakefish
 *
 */
public class IncomingMessage extends Activity {
	/**Used to positon buttons */
	public static final int COMPOSE_POSITION = Menu.FIRST;
	public static final int SETTINGS_POSITION = Menu.FIRST + 1;
	/** The button used for viewing the message */
	Button btnRead;
	/** The button used to ignore messages */
	Button btnIgnore;
	/** Used to get the name of the contact */
    public String name = new String();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);        
 
        //Sets btnRead and btnIgnore to the button on the android xml
        btnRead = (Button) findViewById(R.id.btnRead);
        btnIgnore = (Button) findViewById(R.id.btnIgnore);        
    }    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, COMPOSE_POSITION, 0, R.string.compose);
        menu.add(0, Menu.FIRST, 0, R.string.settings);
        return result;
    }
}
