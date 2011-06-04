package org.rhok.mobilizationDisaster;

import org.rhok.mobilizationDisaster.providers.ResponseStatus.ResponseStates;
import org.rhok.mobilizationDisaster.sender.SMSSender;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class main extends Activity {
	
    public PhoneBook m_contacts;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.v("bla", "blub");
    	SMSSender s = new SMSSender();
    	s.send(getApplicationContext(), 23, "0172-8757502", "Hallo Uwe!");
    	
        m_contacts = new PhoneBook(getContentResolver());
        
        // this is how you use content providers
        Cursor cur = getContentResolver().query(ResponseStates.CONTENT_URI, null, null, null, null);
        if(cur.moveToFirst()) {
        	Log.v("bla", cur.getString(cur.getColumnIndex(ResponseStates.TEXT)));
        }
        cur.close();
        m_contacts.getEverything();
        m_contacts.getStarred();
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