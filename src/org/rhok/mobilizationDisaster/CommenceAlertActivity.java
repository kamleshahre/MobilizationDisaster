package org.rhok.mobilizationDisaster;

import java.util.Date;
import java.util.Random;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import org.rhok.mobilizationDisaster.sender.SMSSender;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class CommenceAlertActivity extends Activity {
	
    public PhoneBook m_contacts;
    public ResponseStatusModel m_model;
    
    public String launchcode;
    
    public static final String MESSAGE = "";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.start_alert);
        
        m_contacts = new PhoneBook(getContentResolver());
        m_model = new ResponseStatusModel(getContentResolver());
        
        m_contacts.getEverything();
        m_contacts.getStarred();

//        String[] pending = m_model.getPending();
//        String[] accepted = m_model.getYes();
//        String[] declined = m_model.getNo();   

        launchcode = createLaunchCode();
        final TextView codeView = (TextView) findViewById(R.id.launchCodeTextView);
        codeView.setText(launchcode);
        
        EditText launchCodeInput = (EditText) findViewById(R.id.launchCodeInput);
        
        launchCodeInput.setOnEditorActionListener(new OnEditorActionListener() {
			
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				String text = v.getText().toString();
				final Button redButton = (Button) findViewById(R.id.redButton);
				if (text.equals(launchcode)) {
					redButton.setEnabled(true);
				}
				else {
					redButton.setEnabled(false);
				}
				return false;
			}
		});

        final Button redButton = (Button) findViewById(R.id.redButton);
        redButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                 // Perform action on clicks
                 Toast.makeText(CommenceAlertActivity.this, "Alarm ausgeloest", Toast.LENGTH_SHORT).show();
            	 m_model.startAlerting();
            	 GlobalState gs = (GlobalState) getApplication();
            	 gs.setAlerting(true);
            	 ProgressBar sendProgress =  (ProgressBar) findViewById(R.id.sendProgress);
            	 SMSSender sender = new SMSSender(getApplicationContext(), m_model);
            	 
            	 // activate the progress bar
            	 sendProgress.setMax(m_model.getPendingNumbers().size());
            	 int sent = 0;
            	 sendProgress.setProgress(sent);
            	 sendProgress.setEnabled(true);
            	 EditText alertText = (EditText) findViewById(R.id.alertText);
            	 String message = alertText.getText().toString();
            	 // send out the SMS
            	 for(PhoneBookEntry i: m_model.getPendingNumbers())
            	 {
            		 SimpleDateFormat df = new SimpleDateFormat();
//            		 sender.send(Integer.parseInt(i.getId()), i.getNumber(), 
//        				 message + df.format(new Date()) );
            		 sent++;
            		 sendProgress.setProgress(sent);
            	 }
            	 finish();
            }
        });
    }
    
    /**
     * Creates a three-digit launch code.
     * @return A string containing the launchcode, a number with leading zeros between
     * 000 and 999.
     */
    public String createLaunchCode() {
    	Random r = new Random();
    	int randomInt = r.nextInt(1000);
    	
    	DecimalFormat nf = new DecimalFormat("000");
    	return nf.format(randomInt);
    }
}