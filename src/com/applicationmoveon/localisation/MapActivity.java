package com.applicationmoveon.localisation;

import java.io.IOException;
import java.util.List;

import com.applicationmoveon.R;
import com.applicationmoveon.R.id;
import com.applicationmoveon.R.layout;
import com.applicationmoveon.R.menu;
import com.applicationmoveon.ToolBox;
import com.applicationmoveon.event.AddEventActivity;
import com.applicationmoveon.event.ListEventActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.ActionBar;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.SearchView.OnQueryTextListener;

public class MapActivity extends FragmentActivity {

	private GoogleMap map;
	private MarkerOptions markerOptions;
	private LatLng locationMap;
	private List<Address> addresses;
	private Button btn_find;
	private Button btn_next;
	private Button btn_previous;
	private Button btn_choose;

	private int cursor;
	private Double finalLatitude = 0.0;
	private Double finalLongitude = 0.0;
	private String finalAdressString = "";

	private Address currentAddress;
	
	private ToolBox tools;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		tools = new ToolBox(this);

		// Recuperer le fragment de la map
		SupportMapFragment supportMapFragment = (SupportMapFragment) 
				getSupportFragmentManager().findFragmentById(R.id.map);

		// Recuperer la map
		map = supportMapFragment.getMap();

		btn_find = (Button) findViewById(R.id.btn_find);
		btn_next = (Button) findViewById(R.id.btn_next);
		btn_previous = (Button) findViewById(R.id.btn_previous);
		btn_choose = (Button) findViewById(R.id.btn_validate);

		OnClickListener findClickListener = new OnClickListener() {            
			@Override
			public void onClick(View v) {

				cursor = 0;
				map.clear();

				EditText etLocation = (EditText) findViewById(R.id.et_location);

				// Recuperer l'adresse demandee
				String location = etLocation.getText().toString();

				if(location!=null && !location.equals("")){
					new LocateTask().execute(location);
				}
			}
		};

		OnClickListener previousClickListener = new OnClickListener() {            
			@Override
			public void onClick(View v) {
				cursor = (cursor - 1);
				if(cursor < 0)
					cursor = addresses.size()-1;
				currentAddress = addresses.get(cursor);
				locationMap = new LatLng(currentAddress.getLatitude(), currentAddress.getLongitude());
				map.animateCamera(CameraUpdateFactory.newLatLng(locationMap)); 
			}
		};

		OnClickListener nextClickListener = new OnClickListener() {            
			@Override
			public void onClick(View v) {
				cursor = (cursor + 1) % (addresses.size());
				currentAddress = addresses.get(cursor);
				locationMap = new LatLng(currentAddress.getLatitude(), currentAddress.getLongitude());
				map.animateCamera(CameraUpdateFactory.newLatLng(locationMap));  
			}
		};

		OnClickListener chooseClickListener = new OnClickListener() {            
			@Override
			public void onClick(View v) {
				finalLatitude = currentAddress.getLatitude();
				finalLongitude = currentAddress.getLongitude();
				finalAdressString = String.format("%s, %s",
						currentAddress.getMaxAddressLineIndex() > 0 ? currentAddress.getAddressLine(0) : "",
								currentAddress.getCountryName());
				Intent intent = new Intent(MapActivity.this, AddEventActivity.class);
				intent.putExtra("EXTRA_LONG", Double.toString(finalLongitude));
				intent.putExtra("EXTRA_LAT", Double.toString(finalLatitude));
				intent.putExtra("EXTRA_ADDRESS",finalAdressString);
				setResult(1,intent);  
				//map.stopAnimation();
				finish();
			}
		};

		btn_find.setOnClickListener(findClickListener); 
		btn_previous.setOnClickListener(previousClickListener); 
		btn_next.setOnClickListener(nextClickListener); 
		btn_choose.setOnClickListener(chooseClickListener); 
	}
	
	@Override
	protected void onStart() {
		super.onStart();

		if(!tools.isOnline()){
			Intent intent = new Intent(MapActivity.this,
					com.applicationmoveon.InternetCheckActivity.class);
			intent.putExtra("KEY_PREVIOUS_ACTIVITY", this.getClass().getName());
			startActivity(intent);
		}
	}

	private class LocateTask extends AsyncTask<String, Void, List<Address>> {

		@Override
		protected List<Address> doInBackground(String... locationName) {

			Geocoder geocoder = new Geocoder(getBaseContext());
			addresses = null;

			btn_previous.setVisibility(View.GONE);
			btn_next.setVisibility(View.GONE);
			btn_choose.setVisibility(View.GONE);

			try {
				// Recuperer 5 adresses possibles
				addresses = geocoder.getFromLocationName(locationName[0], 5);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return addresses;
		}

		@Override
		protected void onPostExecute(List<Address> addresses) {

			if (addresses == null || addresses.size() == 0) {
				Toast.makeText(getBaseContext(), "Pas d'adresse trouvée",
						Toast.LENGTH_SHORT).show();
			}

			// Faire disparaitre les marqueurs de la map
			map.clear();

			// Ajouter un nouveau marqueur par adresse
			for (int i = 0; i < addresses.size(); i++) {

				currentAddress = (Address) addresses.get(i);

				locationMap = new LatLng(currentAddress.getLatitude(),
						currentAddress.getLongitude());

				finalAdressString = String.format(
						"%s, %s",
						currentAddress.getMaxAddressLineIndex() > 0 ? currentAddress
								.getAddressLine(0) : "", currentAddress
								.getCountryName());

				markerOptions = new MarkerOptions();
				markerOptions.position(locationMap);
				markerOptions.title(finalAdressString);

				map.addMarker(markerOptions);

				// Deplacer la map a la premiere adresse
				if (i == 0)
					map.animateCamera(CameraUpdateFactory
							.newLatLng(locationMap));
				
				// Si des adresses sont trouvees les boutons apparaissent
				btn_previous.setVisibility(View.VISIBLE);
				btn_next.setVisibility(View.VISIBLE);
				btn_choose.setVisibility(View.VISIBLE);
			}
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
			intent = new Intent(MapActivity.this, AddEventActivity.class);
			startActivity(intent);
			return true;
		case R.id.menu_pref:
			// TODO ALLER A PREFERENCES
			return true;
		case android.R.id.home:
			this.finish();
			return true;

		}
		return super.onOptionsItemSelected(item);
	}
}
