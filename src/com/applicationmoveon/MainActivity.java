package com.applicationmoveon;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.applicationmoveon.database.ExecTask;
import com.applicationmoveon.session.SessionManager;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	final String EXTRA_LOGIN = "user_login";
	final String EXTRA_PASSWORD = "user_password";
	
	private EditText login;
	private EditText pass;
	
	// Session Manager Class
    SessionManager session;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Session Manager
        session = new SessionManager(MainActivity.this);
        
		login = (EditText) findViewById(R.id.user_email);
		pass = (EditText) findViewById(R.id.user_password);
		final Button loginButton = (Button) findViewById(R.id.connect);
		final Button subscribeButton = (Button) findViewById(R.id.create_account);
		
		subscribeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						AddUserActivity.class);
				startActivity(intent);
			}
		});
		
		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final String loginTxt = login.getText().toString();
				final String passTxt = pass.getText().toString();

				// Vérifier si l'un des deux champs est vide
				if (loginTxt.equals("") || passTxt.equals("")) {
					Toast.makeText(MainActivity.this,
							R.string.email_or_password_empty,
							Toast.LENGTH_SHORT).show();
					return;
				}

				// On déclare le pattern que l’on doit vérifier
				Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
				
				// On déclare un matcher, qui comparera le pattern avec la
				// string passée en argument
				Matcher m = p.matcher(loginTxt);
				
				// Si l’adresse mail saisie ne correspond au format d’une
				// adresse mail on un affiche un message à l'utilisateur
				if (!m.matches()) {
					Toast.makeText(MainActivity.this,
							R.string.email_format_error, Toast.LENGTH_SHORT)
							.show();
					return;
				}
				
				try {
					if(!checkUser()){
						Toast.makeText(MainActivity.this,
								R.string.existing_account_error, Toast.LENGTH_SHORT)
								.show();
						return;
					}
				} catch (NotFoundException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
				
				session.createLoginSession(login.getText().toString());

				Intent intent = new Intent(MainActivity.this,
						AccueilActivity.class);
				
				intent.putExtra(EXTRA_LOGIN, loginTxt);
				intent.putExtra(EXTRA_PASSWORD, passTxt);
				startActivity(intent);
			}
		});
	}
	
	public boolean checkUser() throws InterruptedException, ExecutionException{
		HashMap<String, String> hm = new HashMap<String,String>();
		hm.put("Request","isValidCombination");
		hm.put("email", login.getText().toString());
		hm.put("password", pass.getText().toString());
		Log.i("ANTHO",login.getText().toString());
		Log.i("ANTHO",pass.getText().toString());
		
		//Execution de la requête
		ExecTask rt = new ExecTask();
		rt.execute(hm);
		return rt.get();
	}
}