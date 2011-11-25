package org.rhok.mobilizationDisaster;

/**
 * Simple value object for one phone book entry
 */
public class PhoneBookEntry {
	
	public PhoneBookEntry(String id, String displayName, String number) {
		this.id = id;
		this.displayName = displayName;
		this.number = number;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public String getNumber() {
		return number;
	}
	
	public String getId() {
		return id;
	}
	
	// members
	private String id;
	private String displayName;
	private String number;

}
