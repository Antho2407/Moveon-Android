package com.applicationmoveon;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;



public class AccueilActivity extends Activity{
	GridView gridView;
	ArrayList<Item> gridArray = new ArrayList<Item>();
	CustomGridViewAdapter customGridAdapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accueil);

		//set grid view item
		Bitmap homeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_action_collections_go_to_today);
		Bitmap userIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_action_content_event);

		gridArray.add(new Item(homeIcon,"Profil"));
		gridArray.add(new Item(userIcon,"My Events"));
		gridArray.add(new Item(homeIcon,"Settings"));
		gridArray.add(new Item(userIcon,"User?"));
		gridArray.add(new Item(homeIcon,"menu 5"));
		gridArray.add(new Item(userIcon,"menu 6"));

		gridView = (GridView) findViewById(R.id.gridView1);
		customGridAdapter = new CustomGridViewAdapter(this, R.layout.row_grid, gridArray);
		gridView.setAdapter(customGridAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_accueil, menu);
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
		case R.id.menu_add:
			intent = new Intent(AccueilActivity.this,AddEventActivity.class);
			startActivity(intent);
			return true;
		case R.id.menu_pref:
			//TODO ALLER A PREFERENCES
			return true;
		case android.R.id.home:
			this.finish();
			return true;
		

		}
		return super.onOptionsItemSelected(item);
	}
}