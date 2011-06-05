package org.rhok.mobilizationDisaster.receiver;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.PhoneLookup;
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
		String contactId = getContactIdForNumber(context, sender);
		if (contactId != null) {
			Toast.makeText(context, "Received SMS from Responder with ID " + contactId+ ": " + body, Toast.LENGTH_LONG).show();
			if (isResponder(smsMessage)) {
				handleResponderSms(context, smsMessage);
			} else {
				// Not interested
			}
		}
	}
	
	public String getContactIdForNumber(Context context, String phoneNumber) {
		 Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
		 ContentResolver cr = context.getContentResolver();
		 Cursor cursor = cr.query(uri, new String[]{PhoneLookup._ID}, null, null, null);
		 if (cursor.moveToFirst()) {
			 String contactId = cursor.getString(0);
			 return contactId;
		 } else {
			 return null;
		 }
	}
	
	public boolean isResponder(SmsMessage smsMessage) {
		// TODO implement
		return true;
	}
	
	public void handleResponderSms(Context context, SmsMessage smsMessage) {
		// TODO implement
	}
}
