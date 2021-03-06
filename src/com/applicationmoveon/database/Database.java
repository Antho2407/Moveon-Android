package com.applicationmoveon.database;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Database {
	
	public void addUser(String prenom, String nom, String email, String password, String urlimage){
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("firstname",prenom));
		nameValuePairs.add(new BasicNameValuePair("lastname",nom));
		nameValuePairs.add(new BasicNameValuePair("email",email));
		nameValuePairs.add(new BasicNameValuePair("password",password));
		nameValuePairs.add(new BasicNameValuePair("imageprofile",urlimage));
		 
		//http post
		try{
		        HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost("http://martinezhugo.com/moveon/add_user.php");
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        HttpResponse response = httpclient.execute(httppost);
		}catch(Exception e){
		        Log.e("log_tag", "Error in http connection "+e.toString());
		}
	}
	
	public void addEvent(String title, String location,String latitude, String longitude, String description, String date_debut,String heure_debut, String date_fin,String heure_fin, String participants, String id_createur, String date_creation, String state, String urlimage, String temperature, String likes, String dislikes){
		//the year data to send
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("title",title));
		nameValuePairs.add(new BasicNameValuePair("location",location));
		nameValuePairs.add(new BasicNameValuePair("latitude",latitude));
		nameValuePairs.add(new BasicNameValuePair("longitude",longitude));
		nameValuePairs.add(new BasicNameValuePair("description",description));
		nameValuePairs.add(new BasicNameValuePair("date_debut",date_debut));
		nameValuePairs.add(new BasicNameValuePair("heure_debut",heure_debut));
		nameValuePairs.add(new BasicNameValuePair("date_fin",date_fin));
		nameValuePairs.add(new BasicNameValuePair("heure_fin",heure_fin));
		nameValuePairs.add(new BasicNameValuePair("participants",participants));
		nameValuePairs.add(new BasicNameValuePair("id_createur",id_createur));
		nameValuePairs.add(new BasicNameValuePair("date_creation",date_creation));
		nameValuePairs.add(new BasicNameValuePair("state",state));
		nameValuePairs.add(new BasicNameValuePair("urlimage",urlimage));
		nameValuePairs.add(new BasicNameValuePair("temperature",temperature));
		nameValuePairs.add(new BasicNameValuePair("likes",likes));
		nameValuePairs.add(new BasicNameValuePair("dislikes",dislikes));
		//http post
		try{
		        HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost("http://martinezhugo.com/moveon/add_event.php");
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        HttpResponse response = httpclient.execute(httppost);
		}catch(Exception e){
		        Log.e("log_tag", "Error in http connection "+e.toString());
		}
	}
	
	public void addParticipants(String id_event, String email){
		//the year data to send
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("id_event",id_event));
		nameValuePairs.add(new BasicNameValuePair("email",email));
		 
		//http post
		try{
		        HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost("http://martinezhugo.com/moveon/add_participants.php");
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        HttpResponse response = httpclient.execute(httppost);
		}catch(Exception e){
		        Log.e("log_tag", "Error in http connection "+e.toString());
		}
	}
	
	public void deleteParticipants(String id_event, String email){
		//the year data to send
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("id_event",id_event));
		nameValuePairs.add(new BasicNameValuePair("email",email));
		 
		//http post
		try{
		        HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost("http://martinezhugo.com/moveon/delete_participants.php");
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        HttpResponse response = httpclient.execute(httppost);
		}catch(Exception e){
		        Log.e("log_tag", "Error in http connection "+e.toString());
		}
	}
	
	public void addSuivi(String email_user, String user_suivi){
		//the year data to send
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("email_user",email_user));
		nameValuePairs.add(new BasicNameValuePair("user_suivi",user_suivi));

		//http post
		try{
		        HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost("http://martinezhugo.com/moveon/add_user_suivi.php");
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        HttpResponse response = httpclient.execute(httppost);
		}catch(Exception e){
		        Log.e("log_tag", "Error in http connection "+e.toString());
		}
	}
	
	public void deleteSuivi(String email_user, String user_suivi){
		//the year data to send
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("email_user",email_user));
		nameValuePairs.add(new BasicNameValuePair("user_suivi",user_suivi));
		 
		//http post
		try{
		        HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost("http://martinezhugo.com/moveon/delete_user_suivi.php");
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        HttpResponse response = httpclient.execute(httppost);
		}catch(Exception e){
		        Log.e("log_tag", "Error in http connection "+e.toString());
		}
	}
	
	public void addVote(String id_event, String vote, String email){
		//the year data to send
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("id_event",id_event));
		nameValuePairs.add(new BasicNameValuePair("vote",vote));
		nameValuePairs.add(new BasicNameValuePair("email",email));
		
		//http post
		try{
		        HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost("http://martinezhugo.com/moveon/add_vote.php");
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        HttpResponse response = httpclient.execute(httppost);
		}catch(Exception e){
		        Log.e("log_tag", "Error in http connection "+e.toString());
		}
	}
	
	public void updateTemperature(String id_event, String temperature){
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("id_event",id_event));
		nameValuePairs.add(new BasicNameValuePair("temperature",temperature));
		 
		//http post
		try{
		        HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost("http://martinezhugo.com/moveon/update_temperature.php");
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        HttpResponse response = httpclient.execute(httppost);
		}catch(Exception e){
		        Log.e("log_tag", "Error in http connection "+e.toString());
		}
	}
	
	
	public JSONArray SelectEvent(){
		JSONArray jArray = null;
		String result = "";
		InputStream is = null;
		//the year data to send
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("year","1980"));
		 
		//http post
		try{
		        HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost("http://martinezhugo.com/moveon/select_event.php");
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        HttpResponse response = httpclient.execute(httppost);
		        HttpEntity entity = response.getEntity();
		        is = entity.getContent();
		}catch(Exception e){
		        Log.e("log_tag", "Error in http connection "+e.toString());
		}
		//convert response to string
		try{
		        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
		        StringBuilder sb = new StringBuilder();
		        String line = null;
		        while ((line = reader.readLine()) != null) {
		                sb.append(line + "\n");
		        }
		        is.close();
		 
		        result=sb.toString();
		}catch(Exception e){
		        Log.e("log_tag", "Error converting result "+e.toString());
		}
		 
		//parse json data
		try{
		        jArray = new JSONArray(result);
		}catch(JSONException e){
		        Log.e("log_tag", "Error parsing data "+e.toString());
		}
		return jArray;
		
	}
	
	public JSONArray SelectEventById(String id){
		JSONArray jArray = null;
		String result = "";
		InputStream is = null;
		//the year data to send
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("id_event",id));
		 
		//http post
		try{
		        HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost("http://martinezhugo.com/moveon/select_event_by_id.php");
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        HttpResponse response = httpclient.execute(httppost);
		        HttpEntity entity = response.getEntity();
		        is = entity.getContent();
		}catch(Exception e){
		        Log.e("log_tag", "Error in http connection "+e.toString());
		}
		//convert response to string
		try{
		        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
		        StringBuilder sb = new StringBuilder();
		        String line = null;
		        while ((line = reader.readLine()) != null) {
		                sb.append(line + "\n");
		        }
		        is.close();
		 
		        result=sb.toString();
		}catch(Exception e){
		        Log.e("log_tag", "Error converting result "+e.toString());
		}
		 
		//parse json data
		try{
		        jArray = new JSONArray(result);
		}catch(JSONException e){
		        Log.e("log_tag", "Error parsing data "+e.toString());
		}
		return jArray;
		
	}
	
	public JSONArray SelectVoteByUser(String id_user, String id_event){
		JSONArray jArray = null;
		String result = "";
		InputStream is = null;
		//the year data to send
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("id_user",id_user));
		nameValuePairs.add(new BasicNameValuePair("id_event",id_event));
		 
		//http post
		try{
		        HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost("http://martinezhugo.com/moveon/select_vote_by_user.php");
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        HttpResponse response = httpclient.execute(httppost);
		        HttpEntity entity = response.getEntity();
		        is = entity.getContent();
		}catch(Exception e){
		        Log.e("log_tag", "Error in http connection "+e.toString());
		}
		//convert response to string
		try{
		        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
		        StringBuilder sb = new StringBuilder();
		        String line = null;
		        while ((line = reader.readLine()) != null) {
		                sb.append(line + "\n");
		        }
		        is.close();
		 
		        result=sb.toString();
		}catch(Exception e){
		        Log.e("log_tag", "Error converting result "+e.toString());
		}
		 
		//parse json data
		try{
		        jArray = new JSONArray(result);
		}catch(JSONException e){
		        Log.e("log_tag", "Error parsing data "+e.toString());
		}
		return jArray;
	}
	
	public JSONArray SelectUserByMail(String email){
		JSONArray jArray = null;
		String result = "";
		InputStream is = null;
		//the year data to send
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("email",email));
		 
		//http post
		try{
		        HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost("http://martinezhugo.com/moveon/select_user_by_email.php");
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        HttpResponse response = httpclient.execute(httppost);
		        HttpEntity entity = response.getEntity();
		        is = entity.getContent();
		}catch(Exception e){
		        Log.e("log_tag", "Error in http connection "+e.toString());
		}
		//convert response to string
		try{
		        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
		        StringBuilder sb = new StringBuilder();
		        String line = null;
		        while ((line = reader.readLine()) != null) {
		                sb.append(line + "\n");
		        }
		        is.close();
		 
		        result=sb.toString();
		}catch(Exception e){
		        Log.e("log_tag", "Error converting result "+e.toString());
		}
		
		//parse json data
		try{
		        jArray = new JSONArray(result);
		
		
		}catch(JSONException e){
		        Log.e("log_tag", "Error parsing data "+e.toString());
		}
		return jArray;
		
	}
	
	public JSONArray GetNotificationByUser(String email){
		JSONArray jArray = null;
		String result = "";
		InputStream is = null;
		//the year data to send
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("email",email));
		 
		//http post
		try{
		        HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost("http://martinezhugo.com/moveon/get_notification.php");
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        HttpResponse response = httpclient.execute(httppost);
		        HttpEntity entity = response.getEntity();
		        is = entity.getContent();
		}catch(Exception e){
		        Log.e("log_tag", "Error in http connection "+e.toString());
		}
		//convert response to string
		try{
		        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
		        StringBuilder sb = new StringBuilder();
		        String line = null;
		        while ((line = reader.readLine()) != null) {
		                sb.append(line + "\n");
		        }
		        is.close();
		 
		        result=sb.toString();
		}catch(Exception e){
		        Log.e("log_tag", "Error converting result "+e.toString());
		}

		//parse json data
		try{
		        jArray = new JSONArray(result);
		
		
		}catch(JSONException e){
		        Log.e("log_tag", "Error parsing data "+e.toString());
		}
		return jArray;
		
	}
	
	public JSONArray SelectUsersFollowed(String email_user){
		JSONArray jArray = null;
		String result = "";
		InputStream is = null;
		//the year data to send
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("email",email_user));
		 
		//http post
		try{
		        HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost("http://martinezhugo.com/moveon/select_users_followed.php");
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        HttpResponse response = httpclient.execute(httppost);
		        HttpEntity entity = response.getEntity();
		        is = entity.getContent();
		}catch(Exception e){
		        Log.e("log_tag", "Error in http connection "+e.toString());
		}
		//convert response to string
		try{
		        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
		        StringBuilder sb = new StringBuilder();
		        String line = null;
		        while ((line = reader.readLine()) != null) {
		                sb.append(line + "\n");
		        }
		        is.close();
		 
		        result=sb.toString();
		}catch(Exception e){
		        Log.e("log_tag", "Error converting result "+e.toString());
		}
		
		//parse json data
		try{
		        jArray = new JSONArray(result);
		
		
		}catch(JSONException e){
		        Log.e("log_tag", "Error parsing data "+e.toString());
		}
		return jArray;
		
	}
	
	public JSONArray SelectEventByUserMail(String email){
		JSONArray jArray = null;
		String result = "";
		InputStream is = null;
		//the year data to send
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("email",email));
		 
		//http post
		try{
		        HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost("http://martinezhugo.com/moveon/select_event_by_email.php");
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        HttpResponse response = httpclient.execute(httppost);
		        HttpEntity entity = response.getEntity();
		        is = entity.getContent();
		}catch(Exception e){
		        Log.e("log_tag", "Error in http connection "+e.toString());
		}
		//convert response to string
		try{
		        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
		        StringBuilder sb = new StringBuilder();
		        String line = null;
		        while ((line = reader.readLine()) != null) {
		                sb.append(line + "\n");
		        }
		        is.close();
		 
		        result=sb.toString();
		}catch(Exception e){
		        Log.e("log_tag", "Error converting result "+e.toString());
		}
		
		//parse json data
		try{
		        jArray = new JSONArray(result);
		
		
		}catch(JSONException e){
		        Log.e("log_tag", "Error parsing data "+e.toString());
		}
		return jArray;
		
	}
	
	public JSONArray SelectEventBySearch(String search){
		JSONArray jArray = null;
		String result = "";
		InputStream is = null;
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("search",search));
		 
		try{
		        HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost("http://martinezhugo.com/moveon/select_event_by_search.php");
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        HttpResponse response = httpclient.execute(httppost);
		        HttpEntity entity = response.getEntity();
		        is = entity.getContent();
		}catch(Exception e){
		        Log.e("log_tag", "Error in http connection "+e.toString());
		}
		//convert response to string
		try{
		        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
		        StringBuilder sb = new StringBuilder();
		        String line = null;
		        while ((line = reader.readLine()) != null) {
		                sb.append(line + "\n");
		        }
		        is.close();
		 
		        result=sb.toString();
		}catch(Exception e){
		        Log.e("log_tag", "Error converting result "+e.toString());
		}
		
		//parse json data
		try{
		        jArray = new JSONArray(result);
		
		
		}catch(JSONException e){
		        Log.e("log_tag", "Error parsing data "+e.toString());
		}
		return jArray;
		
	}
	
	public JSONArray SelectEventByParticipation(String email){
		JSONArray jArray = null;
		String result = "";
		InputStream is = null;
		//the year data to send
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("email",email));
		 
		//http post
		try{
		        HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost("http://martinezhugo.com/moveon/select_event_by_participation.php");
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        HttpResponse response = httpclient.execute(httppost);
		        HttpEntity entity = response.getEntity();
		        is = entity.getContent();
		}catch(Exception e){
		        Log.e("log_tag", "Error in http connection "+e.toString());
		}
		//convert response to string
		try{
		        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
		        StringBuilder sb = new StringBuilder();
		        String line = null;
		        while ((line = reader.readLine()) != null) {
		                sb.append(line + "\n");
		        }
		        is.close();
		 
		        result=sb.toString();
		}catch(Exception e){
		        Log.e("log_tag", "Error converting result "+e.toString());
		}
		
		//parse json data
		try{
		        jArray = new JSONArray(result);
		
		
		}catch(JSONException e){
		        Log.e("log_tag", "Error parsing data "+e.toString());
		}
		return jArray;
		
	}
	
	public void Fonction(){
		String toshare = null;
		String result = "";
		InputStream is = null;
		//the year data to send
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("year","1980"));
		 
		//http post
		try{
		        HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost("http://martinezhugo.com/script_test.php");
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        HttpResponse response = httpclient.execute(httppost);
		        HttpEntity entity = response.getEntity();
		        is = entity.getContent();
		}catch(Exception e){
		        Log.e("log_tag", "Error in http connection "+e.toString());
		}
		//convert response to string
		try{
		        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
		        StringBuilder sb = new StringBuilder();
		        String line = null;
		        while ((line = reader.readLine()) != null) {
		                sb.append(line + "\n");
		        }
		        is.close();
		 
		        result=sb.toString();
		        toshare = result;
		}catch(Exception e){
		        Log.e("log_tag", "Error converting result "+e.toString());
		}
		 
		//parse json data
		try{
		        JSONArray jArray = new JSONArray(result);
		        for(int i=0;i<jArray.length();i++){
		                JSONObject json_data = jArray.getJSONObject(i);
		                Log.i("log_tag","id: "+json_data.getInt("id")+
		                        ", name: "+json_data.getString("name")+
		                        ", sex: "+json_data.getInt("sex")+
		                        ", birthyear: "+json_data.getInt("birthyear")
		                );
		        }
		
		}catch(JSONException e){
		        Log.e("log_tag", "Error parsing data "+e.toString());
		}
	}
		
}	
	


