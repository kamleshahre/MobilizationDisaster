package org.rhok.mobilizationDisaster;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;

public class PhoneBook {
	/**
	 * Default constructor
	 */
	public PhoneBook(ContentResolver cr) {
		this.cr = cr;
	}
	
	/**
	 * Get the all the phone numbers from a group
	 */
	public List<PhoneBookEntry> getEverything() {
		List<PhoneBookEntry> entries = new ArrayList<PhoneBookEntry>();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if(cur.getCount() > 0) {
        	while(cur.moveToNext()) {
        		String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
        		String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        		if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
        			entries.add( new PhoneBookEntry(id, name, getPhoneNumberById(id)) );
        		}
            }
        }
        cur.close();
		return entries;
	}
	
	/**
	 * Get all the entries that are 'starred' (have star)
	 */
	public List<PhoneBookEntry> getStarred() {
		List<PhoneBookEntry> entries = new ArrayList<PhoneBookEntry>();
		
		// select the starred contacts from the phone book
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, 
			null, 
			ContactsContract.Contacts.STARRED + " = ?", 
			new String[]{"1"}, 
			null);
		
		// extract infos
        if(cur.getCount() > 0) {
        	while(cur.moveToNext()) {
        		boolean hasPhoneNumber = 
        			(cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0);
        		if(hasPhoneNumber) {
        			String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
            		String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        			entries.add(new PhoneBookEntry(id, name, getPhoneNumberById(id)));
        		}
            }
        }
        cur.close();
        return entries;
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
 	    if(pCur.moveToFirst()) {
 	    	number = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
 	    } 
        pCur.close();
        return number;
	}
	// members
	public ContentResolver cr;
}
