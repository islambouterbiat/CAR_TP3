package com.tp3.actors;

import java.util.HashMap;
import java.util.Map;

import com.tp3.service.AkkaService.OccurrenceMot;
import com.tp3.Message.Occurence;

import akka.actor.UntypedActor;

public class Reducer extends UntypedActor {

    private Map<String,Integer> occuMots;
	// on utilise une instance de la classe Map pour stocker le nombre d'occurrences de chaque mot reçu
    public Reducer() {
        occuMots = new HashMap<>();
    }

    @Override
    public void onReceive(Object message) {
		// on ajoute le mot dans la Map avec un compteur initialisé à 0 ou +1 si le mot est déjà présent
        if (message instanceof String) {
            String mot = (String) message;
            occuMots.put(mot, occuMots.getOrDefault(mot, 0) + 1);
			System.out.println(">>> mot : " + ((String) message)+" " +getSelf());
			// on récupère le mot associé à ce message
        } else if (message instanceof OccurrenceMot) {
            String mot = ((OccurrenceMot) message).getMot();
            getSender().tell(new Occurence(occuMots.getOrDefault(mot, 0)), getSelf());
        }
    }
}
	

