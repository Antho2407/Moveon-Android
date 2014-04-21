package com.applicationmoveon.localisation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.applicationmoveon.R;
import com.applicationmoveon.R.id;
import com.applicationmoveon.R.layout;
import com.applicationmoveon.R.menu;
import com.applicationmoveon.database.RequestTask;
import com.applicationmoveon.event.AddEventActivity;
import com.applicationmoveon.event.EventAdapter;
import com.applicationmoveon.event.EventDisplayActivity;
import com.applicationmoveon.event.TemperatureEvent;
import com.applicationmoveon.event.EventAdapter.EventData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.ActionBar;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

public class MapLocateActivity extends FragmentActivity implements
		LocationListener {

	private GoogleMap map;
	private MarkerOptions markerOptions;
	private LatLng locationMap;
	private List<Location> addresses;
	private Button btn_next;
	private Button btn_previous;
	private Button btn_choose;

	private Location myLocation;

	private int cursor;

	private Location currentAddress;

	private Geocoder geocoder;

	private CircleOptions circle;
	
	private HashMap<Location,EventAdapter.EventData> listEvents;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		listEvents = new HashMap<Location,EventAdapter.EventData>();
		setContentView(R.layout.activity_map_locate);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		geocoder = new Geocoder(getBaseContext());
		addresses = new ArrayList<Location>();

		// Recuperer le fragment de la map
		SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map_locate);

		// Recuperer la map
		map = supportMapFragment.getMap();

		map.setMyLocationEnabled(true);

		/*****/

		// Getting LocationManager object from System Service LOCATION_SERVICE
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		// Creating a criteria object to retrieve provider
		Criteria criteria = new Criteria();

		// Getting the name of the best provider
		String provider = locationManager.getBestProvider(criteria, true);

		// Getting Current Location
		myLocation = locationManager.getLastKnownLocation(provider);

		if (myLocation != null) {
			onLocationChanged(myLocation);
		}
		locationManager.requestLocationUpdates(provider, 20000, 0, this);

		/*****/

		// Location myLocation = map.getMyLocation();
		LatLng myLocationLatlng = new LatLng(myLocation.getLatitude(),
				myLocation.getLongitude());

		markerOptions = new MarkerOptions();
		markerOptions.position(myLocationLatlng);
		BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
		markerOptions.icon(bitmapDescriptor);

		map.addMarker(markerOptions);
		map.animateCamera(CameraUpdateFactory.zoomTo(15));
		map.animateCamera(CameraUpdateFactory.newLatLng(myLocationLatlng));

		circle = new CircleOptions();

		// 55 represents percentage of transparency. For 100% transparency,
		// specify 00.
		// For 0% transparency ( ie, opaque ) , specify ff
		// The remaining 6 characters(00ff00) specify the fill color
		circle.fillColor(0x5500ff00);
		circle.strokeWidth(2);
		circle.center(myLocationLatlng);
		circle.radius(10000);

		map.addCircle(circle);

		btn_next = (Button) findViewById(R.id.btn_next_locate);
		btn_previous = (Button) findViewById(R.id.btn_previous_locate);
		btn_choose = (Button) findViewById(R.id.btn_validate_locate);

		OnClickListener previousClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				cursor = (cursor - 1);
				if (cursor < 0)
					cursor = addresses.size() - 1;
				currentAddress = addresses.get(cursor);
				locationMap = new LatLng(currentAddress.getLatitude(),
						currentAddress.getLongitude());
				map.animateCamera(CameraUpdateFactory.newLatLng(locationMap));
			}
		};

		OnClickListener nextClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				cursor = (cursor + 1) % (addresses.size());
				currentAddress = addresses.get(cursor);
				locationMap = new LatLng(currentAddress.getLatitude(),
						currentAddress.getLongitude());
				map.animateCamera(CameraUpdateFactory.newLatLng(locationMap));
			}
		};

		OnClickListener chooseClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MapLocateActivity.this, EventDisplayActivity.class);
				i.putExtra("KEY_ID_EVENT", listEvents.get(currentAddress).eventId);
				startActivity(i);
			}
		};

		btn_previous.setOnClickListener(previousClickListener);
		btn_next.setOnClickListener(nextClickListener);
		btn_choose.setOnClickListener(chooseClickListener);
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
			intent = new Intent(MapLocateActivity.this, AddEventActivity.class);
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

	@Override
	public void onLocationChanged(Location location) {

		myLocation = location;

		// Getting latitude of the current location
		double latitude = location.getLatitude();

		// Getting longitude of the current location
		double longitude = location.getLongitude();

		// Creating a LatLng object for the current location
		LatLng latLng = new LatLng(latitude, longitude);

		// Showing the current location in Google Map
		// map.moveCamera(CameraUpdateFactory.newLatLng(latLng));

		CircleOptions c = new CircleOptions();
		c.center(latLng);
		// 55 represents percentage of transparency. For 100% transparency,
		// specify 00.
		// For 0% transparency ( ie, opaque ) , specify ff
		// The remaining 6 characters(00ff00) specify the fill color
		c.fillColor(0x5500ff00);
		c.strokeWidth(2);
		c.radius(10000);
		map.animateCamera(CameraUpdateFactory.zoomTo(15));
		map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
		map.addCircle(c);
		
		try {
			getCloseEvents(10000, 5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}

	public ArrayList<EventAdapter.EventData> getCloseEvents(int distanceMax,
			int nbResultMax) throws InterruptedException, ExecutionException,
			JSONException {

		ArrayList<EventAdapter.EventData> result = new ArrayList<EventAdapter.EventData>();

		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("Request", "SelectEvent");

		// Execution de la requête
		RequestTask rt = new RequestTask();
		rt.execute(hm);

		JSONArray resultJson = rt.get();

		if ((resultJson == null))
			return null;

		int length = resultJson.length();

		if (length == 0)
			return result;

		for (int i = 0; i < length; i++) {

			JSONObject row_item = resultJson.getJSONObject(i);
			String title = row_item.getString("title");
			String description = row_item.getString("description");
			String dateStart = row_item.getString("date_debut");
			String dateEnd = row_item.getString("date_fin");
			String hourStart = row_item.getString("heure_debut");
			String hourEnd = row_item.getString("heure_fin");
			String location = row_item.getString("location");
			String emailOwner = row_item.getString("id_createur");
			String urlImage = row_item.getString("urlimage");
			int id = Integer.parseInt(row_item.getString("id_event"));
			float latitude = Float.parseFloat(row_item.getString("latitude"));
			float longitude = Float.parseFloat(row_item.getString("longitude"));
			int state = Integer.parseInt(row_item.getString("state"));
			String dateCreation = row_item.getString("date_creation");
			int participants = Integer.parseInt(row_item
					.getString("participants"));
			
			EventAdapter.EventData newEvent = new EventAdapter.EventData(id,
					title, location, description, dateStart, hourStart,
					hourEnd, participants, emailOwner, state, dateCreation,
					latitude, longitude,urlImage);

			Location locationEvent = new Location("locationEvent");
			locationEvent.setLatitude(latitude);
			locationEvent.setLongitude(longitude);
			
			newEvent.distance = (float) myLocation.distanceTo(locationEvent);

			if (newEvent.distance < distanceMax)
				result.add(newEvent);
				addresses.add(locationEvent);
				listEvents.put(locationEvent, newEvent);
		}

		Collections.sort(result, new Comparator<EventAdapter.EventData>() {
			public int compare(EventAdapter.EventData a,
					EventAdapter.EventData b) {

				int comparison = (int) (a.distance - b.distance);
				if (comparison > 0)
					return 1;
				else if (comparison == 0)
					return 0;
				else
					return -1;
			}
		});

		displayCloseEvents(result, nbResultMax);
		return result;
	}

	public void displayCloseEvents(ArrayList<EventAdapter.EventData> events,
			int nbResultMax) {
		if (events.size() > 0 && events != null) {
			for (int i = 0; i < events.size(); i++) {
				
				if(i > nbResultMax)
					return;
				
				EventAdapter.EventData event = events.get(i);
				int temperatureEvent = (int) TemperatureEvent.getTemperature(event.numberOfParticipants, 0, 0);
				LatLng myLocationLatlng = new LatLng(event.latitude,
						event.longitude);

				markerOptions = new MarkerOptions();
				BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
						.defaultMarker(TemperatureEvent.getColor(temperatureEvent));
				markerOptions.icon(bitmapDescriptor);
				markerOptions.position(myLocationLatlng);
				markerOptions.title(event.eventTitle+" ("+event.numberOfParticipants+" participants)");
				

				map.addMarker(markerOptions);
				map.animateCamera(CameraUpdateFactory.zoomTo(15));
			}
		}
	}
}
