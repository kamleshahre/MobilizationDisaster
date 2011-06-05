package org.rhok.mobilizationDisaster;

import java.util.ArrayList;
import java.util.List;

import org.rhok.mobilizationDisaster.providers.ResponseStatus.ResponseStates;

import android.content.ContentResolver;
import android.database.Cursor;

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
	// Success - Worker Status (1-99):
	public static final int RESPONDER_STATE_SENT = 1;
	public static final int RESPONDER_STATE_DELIVERED = 2;
	// Success - Final Status (100-199):
	public static final int RESPONDER_STATE_YES = 100;
	public static final int RESPONDER_STATE_NO = 101;
	
	public int state;
	public ContentResolver cr;
	
	public ResponseStatusModel(ContentResolver cr) {
		this.cr = cr;
		this.state = SLEEPING;
	}
	
	public String[] getPending()
	{
		return getListByCriterion(ResponseStates.STATE + " <= ?",
			new String[] { new Integer(RESPONDER_STATE_DELIVERED).toString() });
	}
	
	public String[] getYes()
	{
		return getListByCriterion(ResponseStates.STATE + " = ?",
			new String[] { new Integer(RESPONDER_STATE_YES).toString() });
	}
	
	public String[] getNo()
	{
		return getListByCriterion(ResponseStates.STATE + " = ?",
			new String[] { new Integer(RESPONDER_STATE_NO).toString() });
	}
	
	/**
	 * Example: getListByCriterion("name = ?", new String[] { "Henner Piffendeckel" });
	 * @param where The where-clause with ? as placeholder for the args
	 * @param whereArgs the args that replace the ?-placeholders.
	 * @return a simple String array
	 */
	public String[] getListByCriterion(String where, String[] whereArgs)
	{
		List<String> list = new ArrayList<String>();
		Cursor cur = cr.query(ResponseStates.CONTENT_URI, 
			null, 
			ResponseStates.STATE + " <= ?", 
			new String[] { new Integer(RESPONDER_STATE_DELIVERED).toString() },
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
        
        cur.close();
        return (String[]) list.toArray();
	}
}
