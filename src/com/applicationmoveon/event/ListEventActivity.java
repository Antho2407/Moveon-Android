package com.applicationmoveon.event;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.applicationmoveon.R;
import com.applicationmoveon.ToolBox;
import com.applicationmoveon.UserSettingActivity;
import com.applicationmoveon.R.id;
import com.applicationmoveon.R.layout;
import com.applicationmoveon.R.menu;
import com.applicationmoveon.database.ExecTask;
import com.applicationmoveon.database.RequestTask;
import com.applicationmoveon.session.SessionManager;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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


			Intent intent = new Intent(ListEventActivity.this,
					EventDisplayActivity.class);
			intent.putExtra("ID", String.valueOf(eventData.get(position).eventId));
			startActivity(intent);
		}
	}

	private ListView eventList;
	private EventAdapter mainAdapter;
	private ArrayList<EventAdapter.EventData> eventData;
	private ToolBox tools;
	private SessionManager session;
	private String email = "";
	private String type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listevent);
		Bundle extras = getIntent().getExtras();
		if(extras == null){
			return;
		}
		type = extras.getString("type");
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		tools = new ToolBox(this);
		session = new SessionManager(this);
		email = session.getUserDetails().get(SessionManager.KEY_EMAIL);

		eventData = new ArrayList<EventAdapter.EventData>();
		mainAdapter = new EventAdapter(getApplicationContext(), eventData);

		eventList = (ListView)findViewById(R.id.eventList);
		eventList.setAdapter(mainAdapter);
		eventList.setOnItemClickListener(new EventListOnItemClick());

		try {
			try {
				if (type.equals("followed"))
					getEventsFollowed();
				else
					getEvents();
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
	}

	@Override
	protected void onStart() {
		super.onStart();

		if(!tools.isOnline()){
			Intent intent = new Intent(ListEventActivity.this,
					com.applicationmoveon.InternetCheckActivity.class);
			intent.putExtra("KEY_PREVIOUS_ACTIVITY", this.getClass().getName());
			startActivity(intent);
		}
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

	public int getEvents() throws InterruptedException, ExecutionException, JSONException{

		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("Request", "SelectEventByUserMail");
		hm.put("email", email);

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
			float temperature = Float.parseFloat(row_item.getString("temperature"));

			int state = Integer.parseInt(row_item.getString("state"));
			String dateCreation = row_item.getString("date_creation");
			int participants = Integer.parseInt(row_item.getString("participants"));
			EventAdapter.EventData newEvent = new EventAdapter.EventData(id, title, location, description, dateStart,
					hourStart, hourEnd, participants,"test",state,dateCreation, latitude, longitude,temperature,null);
			eventData.add(newEvent);
		}
		return 1;


	}
	public int getEventsFollowed() throws InterruptedException, ExecutionException, JSONException{

		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("Request", "SelectEventByParticipation");
		hm.put("email", email);

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
			float temperature = Float.parseFloat(row_item.getString("temperature"));
			int state = Integer.parseInt(row_item.getString("state"));
			String dateCreation = row_item.getString("date_creation");
			int participants = Integer.parseInt(row_item.getString("participants"));
			EventAdapter.EventData newEvent = new EventAdapter.EventData(id, title, location, description, dateStart,
					hourStart, hourEnd, participants,"test",state,dateCreation, latitude, longitude,temperature,null);
			eventData.add(newEvent);
		}
		return 1;


	}
}
