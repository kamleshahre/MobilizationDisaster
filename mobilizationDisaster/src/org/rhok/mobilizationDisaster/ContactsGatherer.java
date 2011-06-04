package org.rhok.mobilizationDisaster;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;

public class ContactsGatherer {
	/**
	 * Default constructor
	 */
	public ContactsGatherer(ContentResolver cr) {
		this.cr = cr;
	}
	
	/**
	 * Get the all the phone numbers from a group
	 */
	public List<String> getAllNumbers() {
		List<String> numbers = new ArrayList<String>();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if(cur.getCount() > 0) {
        	while(cur.moveToNext()) {
        		String id   = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
        		String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        		if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
        			numbers.add(getPhoneNumberById(id));
        		}
            }
        }
        cur.close();
		return numbers;
	}
	
	/**
	 * Queries a phone number by id
	 */
	public String getPhoneNumberById(String id) {
		String number = null;
		Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
			null, 
 		    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?", 
 		    new String[]{id}, null);
 	    while (pCur.moveToFirst()) {
 	    	pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
 	    } 
        pCur.close();
        return number;
	}
	// members
	public ContentResolver cr;
}
