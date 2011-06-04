package org.rhok.mobilizationDisaster.sender;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class SMSSender extends BroadcastReceiver {

	private static final String TAG = "SMSSender";
	
	// internal Intent filter strings
	private static final String SENT = "SMS_SENT";
	private static final String DELIVERED = "SMS_DELIVERED";		

	@Override
	public void onReceive(Context context, Intent intent) {
		switch (getResultCode()) {
		case Activity.RESULT_OK:
			Log.v(TAG, "SMS sent/delivered");
			break;
		case Activity.RESULT_CANCELED:
			Log.v(TAG, "SMS not delivered");
			break;
		case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
			Log.v(TAG, "Generic failure");
			break;
		case SmsManager.RESULT_ERROR_NO_SERVICE:
			Log.v(TAG, "No service");
			break;
		case SmsManager.RESULT_ERROR_NULL_PDU:
			Log.v(TAG, "Null PDU");
			break;
		case SmsManager.RESULT_ERROR_RADIO_OFF:
			Log.v(TAG,  "Radio off");
			break;
		}};
		
	private PendingIntent getPI(Context context,int userID, String phoneNumber, String intentFilter ) {

		Intent i = new Intent(intentFilter);
		i.putExtra("userID", userID);
		i.putExtra("phoneNumber", phoneNumber);
		
		return PendingIntent.getBroadcast(context, 0, i, 0); 
	}

	public void send(Context context, int userID, String phoneNumber, String message) {
		SmsManager.getDefault().sendTextMessage(phoneNumber, null, message,
				getPI(context,userID,phoneNumber,SENT),
				getPI(context,userID,phoneNumber,DELIVERED)
			);
	}
}
