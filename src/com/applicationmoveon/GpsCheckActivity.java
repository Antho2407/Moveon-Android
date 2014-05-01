package com.applicationmoveon;



import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class GpsCheckActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_gps);
		Button btn_parameter = (Button) findViewById(R.id.button_gps);
		btn_parameter.setOnClickListener(this);
		new GpsCheck(this).execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.facebook, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
		case R.id.menu_pref:
			intent = new Intent(GpsCheckActivity.this,UserSettingActivity.class);
			startActivity(intent);			return true;
		case android.R.id.home:
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View arg0) {
		Intent i = new Intent( android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS) ;
		startActivityForResult(i, 1);
	}

	private class GpsCheck extends AsyncTask<String, Integer, Object> {

		public GpsCheckActivity activityGpsCheck;
		public ToolBox tools;
		public String previousActivity = "";

		public GpsCheck(GpsCheckActivity a) {
			this.activityGpsCheck = a;
			tools = new ToolBox(a);
			previousActivity = getIntent().getStringExtra("KEY_PREVIOUS_ACTIVITY");
		}

		protected Object doInBackground(String... params) {
			while (!tools.gpsActivated()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return activityGpsCheck;
		}

		protected void onPostExecute(Object resultat) {
			if (resultat != null) {
				Intent intent;
				try {
					intent = new Intent(activityGpsCheck,
							Class.forName(previousActivity));
					activityGpsCheck.startActivity(intent);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}

			return;
		}

	}

}
