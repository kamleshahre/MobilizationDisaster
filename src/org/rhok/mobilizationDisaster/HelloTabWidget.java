package org.rhok.mobilizationDisaster;

import java.util.Date;

import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class HelloTabWidget extends TabActivity {
	
	public final String TAG = "HelloTabWidget";
	public static final String INTENT_UPDATE_LIST = "org.rhok.mobilizationDisaster.Intent.UpdateList";
	
	public ResponseStatusModel m_model;
	public PhoneBook m_contacts;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.tab_main);
	    
	    m_contacts = new PhoneBook(getContentResolver());
        m_model = new ResponseStatusModel(getContentResolver());

        Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab
	    
	    updateButtons();
	    final Button startButton = (Button) findViewById(R.id.startButton);
	    startButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(HelloTabWidget .this, CommenceAlertActivity.class);
				startActivity(intent);
			}
		});    
	    final Button stopButton = (Button) findViewById(R.id.stopButton);
	    stopButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				GlobalState gs = (GlobalState) getApplication();
				gs.setAlerting(false);
				// set everything to zero
				m_model.startAlerting();
				updateButtons();
			}
		});    
	    
	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("start").setIndicator("Start",
	                      res.getDrawable(R.drawable.icon_tab_start))
	                  .setContent(R.id.welcomeLayout);
	    tabHost.addTab(spec);

	    // Do the same for the other tabs
	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, TabAllActivity.class);
	    spec = tabHost.newTabSpec("all").setIndicator("Alle",
	                      res.getDrawable(R.drawable.icon_tab_all))
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    intent = new Intent().setClass(this, TabAcceptedActivity.class);
	    spec = tabHost.newTabSpec("accepted").setIndicator("Zugesagt",
	                      res.getDrawable(R.drawable.icon_tab_yes))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    intent = new Intent().setClass(this, TabDeclinedActivity.class);
	    spec = tabHost.newTabSpec("declined").setIndicator("Abgelehnt",
	                      res.getDrawable(R.drawable.icon_tab_no))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    intent = new Intent().setClass(this, TabPendingActivity.class);
	    spec = tabHost.newTabSpec("pending").setIndicator("Unbekannt",
	                      res.getDrawable(R.drawable.icon_tab_pending))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    tabHost.setCurrentTab(0);
	    
	    IntentFilter filter = new IntentFilter();
	    filter.addAction(HelloTabWidget.INTENT_UPDATE_LIST);
	    registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				HelloTabWidget.this.refresh();
				String contactName = intent.getStringExtra("contactName");
				String contactId = intent.getStringExtra("contactId");
				String response = intent.getStringExtra("smsBody");
				String incomingNotification = "Antwort von " + contactName + ": " + response;
				Toast.makeText(getApplicationContext(), incomingNotification,
				          Toast.LENGTH_SHORT).show();
				ResponseStatusModel model = new ResponseStatusModel(getContentResolver());
				model.update(Integer.parseInt(contactId), 100, null, null, -1);
			
			}
	    	
	    }, filter);
	}
	private Button updateButtons() {
		Resources res = getResources(); 
		GlobalState gs = (GlobalState) getApplication();
		
		final Button startButton = (Button) findViewById(R.id.startButton);
	    final Button stopButton = (Button) findViewById(R.id.stopButton);
	    final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
	    final TextView alertStateText = (TextView) findViewById(R.id.alertStateText);
	    if (gs.isAlerting()) {
	    	startButton.setEnabled(false);
	    	stopButton.setEnabled(true);
	    	progressBar.setVisibility(View.VISIBLE);
	    	alertStateText.setText(res.getString(R.string.alert_state_true));
	    }
	    else {
	    	startButton.setEnabled(true);
	    	stopButton.setEnabled(false);
	    	progressBar.setEnabled(false);
	    	progressBar.setVisibility(View.INVISIBLE);
	    	alertStateText.setText(res.getString(R.string.alert_state_false));
	    }
		return startButton;
	}
	@Override
	public void onResume() {
		super.onResume();
		updateButtons();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		updateButtons();
	}
	
	public void refresh() {
		// TODO implement
		Log.v(TAG, "Updating list...");
	}
}
