package org.rhok.mobilizationDisaster;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class TabAcceptedActivity extends Activity{
	private ArrayAdapter<String> mAdapter;
	private String[] mData;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        TextView textview = new TextView(this);
//        textview.setText("This is the Declined tab");
//        setContentView(textview);
        
	  ResponseStatusModel model = new ResponseStatusModel(getContentResolver());
	  
	  ListView lv = new ListView(this);
	  lv.setTextFilterEnabled(true);
	  mData = model.getYes();
	  mAdapter = new ArrayAdapter<String>(this, R.layout.list_item, mData);
	  lv.setAdapter(mAdapter);
	  setContentView(lv);
	  
	  lv.setOnItemClickListener(new OnItemClickListener() {
	    public void onItemClick(AdapterView<?> parent, View view,
	        int position, long id) {
	      //change item color
	    	//((TextView) view).setTextAppearance(getApplicationContext(), R.style.responseListItemAccepted);
	    	((TextView) view).setBackgroundResource(R.color.responseListItemAcceptedColor);
	      // When clicked, show a toast with the TextView text
	      Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
	          Toast.LENGTH_SHORT).show();
	    }
	  });
	  
	    IntentFilter filter = new IntentFilter();
	    filter.addAction(HelloTabWidget.INTENT_UPDATE_LIST);
	    registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				TabAcceptedActivity.this.update();
				String contactName = intent.getStringExtra("contactName");
				String response = intent.getStringExtra("smsBody");
				String incomingNotification = "Antwort von " + contactName + ": " + response;
				Toast.makeText(getApplicationContext(), incomingNotification,
				          Toast.LENGTH_SHORT).show();
			}
	    	
	    }, filter);
	}
	
	public void update() {
		  ResponseStatusModel model = new ResponseStatusModel(getContentResolver());
		  mData = model.getYes();
		  mAdapter.notifyDataSetChanged();
	}
	
	 @Override
	  public void onResume() {
		  super.onResume();
		  update();
	  }
}
