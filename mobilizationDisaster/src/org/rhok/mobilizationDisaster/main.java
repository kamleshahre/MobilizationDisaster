package org.rhok.mobilizationDisaster;

import android.app.Activity;
import android.os.Bundle;

public class main extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        m_contacts = new PhoneBook(getContentResolver());
        m_contacts.getEverything();
        m_contacts.getGroup("responders");
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public PhoneBook m_contacts;
}