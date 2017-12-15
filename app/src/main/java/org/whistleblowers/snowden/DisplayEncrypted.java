package org.whistleblowers.snowden;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DisplayEncrypted extends AppCompatActivity {


    String text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_encrypted);

        Intent intent = getIntent();
        text = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        TextView mTextView;
        mTextView = (TextView) findViewById(R.id.encrypted_textView);
        mTextView.setTextIsSelectable(true);
        mTextView.setText(text);

        Button mbutton = (Button) findViewById(R.id.encrypted_button);
        mbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                share();
            }
        });
    }

    public void share(){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");

        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
        startActivity(Intent.createChooser(sharingIntent, "Send Message Via:"));
    }
}
