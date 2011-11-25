package org.rhok.mobilizationDisaster;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class TabStartActivity extends Activity{
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textview = new TextView(this);
        textview.setText("This is the Start tab");
        setContentView(textview);
    }
}
