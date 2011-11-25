package org.rhok.mobilizationDisaster;

import org.rhok.mobilizationDisaster.providers.ResponseStatus.ResponseStates;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * CursorAdapter subclass that is used to display a list of contact names in 
 * the four list (all, yes, no, and pending). One such adapter is attached to
 * each ListView.
 */
public class AwesomeCursorAdapter extends CursorAdapter {
	
	private LayoutInflater inflater;
	
	public AwesomeCursorAdapter(Context context, Cursor cur) {
		super(context, cur);
		
		// inflater is created once here and is then reused in newView() to
		// avoid constantly creating and destroying an inflater object.
		inflater = LayoutInflater.from(context);
	}

	/**
	 * Adds the contact names to the ListView
	 */
	@Override
	public void bindView(View view, Context context, Cursor cur) {
		TextView itemTextView = (TextView) view.findViewById(R.id.awesomeTextView);
		 
		// Database only holds the contact's ID. This ID is resolved to readable
		// display-name
		String contactId = cur.getString(cur.getColumnIndex(ResponseStates.ID));
		PhoneBook pb = new PhoneBook(context.getContentResolver());
		itemTextView.setText(pb.getEntryById(contactId).getDisplayName());
	}

	/**
	 * Adds a TextView (as described in res/layout/list_item.xml) to each
	 * list item by inflating the XML layout there.
	 */
	@Override
	public View newView(Context context, Cursor cur, ViewGroup parent) {
		final View view = inflater.inflate(R.layout.list_item, parent, false);
	    return view;
	}

}
