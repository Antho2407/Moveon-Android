package com.applicationmoveon.event;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
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
import com.applicationmoveon.user.UserDisplayActivity;

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
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.SearchView.OnQueryTextListener;

public class EventDisplayActivity extends Activity {

	private EventAdapter.EventData event;
	private UserAdapter.UserData user;
	private String id;
	private ToolBox tools;
	private SessionManager session;
	private String email;
	private TextView temperature;
	private int likes;
	private int dislikes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_event);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		session = new SessionManager(this);
		session.checkLogin();
		email = session.getUserDetails().get(SessionManager.KEY_EMAIL);

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
			id = (String) savedInstanceState.getString("ID");
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
		likes=event.likes;
		dislikes=event.dislikes;
		final View barre_verte = (View) findViewById(R.id.bar_green);
		final View barre_rouge = (View) findViewById(R.id.bar_red);
		final View barre_vide = (View) findViewById(R.id.bar_grey);
		if((likes+dislikes) == 0)
			barre_vide.setLayoutParams(new LinearLayout.LayoutParams(0, 4, 1));
		Log.i("ot", "avant      "+getDislikePercentage()+" fefhe"+event.dislikes);
		Log.i("ot", "avant      "+getLikePercentage()+" fefhe"+event.likes);

		barre_rouge.setLayoutParams(new LinearLayout.LayoutParams(0, 4, getDislikePercentage()));
		barre_verte.setLayoutParams(new LinearLayout.LayoutParams(0, 4, getLikePercentage()));
		Log.i("ot", "apres");
		Log.i("ot", "avant      "+getDislikePercentage()+" fefhe"+event.dislikes);
		Log.i("ot", "avant      "+getLikePercentage()+" fefhe"+event.likes);

		temperature = (TextView) findViewById(R.id.event_temp);
		final Button participate = (Button) findViewById(R.id.event_participate);
		final CheckBox like = (CheckBox) findViewById(R.id.button_like);
		final CheckBox dislike = (CheckBox) findViewById(R.id.button_dislike);
		try {
			if(participate()){
				participate.setVisibility(-1);
			}
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			

		like.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked){
					if (dislike.isChecked()){
						//bouton like vient d'etre activé et dislike etait activé donc +2
						dislikes--;
						likes++;
						barre_rouge.setLayoutParams(new LinearLayout.LayoutParams(0, 4, getDislikePercentage()));
						barre_verte.setLayoutParams(new LinearLayout.LayoutParams(0, 4, getLikePercentage()));
						addVote(2);
					}
					if (!dislike.isChecked()){
						//bouton like vient d'etre activé et dislike etait pas activé donc +1
						likes++;
						barre_verte.setLayoutParams(new LinearLayout.LayoutParams(0, 4, getLikePercentage()));
						addVote(1);
					}
					dislike.setChecked(false);
				}
				if (!isChecked){
					if (!dislike.isChecked()){
						//ne rien faire
						like.setChecked(true);
					}
				}
				barre_vide.setLayoutParams(new LinearLayout.LayoutParams(0, 4, 0));
				try {
					udpateTemperature();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		dislike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked){
					if (like.isChecked()){
						//bouton dislike vient d'etre activé et like etait activé donc -2
						likes--;
						dislikes++;
						barre_verte.setLayoutParams(new LinearLayout.LayoutParams(0, 4, getLikePercentage()));
						barre_rouge.setLayoutParams(new LinearLayout.LayoutParams(0, 4, getDislikePercentage()));
						addVote(-2);
					}
					if (!like.isChecked()){
						//bouton dislike vient d'etre activé et like etait pas activé donc -1
						dislikes++;
						barre_rouge.setLayoutParams(new LinearLayout.LayoutParams(0, 4, getDislikePercentage()));
						addVote(-1);
					}
					like.setChecked(false);
				}
				if (!isChecked){
					if (!like.isChecked()){
						//ne rien faire
						dislike.setChecked(true);
					}
				}
				barre_vide.setLayoutParams(new LinearLayout.LayoutParams(0, 4, 0));
				try {
					udpateTemperature();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
		temperature.setText(event.temperature+"%");
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
					participate.setVisibility(-1);
					//TODO UPDATE
				}

			}
		});
		owner.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				 if (v.getId() == R.id.event_owner_button){ Intent intent =
				 new Intent(EventDisplayActivity.this,
				 UserDisplayActivity.class); intent.putExtra("mail",
				 user.email); startActivity(intent);
				 
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
			float temperature = Float.parseFloat(row_item.getString("temperature"));
			int participants = Integer.parseInt(row_item
					.getString("participants"));
			int likes = Integer.parseInt(row_item
					.getString("likes"));
			int dislikes = Integer.parseInt(row_item
					.getString("dislikes"));
			event = new EventAdapter.EventData(id, title, location,
					description, dateStart, hourStart, hourEnd, participants,
					mail, state, dateCreation, latitude, longitude, temperature,url,likes,dislikes);
			Log.i("test", mail);
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
			String _email = row_item.getString("email");
			// Drawable picture = row_item.getString("imageprofile");

			UserAdapter.UserData newUser = new UserAdapter.UserData(prenom,
					nom,_email, 0, getResources().getDrawable(
							R.drawable.ic_action_content_event), false);
			user = newUser;
		}
		return 1;

	}
	
	public boolean participate() throws InterruptedException,
	ExecutionException, JSONException {

		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("Request", "SelectEventByParticipation");

		hm.put("email", email);

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
			int id = row_item.getInt("id_event");
			if(id==event.eventId)
				return true;

		}
		return false;

	}

	public void addVote(int vote) {
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("Request", "addVote");
		hm.put("id_event", String.valueOf(event.eventId));
		hm.put("email", email);
		hm.put("vote", String.valueOf(vote));

		// Execution de la requête
		ExecTask rt = new ExecTask();
		rt.execute(hm);
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
		temperature.setText(newTemperature+"%");
	}

	private float getDislikePercentage(){
		if(dislikes==0)
			return 0;
		float total = likes+dislikes;
		return dislikes/total;
	}
	
	private float getLikePercentage(){
		if(likes==0)
			return 0;
		float total = likes+dislikes;
		return likes/total;
	}
}
