package com.applicationmoveon.event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import com.applicationmoveon.R;
import com.applicationmoveon.ToolBox;
import com.applicationmoveon.database.ExecTask;
import com.applicationmoveon.ftp.FtpUploadTask;
import com.applicationmoveon.localisation.MapActivity;
import com.applicationmoveon.localisation.MapLocateActivity;
import com.applicationmoveon.session.SessionManager;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

public class AddEventActivity extends Activity implements OnClickListener {

	//public static final String EXTRA_LOGIN = "user_login";
	//public static final String EXTRA_PASSWORD = "user_password";
	
	private ImageButton pictureCalendar;
	private Calendar calendar;
	private int day;
	private int month;
	private int year;
	private int hourofday;
	private int minute;
	
	private EditText title;
	private EditText description;
	private EditText date_txt_beginning;
	private EditText date_txt_end;
	
	private TextView hour_txt_beginning;
	private TextView hour_txt_end;
	
	private Button pictureButton;
	private Button locationButton;
	private Button createButton;
	
	private ImageButton calendarButtonBeginning;
	private ImageButton hourButtonBeginning;
	private ImageButton calendarButtonEnd;
	private ImageButton hourButtonEnd;
	
	private int RESULT_LOAD_IMAGE = 0;
	private int RESULT_MAP = 1;
	
	private ImageView pictureCheckLocation;
	private ImageView mainPicture;
    
	private String longitude = "";
    private String latitude = "";
    private String address = "";
    private String picturePath = "";
    private String namePicture = "";
    
    private ToolBox tools;
    
    private SessionManager session;
    private String userMail = "";
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Session Manager
        session = new SessionManager(AddEventActivity.this);
        session.checkLogin();
        userMail = session.getUserDetails().get(SessionManager.KEY_EMAIL);
		
		setContentView(R.layout.activity_add_event);
		
		tools = new ToolBox(this);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		pictureCheckLocation = (ImageView)findViewById(R.id.picture_check_location);

		Intent intent = getIntent();
		
		title = (EditText) findViewById(R.id.title_event);
		description = (EditText) findViewById(R.id.description_event);
		date_txt_beginning = (EditText) findViewById(R.id.txt_date_beginning);
		date_txt_beginning.setKeyListener(null);
		date_txt_end = (EditText) findViewById(R.id.txt_date_end);
		date_txt_end.setKeyListener(null);
		
		mainPicture = (ImageView)findViewById(R.id.sample_picture_event);
		
		hour_txt_beginning = (TextView)findViewById(R.id.txt_hour_beginning);
		hour_txt_end = (TextView)findViewById(R.id.txt_hour_end);
		
		calendar = Calendar.getInstance();
		day = calendar.get(Calendar.DAY_OF_MONTH);
		month = calendar.get(Calendar.MONTH) + 1;
		year = calendar.get(Calendar.YEAR);

		date_txt_beginning.setText(year + "-" + month + "-" + day);
		date_txt_end.setText(year + "-" + month + "-" + day);

		pictureButton = (Button) findViewById(R.id.button_picture);
		locationButton = (Button) findViewById(R.id.button_location);
		calendarButtonBeginning = (ImageButton) findViewById(R.id.calendarButtonBeginning);
		hourButtonBeginning = (ImageButton) findViewById(R.id.hourBeginButton);
		calendarButtonEnd= (ImageButton) findViewById(R.id.calendarButtonEnd);
		hourButtonEnd = (ImageButton) findViewById(R.id.hourEndButton);
		createButton = (Button) findViewById(R.id.button_add_event);

		pictureButton.setOnClickListener(this);
		calendarButtonBeginning.setOnClickListener(this);
		calendarButtonEnd.setOnClickListener(this);
		hourButtonBeginning.setOnClickListener(this);
		hourButtonEnd.setOnClickListener(this);
		locationButton.setOnClickListener(this);
		createButton.setOnClickListener(this);
		
