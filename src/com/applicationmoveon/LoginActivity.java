package com.applicationmoveon;

import java.io.IOException;
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
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
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
    
 // A magic number we will use to know that our sign-in error
 	// resolution activity has completed.
 	private static final int OUR_REQUEST_CODE = 49404;
 	
 // A flag to stop multiple dialogues appearing for the user.
 	private boolean mResolveOnFail;

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
		mConnectionProgressDialog = new ProgressDialog(this);
		mConnectionProgressDialog.setMessage("Signing in...");

		
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
				Log.v(TAG, "Tapped sign in");
				
				if (!mPlusClient.isConnected()) {
					// Show the dialog as we are now signing in.
					mConnectionProgressDialog.show();
					// Make sure that we will start the resolution (e.g. fire the
					// intent and pop up a dialog for the user) for any errors
					// that come in.
					mResolveOnFail = true;
					// We should always have a connection result ready to resolve,
					// so we can start that process.
					if (mConnectionResult != null) {
						startResolution();
					} else {
						// If we don't have one though, we can start connect in
						// order to retrieve one.
						mPlusClient.connect();
					}
				}
				
				HashMap<String,String> hm = new HashMap<String,String>();
				
				if(!userAlreadyExists(mPlusClient.getAccountName())){
					hm.put("Request", "addUser");
					hm.put("lastname", mPlusClient.getCurrentPerson().getName().getFamilyName());
					hm.put("firstname", mPlusClient.getCurrentPerson().getName().getGivenName());
					hm.put("password", "google");
					hm.put("email",mPlusClient.getAccountName());
					hm.put("urlimage", "");
	
					// Execution de la requête
					ExecTask rt = new ExecTask();
					rt.execute(hm);
				}
				
				session.createLoginSession(mPlusClient.getAccountName());
				
				
				
				
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
	        Log.v(TAG, "Start");
			// Every time we start we want to try to connect. If it
			// succeeds we'll get an onConnected() callback. If it
			// fails we'll get onConnectionFailed(), with a result!
			mPlusClient.connect();
	}
	
	@Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "Stop");
		// It can be a little costly to keep the connection open
		// to Google Play Services, so each time our activity is
		// stopped we should disconnect.
		mPlusClient.disconnect();
    }	

	  @Override
	    public void onConnectionFailed(ConnectionResult result) {
		  Log.v(TAG, "ConnectionFailed");
			// Most of the time, the connection will fail with a
			// user resolvable result. We can store that in our
			// mConnectionResult property ready for to be used
			// when the user clicks the sign-in button.
			if (result.hasResolution()) {
				mConnectionResult = result;
				if (mResolveOnFail) {
					// This is a local helper function that starts
					// the resolution of the problem, which may be
					// showing the user an account chooser or similar.
					startResolution();
				}
			}
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
				
				
				if(!userAlreadyExists(id)){
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
				}
			
				
				session.createLoginSession(id);

				
				
			}
	    }
	    
	    public Boolean userAlreadyExists(String id){
	    	HashMap<String, String> testuser = new HashMap<String, String>();
	    	testuser.put("Request", "doesUserAlreadyExists");
			testuser.put("email", id);
			ExecTask rtuser = new ExecTask();
			rtuser.execute(testuser);
			Boolean isAlreadyCreated = true;
				try {
					isAlreadyCreated = rtuser.get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			return isAlreadyCreated;
	    }

	    @Override
	    public void onDisconnected() {
	        Log.d(TAG, "disconnected");
	    }

	    @Override
		public void onConnected(Bundle bundle) {
			// Yay! We can get the oAuth 2.0 access token we are using.
			Log.v(TAG, "Connected. Yay!");
	 
			// Turn off the flag, so if the user signs out they'll have to
			// tap to sign in again.
			mResolveOnFail = false;
	 
			// Hide the progress dialog if its showing.
			mConnectionProgressDialog.dismiss();
	 
	 
			// Retrieve the oAuth 2.0 access token.
			final Context context = this.getApplicationContext();
			AsyncTask task = new AsyncTask() {
				@Override
				protected Object doInBackground(Object... params) {
					String scope = "oauth2:" + Scopes.PLUS_LOGIN;
					try {
						// We can retrieve the token to check via
						// tokeninfo or to pass to a service-side
						// application.
						String token = GoogleAuthUtil.getToken(context,
								mPlusClient.getAccountName(), scope);
					} catch (UserRecoverableAuthException e) {
						// This error is recoverable, so we could fix this
						// by displaying the intent to the user.
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (GoogleAuthException e) {
						e.printStackTrace();
					}
					return null;
				}
			};
			task.execute((Void) null);
		}
	    
	    private void startResolution() {
			try {
				// Don't start another resolution now until we have a
				// result from the activity we're about to start.
				mResolveOnFail = false;
				// If we can resolve the error, then call start resolution
				// and pass it an integer tag we can use to track. This means
				// that when we get the onActivityResult callback we'll know
				// its from being started here.
				mConnectionResult.startResolutionForResult(this, OUR_REQUEST_CODE);
			} catch (SendIntentException e) {
				// Any problems, just try to connect() again so we get a new
				// ConnectionResult.
				mPlusClient.connect();
			}
		}

}