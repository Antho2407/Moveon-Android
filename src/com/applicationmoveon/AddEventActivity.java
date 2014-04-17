package com.applicationmoveon;

import java.util.Calendar;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.SearchView.OnQueryTextListener;

public class AddEventActivity extends Activity implements OnClickListener {

	final String EXTRA_LOGIN = "user_login";
	final String EXTRA_PASSWORD = "user_password";
	final String EXTRA_LONG = "";
	final String EXTRA_LAT = "";
	final String EXTRA_ADDRESS = "";

	private ImageButton pictureCalendar;
	private Calendar calendar;
	private int day;
	private int month;
	private int year;
	private int hourofday;
	private int minute;
	
	private EditText title;
	private EditText description;
	private EditText date_txt;
	
	private TextView hour_txt;
	
	private Button pictureButton;
	private Button locationButton;
	
	private ImageButton calendarButton;
	private ImageButton hourButton;
	
	private int RESULT_LOAD_IMAGE = 0;
	private int RESULT_MAP = 0;
	
	private ImageView pictureCheckLocation;
	private ImageView mainPicture;
    
	private String longitude = "";
    private String latitude = "";
    private String address = "";
    private String picturePath = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_event);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		pictureCheckLocation = (ImageView)findViewById(R.id.picture_check_location);

		Intent intent = getIntent();
		
		if (intent != null) {
			longitude = intent.getStringExtra(EXTRA_LONG);
			latitude = intent.getStringExtra(EXTRA_LAT);
			address = intent.getStringExtra(EXTRA_ADDRESS);
			
			if((longitude != null)&&(latitude != null)){
				pictureCheckLocation.setImageResource(R.drawable.check);
			}
		}

		title = (EditText) findViewById(R.id.title_event);
		description = (EditText) findViewById(R.id.description_event);
		date_txt = (EditText) findViewById(R.id.txt_date);
		date_txt.setKeyListener(null);
		
		mainPicture = (ImageView)findViewById(R.id.sample_picture_event);
		
		hour_txt = (TextView)findViewById(R.id.txt_hour);
		
		calendar = Calendar.getInstance();
		day = calendar.get(Calendar.DAY_OF_MONTH);
		month = calendar.get(Calendar.MONTH) + 1;
		year = calendar.get(Calendar.YEAR);

		date_txt.setText(day + "/" + month + "/" + year);

		pictureButton = (Button) findViewById(R.id.button_picture);
		locationButton = (Button) findViewById(R.id.button_location);
		calendarButton = (ImageButton) findViewById(R.id.calendarButton);
		hourButton = (ImageButton) findViewById(R.id.hourButton);

		pictureButton.setOnClickListener(this);
		calendarButton.setOnClickListener(this);
		hourButton.setOnClickListener(this);
		locationButton.setOnClickListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePath = cursor.getString(columnIndex);
			cursor.close();

			mainPicture.setImageBitmap(BitmapFactory.decodeFile(picturePath));

		}
//		if (requestCode == RESULT_MAP && resultCode == RESULT_OK
//				&& null != data) {
//			Log.i("ANTHO", data.getStringExtra(EXTRA_LAT));
//			Log.i("ANTHO", data.getStringExtra(EXTRA_LONG));
//		}

	}
	
	@Override
	protected void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);
	    state.putString("title", title.getText().toString());
	    state.putString("description", description.getText().toString());
	    state.putString("date", date_txt.getText().toString());
	    state.putString("hour", hour_txt.getText().toString());
	    state.putString("picture", picturePath);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	    if (savedInstanceState != null){
			title.setText(savedInstanceState.getString("title"));
			description.setText(savedInstanceState.getString("description"));
			hour_txt.setText(savedInstanceState.getString("hour"));
			date_txt.setText(savedInstanceState.getString("date"));
			picturePath = savedInstanceState.getString("picture");
			if(picturePath!="")
				mainPicture.setImageBitmap(BitmapFactory.decodeFile(picturePath));
		}
	}

	@Override
	public void onClick(View v) {

		if (v == calendarButton) {
			DatePickerDialog dpd = new DatePickerDialog(this,AlertDialog.THEME_HOLO_LIGHT,
					new DatePickerDialog.OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int y, int m,
						int d) {
					date_txt.setText(d + "/" + (m + 1) + "/" + y);

				}
			}, year, month, day);
			dpd.show();
		}
		if (v == hourButton) {
			TimePickerDialog dpd = new TimePickerDialog(this,AlertDialog.THEME_HOLO_LIGHT,
					new TimePickerDialog.OnTimeSetListener() {
						@Override
						public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
							String hourDisplay = "";
							hourDisplay+=(hourOfDay<10)?"0"+hourOfDay:hourOfDay;
							hourDisplay+=":";
							hourDisplay+=(minute<10)?"0"+minute:minute;
							hour_txt.setText(hourDisplay);
						}
					}
			, hourofday, minute, true);
			dpd.show();
		}
		if (v == pictureButton) {
			// final String loginTxt = login.getText().toString();
			// final String passTxt = pass.getText().toString();

			// Vérifier si l'un des deux champs est vide
			// if (loginTxt.equals("") || passTxt.equals("")) {
			// Toast.makeText(AddEventActivity.this,
			// R.string.email_or_password_empty,
			// Toast.LENGTH_SHORT).show();
			// return;
			// }

			Intent i = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

			startActivityForResult(i, RESULT_LOAD_IMAGE);
		}
		if (v == locationButton) {
			Intent i = new Intent(AddEventActivity.this, MapActivity.class);
			startActivityForResult(i, RESULT_MAP);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_add, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
		case R.id.menu_locate:
			//TODO lancer localisation
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