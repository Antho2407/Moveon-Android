package com.applicationmoveon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

public class ToolBox {

	private Activity activity;

	public ToolBox(Activity activity) {
		this.activity = activity;
	}

	public Bitmap readBitmap(Uri selectedImage) {
		Bitmap bm = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 5;
		AssetFileDescriptor fileDescriptor = null;
		try {
			fileDescriptor = activity.getContentResolver()
					.openAssetFileDescriptor(selectedImage, "r");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				bm = BitmapFactory.decodeFileDescriptor(
						fileDescriptor.getFileDescriptor(), null, options);
				fileDescriptor.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bm;
	}
	
	public void alertUser(String title, String message) {
		new AlertDialog.Builder(activity).setTitle(title).setMessage(message)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					}
				}).setIcon(android.R.drawable.ic_dialog_alert).show();
	}
	
	public File createCacheFolder(){
		File mydir = new File(activity.getCacheDir(), "moveon");
		if(!mydir.exists())
		{
		     mydir.mkdirs();
		}  
		return mydir;
	}
	
	public String getFileName(Uri uri){
		String fileName = "";
		String scheme = uri.getScheme();
		if (scheme.equals("file")) {
		    fileName = uri.getLastPathSegment();
		}
		else if (scheme.equals("content")) {
		    String[] proj = { MediaStore.Images.Media.TITLE };
		    Cursor cursor = activity.getContentResolver().query(uri, proj, null, null, null);
		    if (cursor != null && cursor.getCount() != 0) {
		        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
		        cursor.moveToFirst();
		        fileName = cursor.getString(columnIndex);
		    }
		}
		return fileName;
	}
	
	public boolean isOnline() {
		ConnectivityManager cm =
				(ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}
}
