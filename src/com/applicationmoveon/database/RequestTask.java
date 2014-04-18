package com.applicationmoveon.database;
import java.util.HashMap;

import org.json.JSONArray;

import android.os.AsyncTask;

public class RequestTask extends AsyncTask<HashMap<String,String>, String, JSONArray> {
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
			Database web=new Database();
			if(request.get("Request").equals("SelectEvent")){
				return web.SelectEvent();
			}
			return null;
			
		}
		
		protected JSONArray AddUser(HashMap<String,String> request){
			
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