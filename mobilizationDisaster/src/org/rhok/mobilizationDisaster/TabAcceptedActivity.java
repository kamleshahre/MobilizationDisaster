package org.rhok.mobilizationDisaster;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class TabAcceptedActivity extends Activity{
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textview = new TextView(this);
        textview.setText("This is the Accepted tab");
        setContentView(textview);
    }
}
