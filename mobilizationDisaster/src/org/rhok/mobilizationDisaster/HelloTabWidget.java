package org.rhok.mobilizationDisaster;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class HelloTabWidget extends TabActivity{
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
	                      res.getDrawable(R.drawable.icon_tab_test))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, TabAllActivity.class);
	    spec = tabHost.newTabSpec("all").setIndicator("Alle",
	                      res.getDrawable(R.drawable.icon_tab_test))
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    
	    intent = new Intent().setClass(this, TabAcceptedActivity.class);
	    spec = tabHost.newTabSpec("accepted").setIndicator("Zugesagt",
	                      res.getDrawable(R.drawable.icon_tab_test))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    intent = new Intent().setClass(this, TabAllActivity.class);
	    spec = tabHost.newTabSpec("all").setIndicator("Abgelehnt",
	                      res.getDrawable(R.drawable.icon_tab_test))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    intent = new Intent().setClass(this, TabAllActivity.class);
	    spec = tabHost.newTabSpec("unknown").setIndicator("Unbekannt",
	                      res.getDrawable(R.drawable.icon_tab_test))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	   

	    tabHost.setCurrentTab(2);
	}
}
