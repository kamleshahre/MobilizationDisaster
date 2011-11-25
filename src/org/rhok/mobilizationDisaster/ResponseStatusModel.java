package org.rhok.mobilizationDisaster;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.rhok.mobilizationDisaster.providers.ResponseStatus.ResponseStates;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;

/**
 * This class is a wrapper around the ContentProvider interface to the
 * SQLite data storage and provides easy methods to get the lists of
 * people and their response status.
 */
public class ResponseStatusModel {
	
	// the different states the model can be in
	public static final int SLEEPING = 0;
	public static final int ALERTING = 1;
	
	// the different states one person can be in
	public static final int RESPONDER_STATE_IDLE = 0;
	public static final int RESPONDER_STATE_ERROR = 1;
	// Success - Worker Status (2-99):
	public static final int RESPONDER_STATE_SENT = 2;
	public static final int RESPONDER_STATE_DELIVERED = 3;
	// Success - Final Status (100-199):
	public static final int RESPONDER_STATE_YES = 100;
	public static final int RESPONDER_STATE_NO = 101;
	
	public int state;
	public ContentResolver cr;
	
	public ResponseStatusModel(ContentResolver cr) {
		this.cr = cr;
		this.state = SLEEPING;
	}
	
	public void update(int userId, int state, String text, Date time, int tries) {
		ContentValues values = new ContentValues();
		if (state != -1) {
			values.put(ResponseStates.STATE, state);
		}
		if (text != null) {
			values.put(ResponseStates.TEXT,  text);
		}
		if (time != null) {
			SimpleDateFormat df = new SimpleDateFormat();
			String timeStr = df.format(time);
			values.put(ResponseStates.TIME,  timeStr);
		}
		if (tries != -1) {
			values.put(ResponseStates.TRIES, tries);
		}
		cr.update(ResponseStates.CONTENT_URI, values, ResponseStates.ID + " = ?", 
			new String[] { new Integer(userId).toString() } );
	}
	
	public void cameIn(String number)
	{
		
	    Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
	    String name = "?";

	    
	    Cursor contactLookup = cr.query(uri, new String[] {BaseColumns._ID,
	            ContactsContract.PhoneLookup.DISPLAY_NAME }, null, null, null);
	    
	    try {
	        if (contactLookup != null && contactLookup.getCount() > 0) {
	            contactLookup.moveToNext();
	            int contactId = contactLookup.getInt(contactLookup.getColumnIndex(BaseColumns._ID));
	            update(contactId, RESPONDER_STATE_YES, "n/a", new Date(), 0);
	        }
	    } finally {
	        if (contactLookup != null) {
	            contactLookup.close();
	        }
	    }
	    
	}
	
	/**
	 * Deletes the whole DB and fills it with new values in 'starting' state.
	 */
	public void startAlerting()
	{
		this.state = ALERTING;
		SimpleDateFormat df = new SimpleDateFormat();
		String timeStr = df.format(new Date());

		cr.delete(ResponseStates.CONTENT_URI, null, null);
		PhoneBook pb = new PhoneBook(cr);
		List<PhoneBookEntry> everybody = pb.getStarred();
		for(PhoneBookEntry e: everybody) {
			ContentValues values = new ContentValues();
			
			values.put(ResponseStates.ID,    e.getId());
			values.put(ResponseStates.STATE, RESPONDER_STATE_IDLE);
			values.put(ResponseStates.TEXT,  "");
			values.put(ResponseStates.TIME,  timeStr);
			values.put(ResponseStates.TRIES, 0);
			
			cr.insert(ResponseStates.CONTENT_URI, values);
		}
	}
	
	public List<PhoneBookEntry> getPendingNumbers()
	{
		List<PhoneBookEntry> list = new ArrayList<PhoneBookEntry>();
		Cursor cur = cr.query(ResponseStates.CONTENT_URI, 
			null, 
			ResponseStates.STATE + " < ?", 
			new String[] { "10" },
			null);
		
        while(cur.moveToNext()) {
        	String userId = cur.getString(cur.getColumnIndex(ResponseStates.ID));
        	PhoneBook pb = new PhoneBook(cr);
        	PhoneBookEntry entry = pb.getEntryById(userId);
        	if(entry.getDisplayName() != null)
        	{
        		list.add(entry);
        	}
        }
        
        cur.close();
        return list;
	}
	
	public Cursor getPending()
	{
		return getListByCriterion(ResponseStates.STATE + " < ?",
			new String[] { "10" });
	}
	
	public Cursor getYes()
	{
		return getListByCriterion(ResponseStates.STATE + " = ?",
			new String[] { new Integer(RESPONDER_STATE_YES).toString() });
	}
	
	public Cursor getNo()
	{
		return getListByCriterion(ResponseStates.STATE + " = ?",
			new String[] { new Integer(RESPONDER_STATE_NO).toString() });
	}
	
	public Cursor getAll()
	{
		return getListByCriterion(null, null);
	}
	
	/**
	 * Example: getListByCriterion("name = ?", new String[] { "Henner Piffendeckel" });
	 * @param where The where-clause with ? as placeholder for the args
	 * @param whereArgs the args that replace the ?-placeholders.
	 * @return A cursor
	 */
	public Cursor getListByCriterion(String where, String[] whereArgs)
	{
		List<String> list = new ArrayList<String>();
		Cursor cur = cr.query(ResponseStates.CONTENT_URI, 
			null, 
			where, 
			whereArgs,
			null);
		
        while(cur.moveToNext()) {
        	String userId = cur.getString(cur.getColumnIndex(ResponseStates.ID));
        	PhoneBook pb = new PhoneBook(cr);
        	PhoneBookEntry entry = pb.getEntryById(userId);
        	if(entry.getDisplayName() != null)
        	{
        		list.add(entry.getDisplayName());
        	}
        }
        
        return cur;
        
	}
}
