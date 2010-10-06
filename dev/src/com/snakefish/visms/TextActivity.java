package com.snakefish.visms;

import java.util.List;
import java.util.Scanner;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TextActivity extends SMSActivity {

	public TextActivity() {
		super(R.xml.text_speech);
	}
	
	private TextView textTop = null;
	private EditText textBot = null;
	private Button butLeft = null;
	private Button butRight = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_view);
        
        textTop = (TextView)findViewById(R.id.text_top);
        textBot = (EditText)findViewById(R.id.text_bot);
        butLeft = (Button)findViewById(R.id.text_b1);
        butRight = (Button)findViewById(R.id.text_b2);

        assert(textTop != null);
        assert(textBot != null);
        assert(butLeft != null);
        assert(butRight != null);
        
        butLeft.setOnClickListener(new LeftButtonClickListener());
        butRight.setOnClickListener(new RightButtonClickListener());
    }
    
    @Override
    public void processVoice(List<String> command) {
    	textTop.setText("Received from voice: " + command);
    	
    	/*Scanner commandParser = new Scanner(command);
    	
    	if (commandParser.next().equals("command")) {
    		String commandPart = commandParser.next();
    		
    		if (commandPart.equals("read")) {
    			speak(textBot.getText().toString());
    		}
    	}*/
    }
    
    @Override
    public void processVoice(String command) {
    	textBot.setText("Received from voice: " + command);
    }
    
    private class LeftButtonClickListener implements OnClickListener {

    	/**
    	 * Do not fill in this method any more!
    	 * Refer to seperate reusable methods instead
    	 */
		public void onClick(View arg0) {
			doWork();
		}
		
		public void doWork() {
			// TODO actual work
			textTop.setText("READ MORE! fffffffffffffff" +
					"ffffffffffffffffffffffffffffffffffff" +
					"ffffffffffffffffffffffffffffffffffff" +
					"ffffffffffffffffffffffffffffffffffff" +
					"ffffffffffffffffffffffffffffffffffff" +
					"ffffffffffffffffffffffffffffffffffff" +
					"ffffffffffffffffffffffffffffffffffff" +
					"ffffffffffffffffffffffffffffffffffff" +
					"ffffffffffffffffffffffffffffffffffff" +
					"ffffffffffffffffffffffffffffffffffff" +
					"ffffffffffffffffffffffffffffffffffff" +
					"ffffffffffffffffffffffffffffffffffff" +
					"ffffffffffffffffffffffffffffffffffff" +
					"ffffffffffffffffffffffffffffffffffff" +
					"ffffffffffffffffffffffffffffffffffff" +
					"ffffffffffffffffffffffffffffffffffff" +
					"ffffffffffffffffffffffffffffffff");
			
			speak("Hello");
		}
    	
    }
    
    private class RightButtonClickListener implements OnClickListener{

    	/**
    	 * Do not fill in this method any more!
    	 * Refer to seperate reusable methods instead
    	 */
		public void onClick(View arg0) {
			doWork();
		}
		
		public void doWork() {
			// TODO actual work
			textBot.setText("SEND MORE!");
		}
    	
    }
    
}