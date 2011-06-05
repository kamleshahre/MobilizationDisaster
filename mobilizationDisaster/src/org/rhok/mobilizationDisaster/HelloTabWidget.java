package org.rhok.mobilizationDisaster;

import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class HelloTabWidget extends TabActivity{
	
	public final String TAG = "HelloTabWidget";
	public static final String INTENT_UPDATE_LIST = "org.rhok.mobilizationDisaster.Intent.UpdateList";
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.tab_main);

	    Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, TabStartActivity.class);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("start").setIndicator("Start",
	                      res.getDrawable(R.drawable.icon_tab_start))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    // Do the same for the other tabs
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

	   

	    tabHost.setCurrentTab(4);
	    
	    IntentFilter filter = new IntentFilter();
	    filter.addAction(HelloTabWidget.INTENT_UPDATE_LIST);
	    registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				HelloTabWidget.this.refresh();
				String contactName = intent.getStringExtra("contactName");
				String response = intent.getStringExtra("smsBody");
				String incomingNotification = "Antwort von " + contactName + ": " + response;
				Toast.makeText(getApplicationContext(), incomingNotification,
				          Toast.LENGTH_SHORT).show();
			}
	    	
	    }, filter);
	}
	
	public void refresh() {
		// TODO implement
		Log.v(TAG, "Updating list...");
	}
}
