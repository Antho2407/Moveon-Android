package com.applicationmoveon.database;
import java.util.HashMap;

import org.json.JSONArray;

import android.os.AsyncTask;
import android.util.Log;

public class RequestTask extends AsyncTask<HashMap<String,String>, String, JSONArray> {
		
		protected Database db;
		
		public RequestTask(){
			db = new Database();
		}
		/*
		 * Cette méthode s'exécute dans le thread
		 * de l'interface. C'est le bon endroit
		 * pour notifier l'usager qu'une tâche
		 * plus longue commence (par exemple,
		 * afficher une barre de progression).
		 * 
		 */
		protected void onPreExecute() {
			// Affiche la barre de progression
		}
		
		/*
		 * Cette méthode est exécutée dans son
		 * propre thread. C'est là où le travail le plus
		 * lourd se passe. On pourra appeler publishProgress
		 * durant l'exécution de cette méthode pour mettre
		 * à jour le thread d'interface.
		 * 
		 */
		protected JSONArray doInBackground(HashMap<String,String>... params) {
			HashMap<String, String> request = params[0];
			String query = request.get("Request");
			if(query.equals("SelectEvent")){
				return db.SelectEvent();
			}else if(query.equals("SelectEventById")){
				return SelectEventById(request);
			}else if(query.equals("SelectUserByEmail")){
				return SelectUserByEmail(request);
			}else if(query.equals("SelectEventByUserMail")){
				return SelectEventByUserMail(request);
			}else if(query.equals("SelectEventByParticipation")){
				return SelectEventByParticipation(request);
			}else if(query.equals("SelectEventBySearch")){
				return SelectEventBySearch(request);
			}
			return null;
			
		}
		
		protected JSONArray SelectEventById(HashMap<String,String> request){
			if(request.containsKey("id_event")){
				return db.SelectEventById(request.get("id_event"));
			}
			return null;
		}
		
		protected JSONArray SelectEventBySearch(HashMap<String,String> request){
			if(request.containsKey("search")){
				return db.SelectEventBySearch(request.get("search"));
			}
			return null;
		}
		
		protected JSONArray SelectUserByEmail(HashMap<String,String> request){
			if(request.containsKey("email")){
				return db.SelectUserByMail(request.get("email"));
			}
			return null;
		}
		
		protected JSONArray SelectEventByUserMail(HashMap<String,String> request){
			if(request.containsKey("email")){
				return db.SelectEventByUserMail(request.get("email"));
			}
			return null;
		}
		
		protected JSONArray SelectEventByParticipation(HashMap<String,String> request){
			if(request.containsKey("email")){
				return db.SelectEventByParticipation(request.get("email"));
			}
			return null;
		}
		
		/*
		 * Cette méthode est appelée dans le thread
		 * d'interface lorsque publishProgress est
		 * appelée dans doInBackground. Les paramètres
		 * sont passés directement de l'une à l'autre.
		 * 
		 */
		protected void onProgressUpdate(String... s) {
			// éxécute dans le thread interface, si le thread non-interface
			// appelle publishProgress à l'intérieur de doInBackground
		}
		
		/*
		 * Cette méthode s'exécute dans le thread
		 * d'interface. C'est l'endroit où on
		 * réagit généralement à la complétion du
		 * processus en arrière-plan, par exemple
		 * en mettant à jour l'interface avec
		 * les données obtenues.
		 * 
		 */
		protected void onPostExecute(String web) {
			// Cache la barre de progression
	
			
		}

}