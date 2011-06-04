package org.rhok.mobilizationDisaster.sender;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.widget.Toast;

public class SMSSender extends BroadcastReceiver {

	// internal Intent filter strings
	private static final String SENT = "SMS_SENT";
	private static final String DELIVERED = "SMS_DELIVERED";		

	private Context m_context;
	private SmsManager m_smsmanager;
	
	public SMSSender (Context context);
	{
		m_context = context;
		m_smsmanager = SmsManager.getDefault();
		
		registerReceiver()
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		switch (getResultCode()) {
		case Activity.RESULT_OK:
			if(Intent.)
			Toast.makeText(m_context, "SMS sent", Toast.LENGTH_SHORT)
					.show();
			break;
		case Activity.RESULT_CANCELED:
			Toast.makeText(getBaseContext(), "SMS not delivered",
					Toast.LENGTH_SHORT).show();
			break;
		case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
			Toast.makeText(m_context, "Generic failure",
					Toast.LENGTH_SHORT).show();
			break;
		case SmsManager.RESULT_ERROR_NO_SERVICE:
			Toast.makeText(m_context, "No service", Toast.LENGTH_SHORT)
					.show();
			break;
		case SmsManager.RESULT_ERROR_NULL_PDU:
			Toast.makeText(m_context, "Null PDU", Toast.LENGTH_SHORT)
					.show();
			break;
		case SmsManager.RESULT_ERROR_RADIO_OFF:
			Toast.makeText(m_context, "Radio off", Toast.LENGTH_SHORT)
					.show();
			break;
		}};
		
	private PendingIntent getPI(int userID, String phoneNumber, String intentFilter ) {

		Intent i = new Intent(intentFilter);
		i.putExtra("userID", userID);
		i.putExtra("phoneNumber", phoneNumber);
		
		return PendingIntent.getBroadcast(m_context, 0, i, 0); 
	}

	public void send(int userID, String phoneNumber, String message) {
		m_smsmanager.sendTextMessage(phoneNumber, null, message,
				getPI(userID,phoneNumber,SENT),
				getPI(userID,phoneNumber,DELIVERED)
			);
	}
}
