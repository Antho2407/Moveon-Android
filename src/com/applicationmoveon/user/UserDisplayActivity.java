package com.applicationmoveon.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.applicationmoveon.R;
import com.applicationmoveon.ToolBox;
import com.applicationmoveon.UserSettingActivity;
import com.applicationmoveon.accueil.MainActivity;
import com.applicationmoveon.database.ExecTask;
import com.applicationmoveon.database.RequestTask;
import com.applicationmoveon.event.AddEventActivity;
import com.applicationmoveon.event.EventAdapter;
import com.applicationmoveon.event.EventDisplayActivity;
import com.applicationmoveon.localisation.MapLocateActivity;
import com.applicationmoveon.session.SessionManager;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SearchView.OnQueryTextListener;

public class UserDisplayActivity extends Activity{
	private class EventListOnItemClick implements OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
			Intent intent = new Intent(UserDisplayActivity.this,
					EventDisplayActivity.class);
			intent.putExtra("ID", String.valueOf(eventData.get(position).eventId));
			startActivity(intent);

		}
	}

	private ListView eventList;
	private EventAdapter mainAdapter;
	private UserAdapter.UserData user;
	private ArrayList<EventAdapter.EventData> eventData;
	private ToolBox tools;
	private SessionManager session;
	private String email = "";
	private String mail ="";
	private CheckBox followed;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_user);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		session = new SessionManager(this);
		email = session.getUserDetails().get(SessionManager.KEY_EMAIL);

		tools = new ToolBox(this);

		eventData = new ArrayList<EventAdapter.EventData>();
		mainAdapter = new EventAdapter(getApplicationContext(), eventData);

		eventList = (ListView)findViewById(R.id.eventList);
		eventList.setAdapter(mainAdapter);
		eventList.setOnItemClickListener(new EventListOnItemClick());
		//TODO Ajout de l'user		
		followed = (CheckBox) findViewById(R.id.user_followed);


		Bundle extras;
		if (savedInstanceState == null) {
			extras = getIntent().getExtras();
			if (extras == null) {
				mail = null;
			} else {
				mail = extras.getString("mail");
			}
		} else {
			mail = (String) savedInstanceState.getSerializable("mail");
		}


		try {
			try {
				getEvents(mail);
				getUser(mail);

				if(email.equals(user.email))
					followed.setVisibility(-1);
				else
				user.followed=followed();
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
		
		ImageView picture = (ImageView) findViewById(R.id.user_pic);
		TextView login = (TextView) findViewById(R.id.user_login);
		TextView eventNb = (TextView) findViewById(R.id.user_nb_event);

		user.eventOwned = eventData.size();
		if (user.picture==null)
			picture.setImageDrawable(getResources().getDrawable(R.drawable.ic_social_person));
		else
		picture.setImageDrawable(user.picture);
		login.setText(user.userFirstname+ " "+user.userName);
		eventNb.setText("Nombres d'evenements crées : "+user.eventOwned);

		followed.setChecked(user.followed);
		followed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked){
					suivi(1);
				}
				if (!isChecked){
					suivi(-1);
				}
				
			}
		});

	}

	@Override
	protected void onStart() {
		super.onStart();

		if(!tools.isOnline()){
			Intent intent = new Intent(UserDisplayActivity.this,
					com.applicationmoveon.InternetCheckActivity.class);
			intent.putExtra("KEY_PREVIOUS_ACTIVITY", this.getClass().getName());
			startActivity(intent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
		case R.id.menu_locate:
			intent = new Intent(this,MapLocateActivity.class);
		startActivity(intent);			return true;
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
		hm.put("Request", "SelectUserByEmail");
		hm.put("email", mail);


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

			JSONObject row_item = result.getJSONObject(0);
			String prenom = row_item.getString("firstname");
			String nom = row_item.getString("lastname");
			String _mail = row_item.getString("email");
			String picture_url = row_item.getString("imageprofile");
			//TODO recuperer image
			Drawable picture = null;

			UserAdapter.UserData newUser = new UserAdapter.UserData(prenom,nom,_mail, 0,picture, false);
			user = newUser;
		
		}
		return 1;


	}

	public int getEvents(String mail) throws InterruptedException, ExecutionException, JSONException{

		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("Request", "SelectEventByUserMail");
		hm.put("email", mail);

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
			String hourStart = row_item.getString("heure_debut");
			String hourEnd = row_item.getString("heure_fin");
			String location = row_item.getString("location");
			int id = Integer.parseInt(row_item.getString("id_event"));
			float latitude = Float.parseFloat(row_item.getString("latitude"));
			float longitude= Float.parseFloat(row_item.getString("longitude"));
			int state = Integer.parseInt(row_item.getString("state"));
			String dateCreation = row_item.getString("date_creation");
			float temperature = Float.parseFloat(row_item.getString("temperature"));
			int participants = Integer.parseInt(row_item.getString("participants"));
			EventAdapter.EventData newEvent = new EventAdapter.EventData(id, title, location, description, dateStart,
					hourStart, hourEnd, participants,"test",state,dateCreation, latitude, longitude,temperature,null);
			eventData.add(newEvent);
		}
		return 1;


	}
	
	public boolean followed() throws InterruptedException,
	ExecutionException, JSONException {

		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("Request", "SelectUsersFollowed");

		hm.put("email_user", email);

		RequestTask rt = new RequestTask();
		rt.execute(hm);


		JSONArray result = rt.get();

		if(result == null)
			return false;

		int length = result.length();

		if(length == 0)
			return false;

		for (int i = 0; i < length; i++) {

			JSONObject row_item = result.getJSONObject(i);
			String email = row_item.getString("email");
			if(email.equals(user.email))
				return true;

		}
		return false;

	}
	public void suivi(int req) {
		HashMap<String, String> hm = new HashMap<String, String>();
		if(req==1)
			hm.put("Request", "addSuivi");
		else
			hm.put("Request", "deleteSuivi");

		hm.put("email_user", email);
		hm.put("user_suivi", user.email);


		// Execution de la requête
		ExecTask rt = new ExecTask();
		Log.i("toto","avant exec");
		rt.execute(hm);
	}
}
