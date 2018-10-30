package com.dataminer.actor.v2;

import java.util.concurrent.ForkJoinPool;

public class ActorSystem {
	private ForkJoinPool fjp;
	private ActorSystem() {
		this.fjp = new ForkJoinPool();
	}
	
	public void collect(Message msg) {
		Actor sender = msg.getFrom();
		Actor receiver = msg.getTo();
		
	}
}
