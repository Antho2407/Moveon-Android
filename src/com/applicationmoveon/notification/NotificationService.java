package com.applicationmoveon.notification;

import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.applicationmoveon.R;
import com.applicationmoveon.database.Database;
import com.applicationmoveon.session.SessionManager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class NotificationService extends Service { 
	private WakeLock mWakeLock; 
	private Context context = this;
	private NotificationManager notifManager;
	private SessionManager session;
	private String email;


	@Override
	public IBinder onBind(Intent intent) { 
		return null;
	}

	private void handleIntent(Intent intent) { 
		PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE); 
		mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Notificaion Service"); 
		mWakeLock.acquire(); 

		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE); 
		if (cm.getActiveNetworkInfo()==null) {
			stopSelf(); 
			return; 
		} 
		session = new SessionManager(NotificationService.this);
		session.checkLogin();
		email = session.getUserDetails().get(SessionManager.KEY_EMAIL);
		new NotifTask().execute();
	} 
	private class NotifTask extends AsyncTask<Void, Void, Void> {

		protected Database db;

		public NotifTask(){
			db = new Database();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				getNotif();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			return null; 
		} 

		@Override
		protected void onPostExecute(Void result) {  
			stopSelf(); 
		}

		public void createNotification(String texte) {
			notifManager = (NotificationManager) getSystemService(context.NOTIFICATION_SERVICE);
			StringBuilder text = new StringBuilder();
			StringBuilder longText = new StringBuilder();
			String title = "MoveOn";
			text.append("Un utilisateur que tu suis a ajouté un nouvel evenement...");
			longText.append(texte);


			Notification.Builder noti = null;
			Notification notification;
			noti = new Notification.Builder(context)
			.setContentTitle(title).setContentText(text.toString())
			.setSmallIcon(R.drawable.ic_launcher)
			.setTicker("MoveOn un nouvel evenement !");

			if (!text.toString().equals(longText.toString())) {
				noti.setStyle(new Notification.BigTextStyle().bigText(longText.toString()));
			}
			notification = noti.build();


			notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;

			if (!text.toString().equals("") && !text.toString().equals(null)) {
				notifManager.notify(0, notification);
			}
		}
		public boolean getNotif() throws InterruptedException, ExecutionException, JSONException{

			JSONArray result = 	db.GetNotificationByUser(email);
			if(result == null)
				return false;

			int length = result.length();

			if(length == 0)
				return false;

			for (int i = 0; i < length; i++) {
				JSONObject row_item = result.getJSONObject(i);
				String text = row_item.getString("text_notification");
				String mail = row_item.getString("id_suivi");
				createNotification(text);
			}
			return true;
		}
	}

	@Override
	public void onStart(Intent intent, int startId) { 
		handleIntent(intent);
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) { 
		handleIntent(intent); 
		return START_NOT_STICKY;
	} 

	public void onDestroy() { 
		super.onDestroy(); 
		mWakeLock.release(); 
	} 

}