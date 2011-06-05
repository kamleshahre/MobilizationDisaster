package org.rhok.mobilizationDisaster;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

public class ResponseStatus extends ListActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  
	  ResponseStatusModel model = new ResponseStatusModel(getContentResolver());
	  
	  setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, model.getPending()));

	  ListView lv = getListView();
	  lv.setTextFilterEnabled(true);

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
	
	@Override
	public void onResume() {
		super.onResume();
		Toast.makeText(getApplicationContext(), "resumed!", Toast.LENGTH_SHORT).show();
		//Toast.makeText(ResponseStatus.this, "resumed!", Toast.LENGTH_SHORT).show();
    }

}
