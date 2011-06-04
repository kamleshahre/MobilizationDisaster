package org.rhok.mobilizationDisaster;

import android.app.Activity;
import android.os.Bundle;

public class main extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        mContacts = new ContactsGatherer(getContentResolver());
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public ContactsGatherer mContacts;
}