package com.applicationmoveon;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

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
}
