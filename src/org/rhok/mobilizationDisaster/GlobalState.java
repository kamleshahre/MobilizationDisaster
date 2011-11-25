package org.rhok.mobilizationDisaster;

import android.app.Application;

public class GlobalState extends Application {
	private boolean alertState = false;

	public boolean isAlerting() {
		return alertState;
	}

	public void setAlerting(boolean alertState) {
		this.alertState = alertState;
	}
	
	
}
