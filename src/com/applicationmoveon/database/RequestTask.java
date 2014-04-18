package com.applicationmoveon.database;
import java.util.HashMap;

import org.json.JSONArray;

import android.os.AsyncTask;

public class RequestTask extends AsyncTask<HashMap<String,String>, String, JSONArray> {
		/*
		 * Cette m�thode s'ex�cute dans le thread
		 * de l'interface. C'est le bon endroit
		 * pour notifier l'usager qu'une t�che
		 * plus longue commence (par exemple,
		 * afficher une barre de progression).
		 * 
		 */
		protected void onPreExecute() {
			// Affiche la barre de progression
		}
		
		/*
		 * Cette m�thode est ex�cut�e dans son
		 * propre thread. C'est l� o� le travail le plus
		 * lourd se passe. On pourra appeler publishProgress
		 * durant l'ex�cution de cette m�thode pour mettre
		 * � jour le thread d'interface.
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
		 * Cette m�thode est appel�e dans le thread
		 * d'interface lorsque publishProgress est
		 * appel�e dans doInBackground. Les param�tres
		 * sont pass�s directement de l'une � l'autre.
		 * 
		 */
		protected void onProgressUpdate(String... s) {
			// �x�cute dans le thread interface, si le thread non-interface
			// appelle publishProgress � l'int�rieur de doInBackground
		}
		
		/*
		 * Cette m�thode s'ex�cute dans le thread
		 * d'interface. C'est l'endroit o� on
		 * r�agit g�n�ralement � la compl�tion du
		 * processus en arri�re-plan, par exemple
		 * en mettant � jour l'interface avec
		 * les donn�es obtenues.
		 * 
		 */
		protected void onPostExecute(String web) {
			// Cache la barre de progression
	
			
		}

}