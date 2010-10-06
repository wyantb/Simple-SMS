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
	protected Button btnRead;
	/** The button used to ignore messages */
	protected Button btnIgnore;
	/** Used to get the name of the contact */
	protected String name = new String();
	/** Used to determine orientation of the phone */
    protected boolean landscape = false;
    
    /**
     * Creates a new incoming message
     */
    IncomingMessage(String name){
    	this.name = name;
    }
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        //Sets the correct layout
        if(landscape == false){
        	//Portrait view
        	setContentView(R.layout.incoming_message);        
        }else{
        	//Landscape view
        	setContentView(R.layout.incoming_message_landscape);
        }
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
    
    @Override
    public void onPause(){
    	super.onPause();
    }
    
    /**
     * Sets the name string
     * 
     * @param name - the name of the person texting 
     */
    public void setName(String name){
    	this.name = name;
    }
    
    /**
     * Sets the view the android is in
     * 
     * @param landscape - If it is landscape view or not
     */
    public void setView(boolean landscape){
    	this.landscape = landscape;
    }
}
