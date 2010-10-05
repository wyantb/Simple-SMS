package com.snakefish.visms;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
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
	
	/** Used to get the name of the contact */
    public String name = new String();
    
    /** JLable used to store name */
    //JLabel contactName = new JLabel();
    
    /** 
     * The constructor 
     */
    public IncomingMessage(){
    	
    }
    
    /**
     *  The constructor. Allows to store a name 
     */
    public IncomingMessage(String name){
    	//Sets name to incoming name
    	this.name = name;
    }
    
	public void buildGUI(String name){
		
	}
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.convo_list);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, COMPOSE_POSITION, 0, R.string.compose);
        menu.add(0, Menu.FIRST, 0, R.string.settings);
        return result;
    }
}
