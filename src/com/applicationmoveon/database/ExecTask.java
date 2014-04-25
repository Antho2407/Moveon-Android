package com.applicationmoveon.database;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class ExecTask extends
		AsyncTask<HashMap<String, String>, String, Boolean> {
	/*
	 * Cette m�thode s'ex�cute dans le thread de l'interface. C'est le bon
	 * endroit pour notifier l'usager qu'une t�che plus longue commence (par
	 * exemple, afficher une barre de progression).
	 */
	protected Database db;

	public ExecTask() {
		db = new Database();
	}

	protected void onPreExecute() {
		// Affiche la barre de progression
	}

	/*
	 * Cette m�thode est ex�cut�e dans son propre thread. C'est l� o� le travail
	 * le plus lourd se passe. On pourra appeler publishProgress durant
	 * l'ex�cution de cette m�thode pour mettre � jour le thread d'interface.
	 */
	protected Boolean doInBackground(HashMap<String, String>... params) {
		HashMap<String, String> request = params[0];
		String query = request.get("Request");

		if (query.equals("addEvent")) {
			return addEvent(request);
		} else if (query.equals("isValidCombination")) {
			return isValidCombination(request);
		} else if (query.equals("addUser")) {
			return addUser(request);
		} else if (query.equals("addParticipants")) {
			return addParticipants(request);
		} else if (query.equals("addVote")) {
			return addVote(request);
		} else if (query.equals("updateTemperature")) {
			return updateTemperature(request);
		}

		return false;

	}
	
	protected Boolean updateTemperature(HashMap<String, String> request) {
		if (request.containsKey("id_event") && request.containsKey("temperature")) {
			db.updateTemperature(request.get("id_event"), request.get("temperature"));
			return true;
		}
		return false;
	}

	protected Boolean addParticipants(HashMap<String, String> request) {
		if (request.containsKey("id_event") && request.containsKey("email")) {
			db.addParticipants(request.get("id_event"), request.get("email"));
			return true;
		}
		return false;
	}

	protected Boolean isValidCombination(HashMap<String, String> request) {

		if (request.containsKey("email") && request.containsKey("password")) {
			JSONArray result = db.SelectUserByMail(request.get("email"));

			if (result != null && result.length() == 1) {

				JSONObject json_data = null;
				try {
					json_data = result.getJSONObject(0);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					if (json_data.getString("password").equals(
							request.get("password"))) {
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

	protected Boolean addEvent(HashMap<String, String> request) {

		if (request.containsKey("title") && request.containsKey("description")
				&& request.containsKey("location")
				&& request.containsKey("latitude")
				&& request.containsKey("longitude")
				&& request.containsKey("date_debut")
				&& request.containsKey("heure_debut")
				&& request.containsKey("heure_fin")
				&& request.containsKey("date_fin")
				&& request.containsKey("participants")
				&& request.containsKey("id_createur")
				&& request.containsKey("date_creation")
				&& request.containsKey("state")
				&& request.containsKey("urlimage")
				&& request.containsKey("temperature")
				&& request.containsKey("likes")
				&& request.containsKey("dislikes")) {
			db.addEvent(request.get("title"), request.get("location"),
					request.get("latitude"), request.get("longitude"),
					request.get("description"), request.get("date_debut"),
					request.get("heure_debut"), request.get("date_fin"),
					request.get("heure_fin"), request.get("participants"),
					request.get("id_createur"), request.get("date_creation"),
					request.get("state"), request.get("urlimage"),
					request.get("temperature"),
					request.get("likes"), request.get("dislikes"));
			return true;
		}
		return false;
	}

	protected Boolean addVote(HashMap<String, String> request) {
		if (request.containsKey("id_event") && request.containsKey("vote")) {
			db.addVote(request.get("id_event"), request.get("vote"));
			return true;
		}
		return false;
	}

	protected Boolean addUser(HashMap<String, String> request) {
		if (request.containsKey("firstname") && request.containsKey("lastname")
				&& request.containsKey("email")
				&& request.containsKey("password")
				&& request.containsKey("urlimage")) {
			db.addUser(request.get("firstname"), request.get("lastname"),
					request.get("email"), request.get("password"),
					request.get("urlimage"));
			return true;
		}
		return false;

	}

	/*
	 * Cette m�thode est appel�e dans le thread d'interface lorsque
	 * publishProgress est appel�e dans doInBackground. Les param�tres sont
	 * pass�s directement de l'une � l'autre.
	 */
	protected void onProgressUpdate(String... s) {
		// �x�cute dans le thread interface, si le thread non-interface
		// appelle publishProgress � l'int�rieur de doInBackground
	}

	/*
	 * Cette m�thode s'ex�cute dans le thread d'interface. C'est l'endroit o� on
	 * r�agit g�n�ralement � la compl�tion du processus en arri�re-plan, par
	 * exemple en mettant � jour l'interface avec les donn�es obtenues.
	 */
	protected void onPostExecute(String web) {
		// Cache la barre de progression

	}

}