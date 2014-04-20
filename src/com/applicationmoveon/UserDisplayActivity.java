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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SearchView.OnQueryTextListener;

public class UserDisplayActivity extends Activity{
	private class EventListOnItemClick implements OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {


			Toast.makeText(getApplicationContext(), "Position " + position, Toast.LENGTH_SHORT).show();

		}
	}

	private ListView userList;
	private EventAdapter mainAdapter;
	private UserAdapter.UserData user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_user);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		ArrayList<EventAdapter.EventData> eventData = new ArrayList<EventAdapter.EventData>();
		for (int i=0;i<20;i++){
		//TODO ajouter event de user
		}
		mainAdapter = new EventAdapter(getApplicationContext(), eventData);

		userList = (ListView)findViewById(R.id.eventList);
		userList.setAdapter(mainAdapter);
		userList.setOnItemClickListener(new EventListOnItemClick());
//TODO Ajout de l'user		
		ImageView picture = (ImageView) findViewById(R.id.user_pic);
		TextView login = (TextView) findViewById(R.id.user_login);
		TextView eventNb = (TextView) findViewById(R.id.user_nb_event);
		CheckBox followed = (CheckBox) findViewById(R.id.user_followed);
		TextView desc = (TextView) findViewById(R.id.user_description);

		
		picture.setImageDrawable(user.picture);
		login.setText(user.userLogin);
		eventNb.setText("Nombres d'evenements crées : "+user.eventOwned);
		followed.setChecked(user.followed);
		desc.setText(user.userDescription);

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
