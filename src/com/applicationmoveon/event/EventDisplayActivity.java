package com.applicationmoveon.event;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.applicationmoveon.R;
import com.applicationmoveon.ToolBox;
import com.applicationmoveon.UserSettingActivity;
import com.applicationmoveon.database.ExecTask;
import com.applicationmoveon.database.RequestTask;
import com.applicationmoveon.ftp.FtpDownloadTask;
import com.applicationmoveon.session.SessionManager;
import com.applicationmoveon.user.UserAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.SearchView.OnQueryTextListener;

public class EventDisplayActivity extends Activity {

	private EventAdapter.EventData event;
	private UserAdapter.UserData user;
	private String id;
	private ToolBox tools;
	private SessionManager session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_event);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		session = new SessionManager(this);
		session.checkLogin();
		
		tools = new ToolBox(this);

		// Ajout de l'event à l'activité
		Bundle extras;
		if (savedInstanceState == null) {
			extras = getIntent().getExtras();
			if (extras == null) {
				id = null;
			} else {
				id = getIntent().getStringExtra("ID");
			}
		} else {
			id = (String) savedInstanceState.getSerializable("SEARCH");
		}
		try {
			try {
				getEvent(id);
				getUser(event.eventOwner);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		ImageView picture = (ImageView) findViewById(R.id.event_pic);
		TextView title = (TextView) findViewById(R.id.event_title);
		Button owner = (Button) findViewById(R.id.event_owner_button);
		TextView desc = (TextView) findViewById(R.id.event_description);
		TextView date = (TextView) findViewById(R.id.event_date);
		Button participate = (Button) findViewById(R.id.event_participate);
		final CheckBox like = (CheckBox) findViewById(R.id.button_like);
		final CheckBox dislike = (CheckBox) findViewById(R.id.button_dislike);

		like.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked)
					dislike.setChecked(false);
				addVote(1);
//				try {
//					udpateTemperature();
//				} catch (JSONException e) {
//					e.printStackTrace();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				} catch (ExecutionException e) {
//					e.printStackTrace();
//				}
			}
		});
		dislike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked)
					like.setChecked(false);
				addVote(-1);
