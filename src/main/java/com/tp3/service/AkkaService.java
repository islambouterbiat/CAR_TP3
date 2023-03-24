package com.tp3.service;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.tp3.Message.Occurence;
import com.tp3.actors.Mapper;
import com.tp3.actors.Reducer;
import com.typesafe.config.ConfigFactory;

import akka.actor.Inbox;
import scala.concurrent.duration.FiniteDuration;

@Service
public class AkkaService {

	// une seule instance AKKASERVICE (singleton)
	// appelée de manière statique à partir de n'importe quel endroit dans le code
	// pour accéder à ses méthodes.
	public static AkkaService AKKASERVICE = new AkkaService();

	public ActorSystem sysMappers;

	public ActorSystem sysReducers;

	public ActorRef[] mappers;

	public ActorRef[] reducers;

	public Inbox inbox;

	public ActorRef reducer1;

	// Constructeur, deux systèmes d'acteurs : "sysMapper" et "sysReducer".
	private AkkaService() {
		this.sysMappers = ActorSystem.create("sysMapper", ConfigFactory.load("application1.conf"));
		this.sysReducers = ActorSystem.create("sysReducer", ConfigFactory.load("application2.conf"));
		this.inbox = Inbox.create(sysReducers);

	}

	public void init() {

		if (reducer1 != null) {
			sysMappers.stop(mappers[0]);
			sysMappers.stop(mappers[1]);
			sysMappers.stop(mappers[2]);
			sysReducers.stop(reducers[0]);
			sysReducers.stop(reducers[1]);
		}

		try {
			Thread.sleep(1000);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		reducer1 = sysReducers.actorOf(Props.create(Reducer.class), "Reducer1");
		reducers = new ActorRef[] { reducer1, sysReducers.actorOf(Props.create(Reducer.class), "Reducer2") };
		mappers = new ActorRef[] { sysMappers.actorOf(Props.create(Mapper.class), "Mapper1"),
				sysMappers.actorOf(Props.create(Mapper.class), "Mapper2"),
				sysMappers.actorOf(Props.create(Mapper.class), "Mapper3") };

	}

	public void readFileLines(File file) {
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
			int numLine = 0;
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				this.mappers[numLine % this.mappers.length].tell(line, ActorRef.noSender());
				numLine += 1;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void analyseFile(String fileName) {
		File file = new File(
				(new File(System.getProperty("user.dir")) + "\\src\\main\\resources\\static\\" + fileName));
		this.readFileLines(file);
	}

	// Une classe interne 
	public static class OccurrenceMot {
		public String mot;

		public OccurrenceMot(String mot) {
			this.mot = mot;
		}

		public String getMot() {
			return this.mot;
		}
	}

	public int getWordOccurrences(String mot) {
		this.inbox.send(this.reducers[0], new OccurrenceMot(mot));
		Object replySReducer = inbox.receive(FiniteDuration.create(5, TimeUnit.SECONDS));
		this.inbox.send(this.reducers[1], new OccurrenceMot(mot));
		Object replyFReducer = inbox.receive(FiniteDuration.create(5, TimeUnit.SECONDS));

		return ((Occurence) replySReducer).occurrence + ((Occurence) replyFReducer).occurrence;
	}

}