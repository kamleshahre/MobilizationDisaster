package org.rhok.mobilizationDisaster.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {

	private static final String TAG = "SMSReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.v(TAG, "Received an SMS!");
		Bundle bundle = intent.getExtras();

		Object messages[] = (Object[]) bundle.get("pdus");
		SmsMessage smsMessages[] = new SmsMessage[messages.length];
		for (int n = 0; n < messages.length; n++) {
			smsMessages[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
		}

		SmsMessage smsMessage = smsMessages[0];
		this.handleSms(context, smsMessage);
	}
	
	public void handleSms(Context context, SmsMessage smsMessage) { 
		String sender = smsMessage.getOriginatingAddress();
		String body = smsMessage.getMessageBody();
		Toast toast = Toast.makeText(context, "Received SMS from " + sender + " : " + body, Toast.LENGTH_LONG);
		toast.show();
	}
}
