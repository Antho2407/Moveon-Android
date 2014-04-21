package com.applicationmoveon;
import java.io.File;
import java.io.IOException;

import org.jibble.simpleftp.SimpleFTP;

import android.os.AsyncTask;
import android.util.Log;

public class FtpUploadTask extends AsyncTask<String, Void, SimpleFTP> {
	
	String path = "";
	
	public FtpUploadTask(String path){
		this.path = path;
	}
	
	protected SimpleFTP doInBackground(String... args) {
		
		SimpleFTP ftp = new SimpleFTP();

	    // Connect to an FTP server on port 21.
	    try {
			ftp.connect("ftp.martinezhugo.com", 21, "martinezhugo", "dj$bG0u8v[");

			ftp.bin();
			ftp.cwd("www/moveon/images");
			ftp.stor(new File(path));
	    
		    // Quit from the FTP server.
		    ftp.disconnect();
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return ftp;
     }

     protected void onPostExecute(SimpleFTP result) {
     }

}