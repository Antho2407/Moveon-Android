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

import com.applicationmoveon.database.RequestTask;
import com.applicationmoveon.event.AddEventActivity;
import com.applicationmoveon.session.SessionManager;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SearchView.OnQueryTextListener;


public class ListUserActivity extends Activity{

	private class EventListOnItemClick implements OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
			Intent intent = new Intent(ListUserActivity.this,
					UserDisplayActivity.class); 
			intent.putExtra("mail",userData.get(position).email); 
			startActivity(intent);
			
		}
	}

	private ListView userList;
	private UserAdapter mainAdapter;
	private ArrayList<UserAdapter.UserData> userData;
	private ToolBox tools;
    public SessionManager session;
    private String email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listuser);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		tools = new ToolBox(this);

		userData = new ArrayList<UserAdapter.UserData>();
		mainAdapter = new UserAdapter(getApplicationContext(), userData);
		
		session = new SessionManager(this);
		session.checkLogin();
		email = session.getUserDetails().get(SessionManager.KEY_EMAIL);

		userList = (ListView)findViewById(R.id.userList);
		userList.setAdapter(mainAdapter);
		userList.setOnItemClickListener(new EventListOnItemClick());
		
		try {
			try {
				getUsers();
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
	
	@Override
	protected void onStart() {
		super.onStart();

		if(!tools.isOnline()){
			Intent intent = new Intent(ListUserActivity.this,
					com.applicationmoveon.InternetCheckActivity.class);
			intent.putExtra("KEY_PREVIOUS_ACTIVITY", this.getClass().getName());
			startActivity(intent);
		}
	}
	
	public int getUsers() throws InterruptedException, ExecutionException, JSONException{

		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("Request", "SelectUsersFollowed");
		hm.put("email_user", email);

		
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
			String prenom = row_item.getString("firstname");
			String nom = row_item.getString("lastname");
			String email = row_item.getString("email");
			//Drawable picture = row_item.getString("imageprofile");
			UserAdapter.UserData newUser = new UserAdapter.UserData(prenom,nom,email, 0,getResources().getDrawable(R.drawable.ic_social_person) , false);
			userData.add(newUser);
		}
		return 1;
		
		
	}
}
