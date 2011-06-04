package org.rhok.mobilizationDisaster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class main extends Activity {
	
    public PhoneBook m_contacts;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        m_contacts = new PhoneBook(getContentResolver());
        m_contacts.getEverything();
        m_contacts.getGroup("responders");
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    
        final Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                // Perform action on clicks
                Toast.makeText(main.this, "Alarm ausgelšst", Toast.LENGTH_SHORT).show();
            }
        });
        final Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
            	Intent myIntent = new Intent(view.getContext(), ResponseStatus.class);
                startActivityForResult(myIntent, 0);
            }
        });
        
    }
}