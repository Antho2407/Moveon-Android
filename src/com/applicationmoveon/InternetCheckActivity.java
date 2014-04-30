package com.applicationmoveon;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class InternetCheckActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_internet);
		Button btn_parameter = (Button) findViewById(R.id.button_internet);
		btn_parameter.setOnClickListener(this);
		new InternetCheck(this).execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public void onClick(View arg0) {
		Intent i = new Intent( android.provider.Settings.ACTION_WIFI_SETTINGS) ;
        startActivityForResult(i, 1);
	}

	private class InternetCheck extends AsyncTask<String, Integer, Object> {

		public InternetCheckActivity activityInternetCheck;
		public ToolBox tools;
		public String previousActivity = "";

		public InternetCheck(InternetCheckActivity a) {
			this.activityInternetCheck = a;
			tools = new ToolBox(a);
			previousActivity = getIntent().getStringExtra("KEY_PREVIOUS_ACTIVITY");
		}

		protected Object doInBackground(String... params) {
			while (!tools.isOnline()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return activityInternetCheck;
		}

		protected void onPostExecute(Object resultat) {
			if (resultat != null) {
				Intent intent;
				try {
					intent = new Intent(activityInternetCheck,
							Class.forName(previousActivity));
				activityInternetCheck.startActivity(intent);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}

			return;
		}

	}

}
