package com.applicationmoveon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class LoginDisplayActivity extends Activity {

	final String EXTRA_LOGIN = "user_login";
	final String EXTRA_PASSWORD = "user_password";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_display);

       Intent intent = getIntent();
       TextView loginDisplay = (TextView) findViewById(R.id.email_display);
       TextView passwordDisplay = (TextView) findViewById(R.id.password_display);
       
       if (intent != null) {
    	   loginDisplay.setText(intent.getStringExtra(EXTRA_LOGIN));
    	   passwordDisplay.setText(intent.getStringExtra(EXTRA_PASSWORD));
       }
    }
}
