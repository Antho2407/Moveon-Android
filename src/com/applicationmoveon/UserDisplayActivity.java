package com.applicationmoveon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.applicationmoveon.database.RequestTask;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
			Intent intent = new Intent(UserDisplayActivity.this,
					EventDisplayActivity.class);
			intent.putExtra("ID", eventData.get(position).eventId);
			startActivity(intent);

		}
	}

	private ListView eventList;
	private EventAdapter mainAdapter;
	private UserAdapter.UserData user;
	private ArrayList<EventAdapter.EventData> eventData;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_user);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		eventData = new ArrayList<EventAdapter.EventData>();
		mainAdapter = new EventAdapter(getApplicationContext(), eventData);
		
		eventList = (ListView)findViewById(R.id.eventList);
		eventList.setAdapter(mainAdapter);
		eventList.setOnItemClickListener(new EventListOnItemClick());
//TODO Ajout de l'user		
		/*
		 * Bundle extras;
		if (savedInstanceState == null) {
			extras = getIntent().getExtras();
			if (extras == null) {
				mail = null;
			} else {
				mail = extras.getString("MAIL");
			}
		} else {
			mail = (String) savedInstanceState.getSerializable("SEARCH");
		}
		 * try {
			try {
				getEvents(mail);
				getUser(mail);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 */
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
	
	public int getUser(String mail) throws InterruptedException, ExecutionException, JSONException{

		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("Request", "SelectUserByMail");
		
		// Execution de la requête
		RequestTask rt = new RequestTask();
		rt.execute(hm);
		
		JSONArray result = rt.get();

		if(result == null)
			return -1;
		
		int length = result.length();
		
		if(length == 0)
			return 0;

		if(length == 1) {

			JSONObject row_item = result.getJSONObject(1);
			String prenom = row_item.getString("firstname");
			String nom = row_item.getString("lastname");
			String email = row_item.getString("email");
			String mdp = row_item.getString("password");
			//Drawable picture = row_item.getString("imageprofile");
			
			UserAdapter.UserData newUser = new UserAdapter.UserData(0, nom+" "+prenom, "", 0,getResources().getDrawable(R.drawable.ic_action_content_event) , false);
			user = newUser;
		}
		return 1;
		
		
	}
	
	public int getEvents(String mail) throws InterruptedException, ExecutionException, JSONException{

		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("Request", "SelectsEventFromUserId");
		
		// Execution de la requête
		RequestTask rt = new RequestTask();
		rt.execute(hm);
		
		JSONArray result = rt.get();

		if(result == null)
			return -1;
		
		int length = result.length();
		
		if(length == 0)
			return 0;

		for (int i = 0; i < length; i++) {

			JSONObject row_item = result.getJSONObject(i);
			String title = row_item.getString("title");
			String description = row_item.getString("description");
			String dateStart = row_item.getString("date_debut");
			String dateEnd = row_item.getString("date_fin");
			String hourStart = row_item.getString("heure_debut");
			String hourEnd = row_item.getString("heure_fin");
			String location = row_item.getString("location");
			int id = Integer.parseInt(row_item.getString("id_event"));
			float latitude = Float.parseFloat(row_item.getString("latitude"));
			float longitude= Float.parseFloat(row_item.getString("longitude"));
			int state = Integer.parseInt(row_item.getString("state"));
			String dateCreation = row_item.getString("date_creation");
			int participants = Integer.parseInt(row_item.getString("participants"));
			EventAdapter.EventData newEvent = new EventAdapter.EventData(id, title, location, description, dateStart,
					hourStart, hourEnd, participants,"test",state,dateCreation, latitude, longitude,null);
			eventData.add(newEvent);
		}
		return 1;
		
		
	}
}
