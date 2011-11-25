package org.rhok.mobilizationDisaster.providers;

import android.net.Uri;
import android.provider.BaseColumns;

public class ResponseStatus {
	
	public ResponseStatus() {
		
	}
	
	public static final class ResponseStates implements BaseColumns {
		
		private ResponseStates() {
			// do nothing
		}
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + 
			ResponseStatusProvider.AUTHORITY + "/status");
	
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.rhok.status";
		
		public static final String ID    = "_id";
		public static final String STATE = "state";
		public static final String TRIES = "tries";
		public static final String TIME  = "time";
		public static final String TEXT  = "text";
		
		
	}

}
