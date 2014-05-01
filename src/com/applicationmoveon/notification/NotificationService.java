package com.applicationmoveon.notification;

import java.util.HashMap;
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
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

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

	@SuppressWarnings("deprecation")
	private void handleIntent(Intent intent) { 
		Log.i("toto", "avant wake");
		//obtain the wake lock
		PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE); 
		mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Notificaion Service"); 
		mWakeLock.acquire(); 
		Log.i("toto", "apres wake");

		//check the global background data setting
		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE); 
		if (!cm.getBackgroundDataSetting()) {
			Log.i("toto", "stop");
			stopSelf(); 
			return; 
		} 
		Log.i("toto", "apres stop");
		session = new SessionManager(NotificationService.this);
		session.checkLogin();
		email = session.getUserDetails().get(SessionManager.KEY_EMAIL);
		Log.i("toto", "avant poll");
		new PollTask().execute();
	} 
	private class PollTask extends AsyncTask<Void, Void, Void> {
		/** 
		 * This is where YOU do YOUR work. There's nothing for me to write here 
		 * you have to fill this in. Make your HTTP request(s) or whatever it is 
		 * you have to do to get your updates in here, because this is run in a 
		 * separate thread
		 **/ 
		protected Database db;

		public PollTask(){
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

		@SuppressWarnings("deprecation")
		public void createNotification() {

			StringBuilder text = new StringBuilder();
			StringBuilder longText = new StringBuilder();
			String title = "titre notif";

			Notification.Builder noti = null;
			Notification notification;
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				noti = new Notification.Builder(context)
				.setContentTitle(title).setContentText(text.toString())
				.setSmallIcon(R.drawable.ic_launcher)
				.setTicker("Des changements dans ton emploi du temps.");

				if (!text.toString().equals(longText.toString())) {
					noti.setStyle(new Notification.BigTextStyle().bigText(longText.toString()));
				}
				notification = noti.build();
			}
			else {//SI le sdk est plus ancien que honeycomb
				NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

				mBuilder.setContentTitle(title)
				.setContentText(longText.toString())
				.setSmallIcon(R.drawable.ic_launcher);

				notification = mBuilder.getNotification();
			}

			notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;

			if (!text.toString().equals("") && !text.toString().equals(null)) {
				notifManager.notify(0, notification);
			}
		}
		public boolean getNotif() throws InterruptedException, ExecutionException, JSONException{

			JSONArray result = 	db.GetNotificationByUser("hugo.83300@gmail.com");
			//TODO remplacer par email
			if(result == null)
				return false;

			int length = result.length();

			if(length == 0)
				return false;

			for (int i = 0; i < length; i++) {
				//TODO recuperer texte notif

			}
			return true;
		}
	}

	/** 
	 * This is deprecated, but you have to implement it if you're planning on
	 * supporting devices with an API level lower than 5 (Android 2.0). 
	 **/ 
	@Override
	public void onStart(Intent intent, int startId) { 
		handleIntent(intent);
	}

	/** 
	 * This is called on 2.0+ (API level 5 or higher). Returning
	 * START_NOT_STICKY tells the system to not restart the service if it is 
	 * killed because of poor resource (memory/cpu) conditions.
	 **/
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) { 
		handleIntent(intent); 
		return START_NOT_STICKY;
	} 
	/** * In onDestroy() we release our wake lock. This ensures that whenever the 
	 * Service stops (killed for resources, stopSelf() called, etc.), the wake 
	 * lock will be released.
	 **/
	public void onDestroy() { 
		Log.i("toto", "destroyyyy");

		super.onDestroy(); 
		mWakeLock.release(); 
	} 
		
}