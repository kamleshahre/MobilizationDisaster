package org.rhok.mobilizationDisaster.receiver;

import org.rhok.mobilizationDisaster.HelloTabWidget;
import org.rhok.mobilizationDisaster.ResponseStatusModel;
import org.rhok.mobilizationDisaster.providers.ResponseStatus;

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
		Cursor contact = getContactForNumber(context, sender);
		if (contact != null) {
			String contactId = contact.getString(0);
			String fullName = contact.getString(1);
			//Toast.makeText(context, "Received SMS from Responder with ID " + contactId+ ": " + body, Toast.LENGTH_LONG).show();
			if (isResponder(smsMessage)) {
				handleResponderSms(context, contactId, fullName, body);
			} else {
				// Not interested
			}
		}
	}
	
	public Cursor getContactForNumber(Context context, String phoneNumber) {
		 Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
		 ContentResolver cr = context.getContentResolver();
		 Cursor cursor = cr.query(uri, new String[]{PhoneLookup._ID, PhoneLookup.DISPLAY_NAME}, null, null, null);
		 if (cursor.moveToFirst()) {
			 return cursor;
		 } else {
			 return null;
		 }
	}
	
	public boolean isResponder(SmsMessage smsMessage) {
		// TODO implement
		return true;
	}
	
	public void handleResponderSms(Context context, String contactId, String contactName, String messageBody) {
		ResponseStatusModel rsm = new ResponseStatusModel(context.getContentResolver());
		// FIXME contactId can actually be a Long
		rsm.update((int)Integer.parseInt(contactId), ResponseStatusModel.RESPONDER_STATE_YES, messageBody, null, -1);
		Intent intent = new Intent();
		intent.setAction(HelloTabWidget.INTENT_UPDATE_LIST);
		intent.putExtra("contactId", contactId);
		intent.putExtra("contactName", contactName);
		intent.putExtra("smsBody", messageBody);
		context.sendBroadcast(intent);
	}
}
