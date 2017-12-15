package org.whistleblowers.snowden;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import org.jasypt.util.text.BasicTextEncryptor;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "org.whistleblowers.snowden.TEXT";
    SharedPreferences snowden_sharedPref;
    SharedPreferences.Editor sharedPref_editor;
    private TextView mTextMessage;
    private Button mButton,popupButton;
    public final BasicTextEncryptor encryptor = new BasicTextEncryptor(); //why cant I use static
    FloatingActionButton mActionButton;
    LinearLayout layout_container;
    PopupWindow popupWindow;
    LinearLayout mainLayout;
    LayoutInflater inflater;
    View popupView;
    EditText  newKey;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText("Encryption Tool");         //add this string in resources/strings
                    mButton.setText("Encrypt");
                   // mButton.setBackgroundColor(Color.RED);
                    mButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            // Code here executes on main thread after user presses button
                            encrypt(view);

                        }
                    });
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText("Decryption Tool");          //add this string in resources/strings
                    mButton.setText("Decrypt");
                   // mButton.setBackgroundColor(Color.GREEN);
                    mButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            // Code here executes on main thread after user presses button
                            decrypt(view);
                        }
                    });
                    return true;
                /*case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    mButton.setText("Notification_Button");
                    mButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            // Code here executes on main thread after user presses button
                        }
                    });
                    return true;*/
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout_container = (LinearLayout) findViewById( R.id.container);
        layout_container.getForeground().setAlpha( 0);

        mTextMessage = (TextView) findViewById(R.id.message);
        mButton = (Button) findViewById(R.id.button);

        snowden_sharedPref= this.getPreferences(Context.MODE_PRIVATE);
        sharedPref_editor=snowden_sharedPref.edit();
       encryptor.setPassword(snowden_sharedPref.getString(getString(R.string.encryption_key),"HelloWorld"));


        mActionButton= (FloatingActionButton) findViewById(R.id.ActionButton);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mActionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onActionButtonClick(view);
            }
        });



        // get a reference to the already created main layout
        mainLayout = (LinearLayout) findViewById(R.id.container);

        // inflate the layout of the popup window
        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.popup_addkey, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        popupWindow = new PopupWindow(popupView, width, height, focusable);


        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                layout_container.getForeground().setAlpha( 0);

            }
        });


    }



    public void encrypt(View view){
        Intent intent = new Intent(MainActivity.this, DisplayEncrypted.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String text = editText.getText().toString();
        text = encryptor.encrypt(text);
        intent.putExtra(EXTRA_MESSAGE, text);
        startActivity(intent);
    }



    public void decrypt(View view){
        Intent intent = new Intent(MainActivity.this, DisplayText.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String text = editText.getText().toString();
        try {
            text = encryptor.decrypt(text);
        }
        catch(Exception e){
            text="That's embarrassing! We were unable to decode it!\n" +
                    "Perhaps this is not an encoded text.";
        }
        intent.putExtra(EXTRA_MESSAGE, text);
        startActivity(intent);
    }


    public void onActionButtonClick (View view) {
        //sharedPref_editor=snowden_sharedPref.edit();
        popupWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);
        layout_container.getForeground().setAlpha(150);

        newKey = (EditText) popupView.findViewById(R.id.popupeditText);
        newKey.setHint(snowden_sharedPref.getString(getString(R.string.encryption_key),"HelloWorld"));
        newKey.setText("");

        popupButton = (Button) popupView.findViewById(R.id.popupbutton);


        popupButton.setOnClickListener(new View.OnClickListener() {
          public void onClick(View view) {
                // Code here executes on main thread after user presses button
                String newKeyString= newKey.getText().toString();
                if (!newKeyString.equals("")){
                    sharedPref_editor.putString(getString(R.string.encryption_key),newKeyString);
                    sharedPref_editor.commit();
                }
                encryptor.setPassword(snowden_sharedPref.getString(getString(R.string.encryption_key),"HelloWorld"));
                popupWindow.dismiss();
           }
        });

    }
}
