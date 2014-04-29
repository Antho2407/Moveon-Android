package com.applicationmoveon;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.applicationmoveon.accueil.FacebookActivity;
import com.applicationmoveon.accueil.MainActivity;
import com.applicationmoveon.database.ExecTask;
import com.applicationmoveon.event.AddEventActivity;
import com.applicationmoveon.localisation.MapActivity;
import com.applicationmoveon.session.SessionManager;
import com.applicationmoveon.user.AddUserActivity;

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
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.plus.PlusClient;

public class LoginActivity extends Activity implements
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
	
	private int RESULT_FACEBOOK = 0;
	
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
        .setScopes(Scopes.PLUS_LOGIN)
        .build();
        
		login = (EditText) findViewById(R.id.user_email);
		pass = (EditText) findViewById(R.id.user_password);
		
		final Button loginButton = (Button) findViewById(R.id.connect);
		final Button subscribeButton = (Button) findViewById(R.id.create_account);
		final SignInButton googleplusco = (SignInButton)findViewById(R.id.sign_in_button);
		final ImageButton facebookConnect = (ImageButton)findViewById(R.id.connectFacebook);
		
		googleplusco.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				 if (v.getId() == R.id.sign_in_button && !mPlusClient.isConnected()) {
				        if (mConnectionResult == null) {
				            mConnectionProgressDialog.show();
				        } else {
				            try {
				                mConnectionResult.startResolutionForResult(LoginActivity.this, REQUEST_CODE_RESOLVE_ERR);
				            } catch (SendIntentException e) {
				                // Nouvelle tentative de connexion
				                mConnectionResult = null;
				                mPlusClient.connect();
				            }
				        }
				    }
				
			}
			
		});
		facebookConnect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(LoginActivity.this, FacebookActivity.class);
				startActivityForResult(i, RESULT_FACEBOOK);
			}
		});
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

				// Vérifier si l'un des deux champs est vide
				if (loginTxt.equals("") || passTxt.equals("")) {
					Toast.makeText(LoginActivity.this,
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
		
		//Execution de la requête
		ExecTask rt = new ExecTask();
		rt.execute(hm);
		return rt.get();
	}
	
	 @Override
	 protected void onStart() {
	        super.onStart();
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
	        // Enregistrer le résultat et résoudre l'échec de connexion lorsque l'utilisateur clique.
	        mConnectionResult = result;
	    }

	    @Override
	    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
	        if (requestCode == REQUEST_CODE_RESOLVE_ERR && responseCode == RESULT_OK) {
	            mConnectionResult = null;
	            mPlusClient.connect();
	        }
	        if (requestCode == RESULT_FACEBOOK
					&& null != intent) {
				String name = intent.getStringExtra("EXTRA_NAME");
				String firstname = intent.getStringExtra("EXTRA_FIRSTNAME");
				String id = intent.getStringExtra("EXTRA_ID");
				
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put("Request", "addUser");
				hm.put("lastname", name);
				hm.put("firstname", firstname);
				hm.put("password", "facebook");
				hm.put("email",id);
				hm.put("urlimage", "");

				// Execution de la requête
				ExecTask rt = new ExecTask();
				rt.execute(hm);
				
				session.createLoginSession(id);

				Intent i = new Intent(LoginActivity.this,
						MainActivity.class);
				startActivity(i);
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

}