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

public class TabAllActivity extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
		ResponseStatusModel model = new ResponseStatusModel(getContentResolver());
		GlobalState gs = (GlobalState) getApplication();
		
		ListView lv = new ListView(this);
		lv.setTextFilterEnabled(true);
		AwesomeCursorAdapter la = new AwesomeCursorAdapter(this, model.getAll());
		lv.setAdapter(la);
		setContentView(lv);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//change item color
				//((TextView) view).setTextAppearance(getApplicationContext(), R.style.responseListItemAccepted);
				((TextView) view).setBackgroundResource(R.color.responseListItemAcceptedColor);
				// When clicked, show a toast with the TextView text
				Toast.makeText(getApplicationContext(), ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
			}
		});
	}

}
