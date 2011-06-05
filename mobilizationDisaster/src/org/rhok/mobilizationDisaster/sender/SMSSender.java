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
	private Context m_context;
	
	public SMSSender()
	{
		m_context = null; 
	}

	public SMSSender(Context context)
	{
		m_context = context;
		
		// register intent filters for my context
		context.registerReceiver(this, new IntentFilter(SENT));
		context.registerReceiver(this, new IntentFilter(DELIVERED));
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		
		int res,userId;

		// verify if it's an answer to our call 
		if((userId = intent.getIntExtra(intent_userId, 0))==0)
			return;
		
		switch (res = getResultCode()) {
		case Activity.RESULT_OK:
			String reason = intent.getAction(); 
			if(reason.equals(SENT))
				Log.v(TAG, "SMS sent for UID="+userId);
			else
				if(reason.equals(DELIVERED))
					Log.v(TAG, "SMS delivered for UID="+userId);
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
		default:
			Log.v(TAG, "Error code "+res);		
		}};
	
	private PendingIntent createPI(Context context,int userId, String phoneNumber,String intentFilter ) {

		Intent i = new Intent(intentFilter);
		i.putExtra(intent_userId, userId);		
		i.putExtra(intent_phoneNumber, userId);		
		return PendingIntent.getBroadcast(context, 0, i, 0); 
	}

	public void send(int userId, String phoneNumber, String message) {
		Log.v(TAG, "Sending SMS to "+phoneNumber);

		if (m_context != null)
			SmsManager.getDefault().sendTextMessage(phoneNumber, null, message,
					createPI(m_context, userId, phoneNumber, SENT),
					createPI(m_context, userId, phoneNumber, DELIVERED));
	}
}
