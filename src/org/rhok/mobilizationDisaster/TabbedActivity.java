package org.rhok.mobilizationDisaster;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class TabbedActivity extends TabActivity {
	public TabHost tabHost;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.tab_activity_layout);

	    
	    
            // Get the tabHost
//	    this.tabHost = getTabHost();
//
//	    TabHost.TabSpec spec;  	// Reusable TabSpec for each tab
//	    Intent intent;  		// Reusable Intent for each tab
//
//	    // Create an Intent to launch the first Activity for the tab (to be reused)
//	    intent = new Intent().setClass(this, FirstGroup.class);
//
//	    // Initialize a TabSpec for the first tab and add it to the TabHost
//	    spec = tabHost.newTabSpec("FirstGroup")
//	    		//.setIndicator("FirstGroup", getResources().getDrawable (null)) // Replace null with R.drawable.your_icon to set tab icon
//	    				.setContent(intent);
//	    tabHost.addTab(spec);
//
//            // Create an Intent to launch an Activity for the tab (to be reused)
//	    //intent = new Intent().setClass(this, SecondActivityGroup.class);
//
//	    // Initialize a TabSpec for the second tab and add it to the TabHost
//	    /*spec = tabHost.newTabSpec("SecondGroup").setIndicator("SecondGroup",
//	    		getResources().getDrawable
//	    				(null)) // Replace null with R.drawable.your_icon to set tab icon
//	    				.setContent(intent);
//	    tabHost.addTab(spec);
//		*/
//	    tabHost.setCurrentTab(0);
	}

}