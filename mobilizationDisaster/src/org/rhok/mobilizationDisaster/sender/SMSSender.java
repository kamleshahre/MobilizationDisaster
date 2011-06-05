package org.rhok.mobilizationDisaster.sender;

import java.util.Date;
import org.rhok.mobilizationDisaster.ResponseStatusModel;
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
    private ResponseStatusModel m_model;
	
	public SMSSender()
	{
		m_context = null;
		m_model = null;
	}

	public SMSSender(Context context, ResponseStatusModel model)
	{
		m_context = context;
		m_model = model;

		// register intent filters for my context
		context.registerReceiver(this, new IntentFilter(SENT));
		context.registerReceiver(this, new IntentFilter(DELIVERED));
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		ResponseStatusModel model;
		int res,userId;

		// verify if it's an answer to our call 
		if((userId = intent.getIntExtra(intent_userId, 0))==0)
			return;

		model = (m_model != null) ? m_model :
			new ResponseStatusModel(context.getContentResolver());

		switch (res = getResultCode()) {
		case Activity.RESULT_OK:
			String reason = intent.getAction(); 
			if(reason.equals(SENT))
			{
				Log.v(TAG, "SMS sent for UID="+userId);
				model.update(userId, ResponseStatusModel.RESPONDER_STATE_SENT, new Date(), null, -1);
			}
			else
				if(reason.equals(DELIVERED))
				{
					Log.v(TAG, "SMS delivered for UID="+userId);
					model.update(userId, ResponseStatusModel.RESPONDER_STATE_DELIVERED, null, new Date(), -1);
				}
			break;
		default:
			Log.v(TAG, "Error code "+res);		
			model.update(userId, ResponseStatusModel.RESPONDER_STATE_ERROR, null, new Date(), -1);
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
