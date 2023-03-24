package com.tp3.actors;


import com.tp3.service.AkkaService;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;



public class Mapper extends UntypedActor {

	@Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            // si le message reçu est un string alors on remplace tous ce qui est punctuation et numeros par vide " "
        	String ligne =((String)message).replaceAll("[\\p{Punct}0-9]"," ");
        	// et puis on decoupe la ligne en mots en ustilisant les characters qui ne sont pas des lettres 
            String[] mots =ligne.trim().split("[^\\p{L}]+");
           
            for (String mot : mots) {
                ActorRef reducer = selectReducer(AkkaService.AKKASERVICE.reducers, mot);
                reducer.tell(mot, ActorRef.noSender());
            }
            System.out.println(">>> ligne : " + ((String) message) + getSelf());
        } 
    }

    private ActorRef selectReducer(ActorRef [] reducers, String mot) {
        int randomNumber = (int) (Math.random() * reducers.length);
        return reducers[randomNumber];
    }
}


