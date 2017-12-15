package org.whistleblowers.snowden;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayText extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_text);

        Intent intent = getIntent();
        String text = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        TextView mTextView;
        mTextView = (TextView) findViewById(R.id.popuptextView);
        mTextView.setTextIsSelectable(true);

        mTextView.setText(text);
    }
}
