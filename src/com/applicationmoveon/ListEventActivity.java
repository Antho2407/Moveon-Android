package com.applicationmoveon;
import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;


public class ListEventActivity extends Activity {

	private class EventListOnItemClick implements OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {


			Toast.makeText(getApplicationContext(), "Position " + position, Toast.LENGTH_SHORT).show();

		}
	}

	private ListView eventList;
	private EventAdapter mainAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listevent);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		ArrayList<EventAdapter.EventData> eventData = new ArrayList<EventAdapter.EventData>();
		mainAdapter = new EventAdapter(getApplicationContext(), eventData);

		eventList = (ListView)findViewById(R.id.eventList);
		eventList.setAdapter(mainAdapter);
		eventList.setOnItemClickListener(new EventListOnItemClick());
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
			intent = new Intent(ListEventActivity.this,AddEventActivity.class);
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
