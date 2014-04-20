package com.applicationmoveon;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.applicationmoveon.database.ExecTask;
import com.applicationmoveon.session.SessionManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.plus.PlusClient;

public class LoginActivity extends Activity implements View.OnClickListener,
ConnectionCallbacks, OnConnectionFailedListener {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_CODE_RESOLVE_ERR = 9000;

    private ProgressDialog mConnectionProgressDialog;
    private PlusClient mPlusClient;
    private ConnectionResult mConnectionResult;
	
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
        session = new SessionManager(LoginActivity.this);
        
        mPlusClient = new PlusClient.Builder(this, this, this)
        .setVisibleActivities("http://schemas.google.com/AddActivity", "http://schemas.google.com/BuyActivity")
        .build();
        
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        
		login = (EditText) findViewById(R.id.user_email);
		pass = (EditText) findViewById(R.id.user_password);
		final Button loginButton = (Button) findViewById(R.id.connect);
		final Button subscribeButton = (Button) findViewById(R.id.create_account);
		
		subscribeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this,
						AddUserActivity.class);
				startActivity(intent);
			}
		});
		
		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final String loginTxt = login.getText().toString();
				final String passTxt = pass.getText().toString();

				// V�rifier si l'un des deux champs est vide
				if (loginTxt.equals("") || passTxt.equals("")) {
					Toast.makeText(LoginActivity.this,
							R.string.email_or_password_empty,
							Toast.LENGTH_SHORT).show();
					return;
				}

				// On d�clare le pattern que l�on doit v�rifier
				Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
				
				// On d�clare un matcher, qui comparera le pattern avec la
				// string pass�e en argument
				Matcher m = p.matcher(loginTxt);
				
				// Si l�adresse mail saisie ne correspond au format d�une
				// adresse mail on un affiche un message � l'utilisateur
				if (!m.matches()) {
					Toast.makeText(LoginActivity.this,
							R.string.email_format_error, Toast.LENGTH_SHORT)
							.show();
					return;
				}
				
				try {
					if(!checkUser()){
						Toast.makeText(LoginActivity.this,
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

				Intent intent = new Intent(LoginActivity.this,
						MainActivity.class);
				
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
		
		//Execution de la requ�te
		ExecTask rt = new ExecTask();
		rt.execute(hm);
		return rt.get();
	}
	
	 @Override
	 protected void onStart() {
	        super.onStart();
	        mPlusClient.connect();
	}
	
	@Override
    protected void onStop() {
        super.onStop();
        mPlusClient.disconnect();
    }

	  @Override
	    public void onConnectionFailed(ConnectionResult result) {
	        if (result.hasResolution()) {
	            try {
	                result.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
	            } catch (SendIntentException e) {
	                mPlusClient.connect();
	            }
	        }
	        // Enregistrer le r�sultat et r�soudre l'�chec de connexion lorsque l'utilisateur clique.
	        mConnectionResult = result;
	    }

	    @Override
	    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
	        if (requestCode == REQUEST_CODE_RESOLVE_ERR && responseCode == RESULT_OK) {
	            mConnectionResult = null;
	            mPlusClient.connect();
	        }
	    }


	    @Override
	    public void onDisconnected() {
	        Log.d(TAG, "disconnected");
	    }

		@Override
		public void onConnected(Bundle connectionHint) {
			 String accountName = mPlusClient.getAccountName();
		     Toast.makeText(this, accountName + " is connected.", Toast.LENGTH_LONG).show();
			
		}

		@Override
		public void onClick(View v) {
			 if (v.getId() == R.id.sign_in_button && !mPlusClient.isConnected()) {
			        if (mConnectionResult == null) {
			            mConnectionProgressDialog.show();
			        } else {
			            try {
			                mConnectionResult.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
			            } catch (SendIntentException e) {
			                // Nouvelle tentative de connexion
			                mConnectionResult = null;
			                mPlusClient.connect();
			            }
			        }
			    }
		}
}