		restoreActivity(savedInstanceState);
	}
	
	@Override
	protected void onStart() {
		super.onStart();

		if(!tools.isOnline()){
			Intent intent = new Intent(AddEventActivity.this,
					com.applicationmoveon.InternetCheckActivity.class);
			intent.putExtra("KEY_PREVIOUS_ACTIVITY", this.getClass().getName());
			startActivity(intent);
		}
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
			String extension = picturePath.substring(picturePath.lastIndexOf("."));
			namePicture = tools.getFileName(selectedImage)+extension;
			cursor.close();

			BitmapFactory.Options options=new BitmapFactory.Options();
			options.outHeight = 8;
			//mainPicture.setImageBitmap(BitmapFactory.decodeFile(picturePath, options));
			mainPicture.setImageBitmap(tools.decodeSampledBitmapFromResource(picturePath, 200, 200));
			new FtpUploadTask(picturePath, userMail, namePicture).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

		}
		
		if (requestCode == RESULT_MAP
				&& null != data) {
			latitude = data.getStringExtra("EXTRA_LAT");
			longitude = data.getStringExtra("EXTRA_LONG");
			address = data.getStringExtra("EXTRA_ADDRESS");
			pictureCheckLocation.setImageResource(R.drawable.check);
		}

	}
	
	@Override
	protected void onSaveInstanceState(Bundle state) {
	    state.putString("title", title.getText().toString());
	    state.putString("description", description.getText().toString());
	    state.putString("date_beginning", date_txt_beginning.getText().toString());
	    state.putString("date_end", date_txt_end.getText().toString());
	    state.putString("hour_beginning", hour_txt_beginning.getText().toString());
	    state.putString("hour_end", hour_txt_end.getText().toString());
	    state.putString("picture", picturePath);
	    super.onSaveInstanceState(state);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		restoreActivity(savedInstanceState);
	}
	
	private void restoreActivity(Bundle savedInstanceState) {
	    if (savedInstanceState != null)
	    {
	    	title.setText(savedInstanceState.getString("title"));
			description.setText(savedInstanceState.getString("description"));
			hour_txt_beginning.setText(savedInstanceState.getString("hour_beginning"));
			hour_txt_end.setText(savedInstanceState.getString("hour_end"));
			date_txt_beginning.setText(savedInstanceState.getString("date_beginning"));
			date_txt_end.setText(savedInstanceState.getString("date_end"));
			picturePath = savedInstanceState.getString("picture");
			if(picturePath!="")
				mainPicture.setImageBitmap(BitmapFactory.decodeFile(picturePath));
	    }
	}

	@Override
	public void onClick(View v) {

		if (v == calendarButtonBeginning) {
			DatePickerDialog dpd = new DatePickerDialog(this,AlertDialog.THEME_HOLO_LIGHT,
					new DatePickerDialog.OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int y, int m,
						int d) {
					date_txt_beginning.setText(y + "-" + (m + 1) + "-" + d);

				}
			}, year, month, day);
			dpd.show();
		}
		if (v == calendarButtonEnd) {
			DatePickerDialog dpd = new DatePickerDialog(this,AlertDialog.THEME_HOLO_LIGHT,
					new DatePickerDialog.OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int y, int m,
						int d) {
					date_txt_end.setText(y + "-" + (m + 1) + "-" + d);

				}
			}, year, month, day);
			dpd.show();
		}
		if (v == hourButtonBeginning) {
			TimePickerDialog dpd = new TimePickerDialog(this,AlertDialog.THEME_HOLO_LIGHT,
					new TimePickerDialog.OnTimeSetListener() {
						@Override
						public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
							String hourDisplay = "";
							hourDisplay+=(hourOfDay<10)?"0"+hourOfDay:hourOfDay;
							hourDisplay+=":";
							hourDisplay+=(minute<10)?"0"+minute:minute;
							hour_txt_beginning.setText(hourDisplay);
						}
					}
			, hourofday, minute, true);
			dpd.show();
		}
		if (v == hourButtonEnd) {
			TimePickerDialog dpd = new TimePickerDialog(this,AlertDialog.THEME_HOLO_LIGHT,
					new TimePickerDialog.OnTimeSetListener() {
						@Override
						public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
							String hourDisplay = "";
							hourDisplay+=(hourOfDay<10)?"0"+hourOfDay:hourOfDay;
							hourDisplay+=":";
							hourDisplay+=(minute<10)?"0"+minute:minute;
							hour_txt_end.setText(hourDisplay);
						}
					}
			, hourofday, minute, true);
			dpd.show();
		}
		if (v == pictureButton) {

			Intent i = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

			startActivityForResult(i, RESULT_LOAD_IMAGE);
		}
		if (v == locationButton) {
			Intent i = new Intent(AddEventActivity.this, MapActivity.class);
			startActivityForResult(i, RESULT_MAP);
		}
		if (v == createButton) {
			ArrayList<String> fieldsEmpty = new ArrayList<String>();
			String message = "Attention, les champs suivants doivent être renseignés :\n";
			fieldsEmpty = checkFields();
			if(fieldsEmpty.size()>0){
				for(String field : fieldsEmpty)
					message+="-"+field+"\n";
				alertUser("Champs manquants", message);
			}else{
				HashMap<String, String> hm = new HashMap<String,String>();
				hm.put("Request","addEvent");
				hm.put("title", title.getText().toString());
				hm.put("description", description.getText().toString());
				hm.put("date_debut", date_txt_beginning.getText().toString());
				hm.put("date_fin", date_txt_end.getText().toString());
				hm.put("heure_debut", hour_txt_beginning.getText().toString()+":00");
				hm.put("heure_fin", hour_txt_end.getText().toString()+":00");
				hm.put("participants", Integer.toString(0));
				hm.put("location", address);
				hm.put("latitude", latitude);
				hm.put("longitude", longitude);
				hm.put("id_createur", userMail);
				hm.put("date_creation", year + "-" + month + "-" + day);
				hm.put("state", Integer.toString(0));
				hm.put("urlimage", namePicture);
				hm.put("temperature", Integer.toString(0));
				hm.put("likes", Integer.toString(0));
				hm.put("dislikes", Integer.toString(0));
				
				//Execution de la requête
				ExecTask rt = new ExecTask();
				rt.execute(hm);
				
				
				try {
					if(!rt.get()){
						alertUser("Ajout impossible","L'évènement existe déjà !");
						return;
					}else{
						alertUser("Ajout effectué","L'évènement a été ajouté avec succès !");
						return;
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
 			}
			return;
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
			intent = new Intent(AddEventActivity.this, MapLocateActivity.class);
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
	
	public ArrayList<String> checkFields(){
		ArrayList<String> fieldsEmpty = new ArrayList<String>();
		if(title.getText().toString().equals(""))
			fieldsEmpty.add("titre");
		if(description.getText().toString().equals(""))
			fieldsEmpty.add("description");
		if(date_txt_beginning.getText().toString().equals("Date de début :"))
			fieldsEmpty.add("date de début");
		if(date_txt_end.getText().toString().equals("Date de fin :"))
			fieldsEmpty.add("date de fin");
		if(hour_txt_beginning.getText().toString().equals("Heure de début :"))
			fieldsEmpty.add("heure de début");
		if(hour_txt_end.getText().toString().equals("Heure de fin :"))
			fieldsEmpty.add("heure de fin");
		if((longitude=="")||(latitude=="")||(address=="")){
			fieldsEmpty.add("lieu");
		
		}
		return fieldsEmpty;
	}
	
	public void alertUser(String title, String message){
		new AlertDialog.Builder(this)
	    .setTitle(title)
	    .setMessage(message)
	    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int id) {
           }
	    })
	    .setIcon(android.R.drawable.ic_dialog_alert)
	    .show();
	}
}