package org.rhok.mobilizationDisaster;

import org.rhok.mobilizationDisaster.sender.SMSSender;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class main extends Activity {
	
    public PhoneBook m_contacts;
    public ResponseStatusModel m_model;
    
    public static final String MESSAGE = "Bitte in der Zentrale melden!";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.v("bla", "blub");
    	
        m_contacts = new PhoneBook(getContentResolver());
        m_model = new ResponseStatusModel(getContentResolver());
        
        m_contacts.getEverything();
        m_contacts.getStarred();
        
       

        SMSSender sms = new SMSSender(getApplicationContext(), m_model);
        sms.send(23, "0172-8757502", "Hallo Uwe!");

        String[] pending = m_model.getPending();
        String[] accepted = m_model.getYes();
        String[] declined = m_model.getNo();   
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                // Perform action on clicks
                //Toast.makeText(main.this, "Alarm ausgelöst", Toast.LENGTH_SHORT).show();
            	 m_model.startAlerting();
            	 SMSSender sender = new SMSSender(getApplicationContext(), m_model);
            	 for(PhoneBookEntry i: m_model.getPendingNumbers())
            	 {
            		 sender.send(Integer.parseInt(i.getId()), i.getNumber(), MESSAGE);
            	 }
            }
        });
        
        final Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
            	Intent myIntent = new Intent(view.getContext(), ResponseStatus.class);
                startActivityForResult(myIntent, 0);
            }
        });
        
        final Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
            	Intent myIntent = new Intent(view.getContext(), HelloTabWidget.class);
                startActivityForResult(myIntent, 0);
            }
        });
        
        
    }
}