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
	
	// declaration of 'Intent'-payload names
	private static final String intent_userId = TAG+".userId";
	private static final String intent_phoneNumber = TAG+".phoneNumber";

	// internal Intent filter strings
	private static final String SENT = "SMS_SENT";
	private static final String DELIVERED = "SMS_DELIVERED";		

	@Override
	public void onReceive(Context context, Intent intent) {
		int userId;
		
		// verify if it's an answer to our call 
		if((userId = intent.getIntExtra(intent_userId, 0))==0)
			return;
		
		switch (getResultCode()) {
		case Activity.RESULT_OK:
			Log.v(TAG, "SMS sent/delivered for UID="+userId);
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
	
	private PendingIntent getPI(Context context,int userId, String phoneNumber,String intentFilter ) {

		Intent i = new Intent(intentFilter);
		i.putExtra(intent_userId, userId);		
		i.putExtra(intent_phoneNumber, userId);		
		return PendingIntent.getBroadcast(context, 0, i, 0); 
	}

	public void send(Context context, int userId, String phoneNumber, String message) {
		SmsManager.getDefault().sendTextMessage(phoneNumber, null, message,
				getPI(context,userId,phoneNumber,SENT),
				getPI(context,userId,phoneNumber,DELIVERED)
			);
	}
}
