package com.applicationmoveon;


import java.util.ArrayList;

import com.applicationmoveon.session.SessionManager;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SearchView.OnQueryTextListener;



public class AccueilActivity extends Activity{
	
	 // Session Manager Class
    SessionManager session;
    
	private class GridOnItemClick implements OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
			Intent intent = null;
			switch(position){
			case 0:
				break;
			case 1:
				intent = new Intent(AccueilActivity.this,ListEventActivity.class);
				startActivity(intent);
				break;
			case 2:
				intent = new Intent(AccueilActivity.this,UserSettingActivity.class);
				startActivity(intent);
				break;
			case 3:
				break;
			case 4:
				intent = new Intent(AccueilActivity.this,ListUserActivity.class);
				startActivity(intent);
				break;
			case 5:
				break;
			}
			//startActivity(intent);
		}
	}
	GridView gridView;
	ArrayList<Item> gridArray = new ArrayList<Item>();
	CustomGridViewAdapter customGridAdapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		session = new SessionManager(AccueilActivity.this);
		session.checkLogin();
		
		setContentView(R.layout.activity_accueil);

		//set grid view item
		Bitmap homeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_action_collections_go_to_today);
		Bitmap userIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_social_person);
		Bitmap settingsIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_action_settings);
		Bitmap eventIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_action_content_event);
		Bitmap usersIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_social_group);


		gridArray.add(new Item(userIcon,"Profil"));
		gridArray.add(new Item(eventIcon,"My Events"));
		gridArray.add(new Item(settingsIcon,"Settings"));
		gridArray.add(new Item(homeIcon,"menu 4"));
		gridArray.add(new Item(usersIcon,"Users followed"));
		gridArray.add(new Item(homeIcon,"menu 6"));

		gridView = (GridView) findViewById(R.id.gridView1);
		customGridAdapter = new CustomGridViewAdapter(this, R.layout.row_grid, gridArray);
		gridView.setAdapter(customGridAdapter);
		gridView.setOnItemClickListener(new GridOnItemClick());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_accueil, menu);
		MenuItem itemSearch = menu.findItem(R.id.menu_search);
		SearchView mSearchView = (SearchView) itemSearch.getActionView();
		mSearchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				//TODO RECHERCHE
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
		case R.id.menu_add:
			intent = new Intent(AccueilActivity.this,AddEventActivity.class);
			startActivity(intent);
			return true;
		case R.id.menu_pref:
			intent = new Intent(AccueilActivity.this,UserSettingActivity.class);
			startActivity(intent);			return true;
		case android.R.id.home:
			this.finish();
			return true;


		}
		return super.onOptionsItemSelected(item);
	}

	public void onResume() {
		super.onResume();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		int minutes = 0;
		if(prefs.getBoolean("prefNotification", false)){
			minutes=Integer.parseInt(prefs.getString("prefNotificationFrequency", ""));
		}
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		Intent i = new Intent(this, NotificationService.class);
		PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
		am.cancel(pi);
		if (minutes > 0) { 
			am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + minutes*60*1000, minutes*60*1000, pi);
		}
	}
}