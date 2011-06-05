package org.rhok.mobilizationDisaster;

import java.util.Date;
import java.text.SimpleDateFormat;

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
    
    public static final String MESSAGE = "Bitte in der Zentrale melden! Alarmierung gestartet um: ";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.v("bla", "blub");
    	
        m_contacts = new PhoneBook(getContentResolver());
        m_model = new ResponseStatusModel(getContentResolver());
        
        m_contacts.getEverything();
        m_contacts.getStarred();

        String[] pending = m_model.getPending();
        String[] accepted = m_model.getYes();
        String[] declined = m_model.getNo();   
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                 // Perform action on clicks
                 Toast.makeText(main.this, "Alarm ausgeloest", Toast.LENGTH_SHORT).show();
            	 m_model.startAlerting();
            	 SMSSender sender = new SMSSender(getApplicationContext(), m_model);
            	 for(PhoneBookEntry i: m_model.getPendingNumbers())
            	 {
            		 SimpleDateFormat df = new SimpleDateFormat();
            		 sender.send(Integer.parseInt(i.getId()), i.getNumber(), 
        				 MESSAGE + df.format(new Date()) );
            	 }
            	 Intent myIntent = new Intent(view.getContext(), ResponseStatus.class);
            	 startActivityForResult(myIntent, 0);
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