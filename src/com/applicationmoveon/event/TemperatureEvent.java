package com.applicationmoveon.event;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import android.graphics.Color;

// Classe permettant de gerer la temperature d'un evenement
public abstract class TemperatureEvent {
	
	// Palier de nombre de participants au-dessus duquel on 
	// considere un evenement a tres fort potentiel
	public final static int AMAZING_NB_OF_PARTICIPANT = 100;
	
	// Calculer la temperature d'un evenement
	public static float getTemperature(int nbOfParticipants, int likes, int dislikes){
		float result = 100;
		float coeffNbOfParticipants;
		float coeffLikes = 0;
		float coeffDislikes = 0;
		
		// Definir un coefficient qui rendra le nombre de like/dislike
		// moins important selon le nombre de participants
		if(likes>0)
			coeffLikes = likes/nbOfParticipants;
		if(dislikes>0)
			coeffDislikes = dislikes/nbOfParticipants;
		
		// Premier coefficient lie au nombre de participants
		// Si le nombre depasse le palier fixe
		// La temperature de base reste a 100 %
		if(nbOfParticipants > AMAZING_NB_OF_PARTICIPANT)
			coeffNbOfParticipants = 1;
		else{
			coeffNbOfParticipants = nbOfParticipants/100;
		}
		
		// Appliquer le premier coefficient
		result *= coeffNbOfParticipants;
		
		// Appliquer les coefficients de like / dislike aux votes
		// Modifier la temperature en consequence
		result += coeffLikes*likes;
		result -= coeffDislikes*dislikes;
		
		if(result<0)
			return 0;
		if(result>100)
			return 100;
		
		return result;
	}
	
	// Recuperer une couleur adaptee pour l'affichage d'un event
	public static float getColor(int temperature){
		if(temperature > 75){
			return BitmapDescriptorFactory.HUE_RED;
		}else if(temperature < 30){
			return BitmapDescriptorFactory.HUE_YELLOW;
		}else{
			return BitmapDescriptorFactory.HUE_ORANGE;
		}
	}
}
