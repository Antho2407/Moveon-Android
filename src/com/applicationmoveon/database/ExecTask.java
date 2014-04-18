package com.applicationmoveon.database;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

public class ExecTask extends AsyncTask<HashMap<String,String>, String, Boolean> {
		/*
		 * Cette méthode s'exécute dans le thread
		 * de l'interface. C'est le bon endroit
		 * pour notifier l'usager qu'une tâche
		 * plus longue commence (par exemple,
		 * afficher une barre de progression).
		 * 
		 */
		protected Database db;
		
		public ExecTask(){
			db =new Database();
		}
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
		protected Boolean doInBackground(HashMap<String,String>... params) {
			HashMap<String, String> request = params[0];
			String query = request.get("Request");
			
			if(query.equals("addEvent")){
				return addEvent(request);
			}else if(query.equals("isValidCombination")){
				return isValidCombination(request);
			}else if(query.equals("addUser")){
				return addUser(request);
			}
			
			return false;
			
		}
		
		protected Boolean isValidCombination(HashMap<String,String> request){
			
			if(request.containsKey("email") && request.containsKey("motdepasse")){
				JSONArray result = db.SelectUserByMail(request.get("email"));
				if(result.length() == 1){
					JSONObject json_data = null;
					 try {
						 json_data = result.getJSONObject(0);
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						if(json_data.getString("motdepasse").equals(request.get("motdepasse"))){
							return true;
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return false;
			}
				
				
			return false;
		}
		
		protected Boolean addEvent(HashMap<String,String> request){
			
			if(request.containsKey("title") && request.containsKey("description")&& request.containsKey("location")&& request.containsKey("date_debut")&& request.containsKey("heure_debut")&& request.containsKey("heure_fin")&& request.containsKey("date_fin")
			&& request.containsKey("participants")&& request.containsKey("id_createur")&& request.containsKey("date_creation")&& request.containsKey("state")
			&& request.containsKey("urlimage")){
				db.addEvent(request.get("title"), request.get("location"), request.get("description"), request.get("date_debut"),request.get("heure_debut"), request.get("date_fin"), 
				request.get("heure_fin"),request.get("participants"), request.get("id_createur"), request.get("date_creation"), request.get("state"), request.get("urlimage"));
				return true;
			}
			return false;
			
			
		}

		protected Boolean addUser(HashMap<String,String> request){
			if(request.containsKey("firstname")&&request.containsKey("lastname")&&request.containsKey("email")&&request.containsKey("password")&&request.containsKey("urlimage")){
				db.addUser(request.get("firstname"), request.get("lastname"), request.get("email"), request.get("password"), request.get("urlimage"));
				return true;
			}
			return false;
			
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