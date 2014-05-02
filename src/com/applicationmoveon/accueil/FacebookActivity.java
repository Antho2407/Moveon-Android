package com.applicationmoveon.accueil;

import com.applicationmoveon.R;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.*;
import com.facebook.model.*;

public class FacebookActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.facebook_layout);
		// Se loger a Facebook
		Session.openActiveSession(this, true, new Session.StatusCallback() {

			// callback appelee si l'etat de la session change
			@Override
			public void call(Session session, SessionState state,
					Exception exception) {
				if (session.isOpened()) {
					// make request to the /me API
					Request.newMeRequest(session, new Request.GraphUserCallback() {

						  // callback after Graph API response with user object
						  @Override
						  public void onCompleted(GraphUser user, Response response) {
						    if (user != null) {
						    	Intent intent = new Intent(FacebookActivity.this, LoginActivity.class);
								intent.putExtra("EXTRA_NAME", user.getLastName());
								intent.putExtra("EXTRA_FIRSTNAME", user.getFirstName());
								intent.putExtra("EXTRA_ID",user.getId());
								setResult(0,intent);  
								finish();
						    }
						  }
					}).executeAsync();
				}else Toast.makeText(getApplicationContext(), "Connexion Facebook effectuée", Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.facebook, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.menu_pref) {
			return true;
		}
		if (id == android.R.id.home){
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_facebook,
					container, false);
			return rootView;
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
	}

}
