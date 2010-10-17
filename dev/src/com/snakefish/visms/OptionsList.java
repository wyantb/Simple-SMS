package com.snakefish.visms;

import java.util.List;

import com.snakefish.feedback.CommandAction;
import com.snakefish.feedback.VoiceCommand;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class OptionsList extends SMSListActivity {

    public OptionsList() {
		super(R.xml.options_speech);
	}

	static final String[] OPTIONS = {"Headphones", "Text Size +", "Text Size -", "Color Scheme"};
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, OPTIONS));

      ListView lv = getListView();
      lv.setTextFilterEnabled(true);

      lv.setOnItemClickListener(new OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view,
            int position, long id) {
          // When clicked, show a toast with the TextView text
          Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
              Toast.LENGTH_SHORT).show();
        }
      });
    }

	public void processVoice(VoiceCommand command) {
		// TODO actually do stuff in options view...
	}
    
}
