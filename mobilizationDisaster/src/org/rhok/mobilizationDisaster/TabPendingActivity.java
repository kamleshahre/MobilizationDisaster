package org.rhok.mobilizationDisaster;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class TabPendingActivity extends Activity{
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textview = new TextView(this);
        textview.setText("This is the Pending tab");
        setContentView(textview);
        
	  ResponseStatusModel model = new ResponseStatusModel(getContentResolver());
	  
	  ListView lv = new ListView(this);
	  lv.setTextFilterEnabled(true);
	  ArrayAdapter<String> la = new ArrayAdapter<String>(this, R.layout.list_item, model.getPending());
	  lv.setAdapter(la);
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
        
    }
}
