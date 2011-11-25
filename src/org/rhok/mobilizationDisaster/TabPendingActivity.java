package org.rhok.mobilizationDisaster;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class TabPendingActivity extends ListActivity {
	
	private AwesomeCursorAdapter mAdapter;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
       
	  ResponseStatusModel model = new ResponseStatusModel(getContentResolver());
	  
	  ListView lv = getListView();
	  lv.setTextFilterEnabled(true);
	  mAdapter = new AwesomeCursorAdapter(this, model.getPending());
	  setListAdapter(mAdapter);
	  
	  // When clicked, show a toast with the TextView text
	  lv.setOnItemClickListener(new OnItemClickListener() {
		  
		  public void onItemClick(AdapterView<?> parent, View view, 
				  int position, long id) {
			  ((TextView) view).setBackgroundResource(R.color.responseListItemAcceptedColor);
			  
			  Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
					  Toast.LENGTH_SHORT).show();
		  }
		  
	  });
	  
	}
	
	public void update() {
		mAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		this.update();
  	}
	 
	@Override
	public void onStart() {
		super.onStart();
		this.update();
	}	
	
} // end class