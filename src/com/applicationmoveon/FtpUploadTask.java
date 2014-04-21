package com.applicationmoveon;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import android.os.AsyncTask;
import android.util.Log;

public class FtpUploadTask extends AsyncTask<String, Void, FTPClient> {

	String path = "";
	boolean status = false;
	String email = "";
	String name = "";

	public FtpUploadTask(String path, String email, String name) {
		this.path = path;
		this.email = email;
		this.name = name;
	}

	protected FTPClient doInBackground(String... args) {

		FTPClient mFTPClient = new FTPClient();

		// connecting to the host
		try {
			mFTPClient.connect("ftp.martinezhugo.com", 21);

			status = mFTPClient.login("martinezhugo", "dj$bG0u8v[");
		
		} catch (SocketException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {  
            FileInputStream srcFileStream = new FileInputStream(path);  
            mFTPClient.changeWorkingDirectory("www/moveon/images");
            boolean status = mFTPClient.makeDirectory(email+"/");  
            status = mFTPClient.storeFile(email+"/"+name,  
                      srcFileStream);  
            Log.e("Status", String.valueOf(status));  
            srcFileStream.close();  
       } catch (Exception e) {  
            e.printStackTrace();  
       }  
		return mFTPClient;
	}

	protected void onPostExecute(FTPClient result) {
	}

}