//package com.applicationmoveon.accueil;
//
//import android.R;
//import android.os.Bundle;
//import android.app.Activity;
//import android.view.Menu;
//
//import com.facebook.*;
//import com.facebook.model.*;
//
//import android.widget.TextView;
//import android.widget.Toast;
//import android.content.Intent;
//
//public class FacebookActivityold extends Activity {
//	
//	public static final String mAPP_ID = "722576574443679";
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_facebook);
//
////		// Se loger a Facebook
////		Session.openActiveSession(this, true, new Session.StatusCallback() {
////
////			// callback appelee si l'etat de la session change
////			@Override
////			public void call(Session session, SessionState state,
////					Exception exception) {
////				if (session.isOpened()) {
////					Toast.makeText(getApplicationContext(), "OMG", Toast.LENGTH_SHORT).show();
////					// make request to the /me API
////					Request.newMeRequest(session, new Request.GraphUserCallback() {
////
////						  // callback after Graph API response with user object
////						  @Override
////						  public void onCompleted(GraphUser user, Response response) {
////						    if (user != null) {
////						      TextView welcome = (TextView) findViewById(R.id.welcome);
////						      welcome.setText("Hello " + user.getName() + "!");
////						    }
////						  }
////					}).executeAsync();
////				}else Toast.makeText(getApplicationContext(), "msg msg", Toast.LENGTH_SHORT).show();
////			}
////		});
//	}
//
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		Session.getActiveSession().onActivityResult(this, requestCode,
//				resultCode, data);
//	}
//
//}
