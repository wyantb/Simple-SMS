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
	/** Used to get the name of the contact */
    public String name = new String();
    
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
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);        
 
        //Sets btnRead to the button on the android xml
        btnRead = (Button) findViewById(R.id.btnRead);
        
        /*btnRead.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {                
                String phoneNo = txtPhoneNo.getText().toString();
                String message = txtMessage.getText().toString();                 
                if (phoneNo.length()>0 && message.length()>0)                
                    sendSMS(phoneNo, message);                
                else
                    Toast.makeText(getBaseContext(), 
                        "Please enter both phone number and message.", 
                        Toast.LENGTH_SHORT).show();
            }
        }); */        
    }    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, COMPOSE_POSITION, 0, R.string.compose);
        menu.add(0, Menu.FIRST, 0, R.string.settings);
        return result;
    }
}
