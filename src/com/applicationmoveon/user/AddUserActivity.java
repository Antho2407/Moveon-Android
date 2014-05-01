package com.applicationmoveon.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import com.applicationmoveon.R;
import com.applicationmoveon.ToolBox;
import com.applicationmoveon.UserSettingActivity;

import com.applicationmoveon.database.ExecTask;
import com.applicationmoveon.event.AddEventActivity;
import com.applicationmoveon.localisation.MapActivity;
import com.applicationmoveon.localisation.MapLocateActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class AddUserActivity extends Activity implements OnClickListener {

	private EditText name;
	private EditText firstname;
	private EditText email;
	private EditText password;
	private EditText password_check;

	private Button subscribeButton;
	private Button pictureButton;

	private ImageView mainPicture;
	private String picturePath;
	
	private ToolBox tools;

	private int RESULT_LOAD_IMAGE = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_user);

		tools = new ToolBox(this);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		name = (EditText) findViewById(R.id.name_user);
		firstname = (EditText) findViewById(R.id.firstname_user);
		email = (EditText) findViewById(R.id.email_user);
		password = (EditText) findViewById(R.id.password_user);
		password_check = (EditText) findViewById(R.id.password_user_check);

		mainPicture = (ImageView) findViewById(R.id.sample_picture_user);

		pictureButton = (Button) findViewById(R.id.button_picture_user);
		subscribeButton = (Button) findViewById(R.id.button_add_user);

		subscribeButton.setOnClickListener(this);
		pictureButton.setOnClickListener(this);
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

			BitmapFactory.Options options=new BitmapFactory.Options();
			options.inSampleSize = 8;
			mainPicture.setImageBitmap(BitmapFactory.decodeFile(picturePath, options));

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onClick(View v) {

		if (v == pictureButton) {
			Intent i = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(i, RESULT_LOAD_IMAGE);
		}
		if (v == subscribeButton) {
			ArrayList<String> fieldsEmpty = new ArrayList<String>();

			if (!checkPassword()) {
				tools.alertUser("Vérification du mot de passe",
						"Les deux champs mot de passe ne correspondent pas");
				return;
			}

			String message = "Attention, les champs suivants doivent être renseignés :\n";
			fieldsEmpty = checkFields();
			if (fieldsEmpty.size() > 0) {
				for (String field : fieldsEmpty)
					message += "-" + field + "\n";
				tools.alertUser("Champs manquants", message);
			} else {
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put("Request", "addUser");
				hm.put("lastname", name.getText().toString());
				hm.put("firstname", firstname.getText().toString());
				hm.put("password", password.getText().toString());
				hm.put("email", email.getText().toString());
				hm.put("urlimage", picturePath);

				// Execution de la requête
				ExecTask rt = new ExecTask();
				rt.execute(hm);

				try {
					if (!rt.get()) {
						tools.alertUser("Ajout impossible",
								"Un compte lié à la même adresse mail existe déjà !");
						return;
					} else {
						tools.alertUser("Ajout effectué",
								"Votre compte a été créé avec succès !");
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
	protected void onStart() {
		super.onStart();

		if(!tools.isOnline()){
			Intent intent = new Intent(AddUserActivity.this,
					com.applicationmoveon.InternetCheckActivity.class);
			intent.putExtra("KEY_PREVIOUS_ACTIVITY", this.getClass().getName());
			startActivity(intent);
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
		case R.id.menu_pref:
			intent = new Intent(this,UserSettingActivity.class);
			startActivity(intent);			return true;
		case android.R.id.home:
			this.finish();
			return true;

		}
		return super.onOptionsItemSelected(item);
	}

	public ArrayList<String> checkFields() {
		ArrayList<String> fieldsEmpty = new ArrayList<String>();
		if (name.getText().toString().equals(""))
			fieldsEmpty.add("nom");
		if (firstname.getText().toString().equals(""))
			fieldsEmpty.add("prénom");
		if (email.getText().toString().equals(""))
			fieldsEmpty.add("prénom");
		if (password.getText().toString().equals(""))
			fieldsEmpty.add("mot de passe");
		return fieldsEmpty;
	}

	public boolean checkPassword() {
		if (!password.getText().toString()
				.equals(password_check.getText().toString()))
			return false;
		return true;
	}
}