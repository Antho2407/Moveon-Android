package com.applicationmoveon;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.SearchView.OnQueryTextListener;

public class EventDisplayActivity extends Activity{



	private EventAdapter.EventData event;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_event);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		
		//TODO Ajout de l'event à l'activité	tache asynchrone? je pense que sans l'image pas besoin	
		ImageView picture = (ImageView) findViewById(R.id.event_pic);
		TextView title = (TextView) findViewById(R.id.event_title);
		TextView owner = (TextView) findViewById(R.id.event_owner);
		TextView desc = (TextView) findViewById(R.id.event_description);
		TextView date = (TextView) findViewById(R.id.event_date);
		Button participate = (Button) findViewById(R.id.event_participate);

		//TODO Image?
		//picture.setImageDrawable(event.);
		title.setText(event.eventTitle);
		owner.setText(event.eventOwner);
		desc.setText(event.eventDescription);
		date.setText("Le "+event.eventDateStart+" de "+event.eventHourStart+" à "+event.eventHourFinish+" à "+event.eventLocation);
		participate.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				if (v.getId() == R.id.event_participate){
					//TODO participer
					
				}
				
			}
			});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
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
		case R.id.menu_locate:
			//TODO lancer localisation
			return true;
		case R.id.menu_add:
			intent = new Intent(this,AddEventActivity.class);
			startActivity(intent);
			return true;
		case R.id.menu_pref:
			intent = new Intent(this,UserSettingActivity.class);
			startActivity(intent);
			return true;
		case android.R.id.home:
			this.finish();
			return true;


		}
		return super.onOptionsItemSelected(item);
	}
}