//				try {
//					udpateTemperature();
//				} catch (JSONException e) {
//					e.printStackTrace();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				} catch (ExecutionException e) {
//					e.printStackTrace();
//				}
			}
		});

		File mydir = tools.createCacheFolder();
		new FtpDownloadTask(event.eventOwner + "/" + event.url,
				mydir.getAbsolutePath() + "/" + event.url).execute();
		Bitmap myBitmap = tools.decodeSampledBitmapFromResource(
				mydir.getAbsolutePath() + "/" + event.url, 100, 100);
		picture.setImageBitmap(myBitmap);

		title.setText(event.eventTitle);
		owner.setText(user.userFirstname + " " + user.userName);
		desc.setText(event.eventDescription);
		date.setText("Le " + event.eventDateStart + " de "
				+ event.eventHourStart + " à " + event.eventHourFinish + " à "
				+ event.eventLocation);
		participate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v.getId() == R.id.event_participate) {
					HashMap<String, String> hm = new HashMap<String, String>();
					hm.put("Request", "addParticipants");
					hm.put("id_event", Integer.toString(event.eventId));
					hm.put("email", event.eventOwner);

					// Execution de la requête
					ExecTask rt = new ExecTask();
					rt.execute(hm);

					tools.alertUser("Participation envoyée",
							"Vous participez maintenant à l'évenement!");
				}

			}
		});
		owner.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				/*
				 * if (v.getId() == R.id.event_owner_button){ Intent intent =
				 * new Intent(ListEventActivity.this,
				 * EventDisplayActivity.class); intent.putExtra("MAIL",
				 * eventData.get(position).eventId); startActivity(intent);
				 * 
				 * }
				 */

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
				// TODO RECHERCHE
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
			// TODO lancer localisation
			return true;
		case R.id.menu_add:
			intent = new Intent(this, AddEventActivity.class);
			startActivity(intent);
			return true;
		case R.id.menu_pref:
			intent = new Intent(this, UserSettingActivity.class);
			startActivity(intent);
			return true;
		case android.R.id.home:
			this.finish();
			return true;

		}
		return super.onOptionsItemSelected(item);
	}

	public int getEvent(String id_event) throws InterruptedException,
			ExecutionException, JSONException {

		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("Request", "SelectEventById");
		hm.put("id_event", id_event);

		// Execution de la requête
		RequestTask rt = new RequestTask();
		rt.execute(hm);

		JSONArray result = rt.get();

		if (result == null)
			return -1;

		int length = result.length();

		if (length == 0)
			return 0;

		else {

			JSONObject row_item = result.getJSONObject(0);
			String title = row_item.getString("title");
			String description = row_item.getString("description");
			String dateStart = row_item.getString("date_debut");
			String dateEnd = row_item.getString("date_fin");
			String hourStart = row_item.getString("heure_debut");
			String hourEnd = row_item.getString("heure_fin");
			String location = row_item.getString("location");
			String mail = row_item.getString("id_createur");
			int id = Integer.parseInt(row_item.getString("id_event"));
			float latitude = Float.parseFloat(row_item.getString("latitude"));
			float longitude = Float.parseFloat(row_item.getString("longitude"));
			int state = Integer.parseInt(row_item.getString("state"));
			String dateCreation = row_item.getString("date_creation");
			String url = row_item.getString("urlimage");
			int participants = Integer.parseInt(row_item
					.getString("participants"));
			event = new EventAdapter.EventData(id, title, location,
					description, dateStart, hourStart, hourEnd, participants,
					mail, state, dateCreation, latitude, longitude, url);
		}
		return 1;

	}

	public int getUser(String mail) throws InterruptedException,
			ExecutionException, JSONException {

		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("Request", "SelectUserByEmail");
		hm.put("email", event.eventOwner);

		// Execution de la requête
		RequestTask rt = new RequestTask();
		rt.execute(hm);

		JSONArray result = rt.get();

		if (result == null)
			return -1;

		int length = result.length();

		if (length == 0)
			return 0;

		if (length > 0) {

			JSONObject row_item = result.getJSONObject(0);
			String prenom = row_item.getString("firstname");
			String nom = row_item.getString("lastname");
			String email = row_item.getString("email");
			String mdp = row_item.getString("password");
			// Drawable picture = row_item.getString("imageprofile");

			UserAdapter.UserData newUser = new UserAdapter.UserData(prenom,
					nom, 0, getResources().getDrawable(
							R.drawable.ic_action_content_event), false);
			user = newUser;
			Log.i("ANTHO", newUser.toString());
		}
		return 1;

	}

	public void addVote(int vote) {
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("Request", "addVote");
		hm.put("id_event", String.valueOf(event.eventId));
		hm.put("email", session.getUserDetails().get(SessionManager.KEY_EMAIL));
		hm.put("vote", String.valueOf(vote));

		// Execution de la requête
		ExecTask rt = new ExecTask();
		rt.execute(hm);

		try {
			if (!rt.get()) {
				tools.alertUser("Vote impossible",
						"Impossible d'enregistrer le vote pour cet évènement");
				return;
			} else {
				tools.alertUser("Vote effectué",
						"Votre vote a bien été pris en compte !");
				return;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	public void udpateTemperature() throws JSONException, InterruptedException, ExecutionException{
		
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("Request", "SelectEventById");
		hm.put("id_event", String.valueOf(event.eventId));

		// Execution de la requête
		RequestTask rt = new RequestTask();
		rt.execute(hm);

		JSONArray result = rt.get();
		JSONObject row_item = result.getJSONObject(0);

		int length = result.length();
		int likes = Integer.parseInt(row_item.getString("likes"));
		int dislikes = Integer.parseInt(row_item.getString("dislikes"));

		float newTemperature = TemperatureEvent.getTemperature(event.numberOfParticipants, likes, dislikes);
		hm = new HashMap<String,String>();
		hm.put("Request","updateTemperature");
		hm.put("id_event", String.valueOf(event.eventId));
		hm.put("temperature", String.valueOf(newTemperature));
		
		//Execution de la requête
		RequestTask rt2 = new RequestTask();
		rt2.execute(hm);
	}
}
