package com.applicationmoveon;

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

public class NotificationService extends Service { 
	private WakeLock mWakeLock; 
	private Context context = this;
	private NotificationManager notifManager;
	/**
	 * Simply return null, since our Service will not be communicating with 
	 * any other components. It just does its work silently.
	 **/
	@Override
	public IBinder onBind(Intent intent) { 
		return null;
	}
	/** 
	 * This is where we initialize. We call this when onStart/onStartCommand is 
	 * called by the system. We won't do anything with the intent here, and you 
	 * probably won't, either. 
	 **/ 
	private void handleIntent(Intent intent) {  
		//obtain the wake lock
		PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE); 
		mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Notificaion Service"); 
		mWakeLock.acquire(); 
		//check the global background data setting
		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE); 
		if (!cm.getBackgroundDataSetting()) {
			stopSelf(); 
			return; 
		} 
		/*do the actual work, in a separate thread*/ 
		new PollTask().execute();
	} 
	private class PollTask extends AsyncTask<Void, Void, Void> {
		/** 
		 * This is where YOU do YOUR work. There's nothing for me to write here 
		 * you have to fill this in. Make your HTTP request(s) or whatever it is 
		 * you have to do to get your updates in here, because this is run in a 
		 * separate thread
		 **/ 
		@Override
		protected Void doInBackground(Void... params) {
			/*do stuff!*/ 
			return null; 
		} 
		/** 
		 * In here you should interpret whatever you fetched in doInBackground 
		 * and push any notifications you need to the status bar, using the 
		 * NotificationManager. I will not cover this here, go check the docs on
		 * NotificationManager. 
		 * 
		 * What you HAVE to do is call stopSelf() after you've pushed your 
		 * notification(s). This will: 
		 * 1) Kill the service so it doesn't waste precious resources 
		 * 2) Call onDestroy() which will release the wake lock, so the device
		 * can go to sleep again and save precious battery. 
		 **/ 
		@Override
		protected void onPostExecute(Void result) {  
			/*handle your data*/
			//createNotification();
			stopSelf(); 
		}

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
		super.onDestroy(); 
		mWakeLock.release(); 
	} 
}