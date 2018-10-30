package com.dataminer.actor.v1;

import com.dataminer.actor.v1.Actor;
import com.dataminer.actor.v1.Message;

public class Borrower extends Actor {
	
	public Borrower(String id) {
		super(id);
	}
	
	public void onReceive(Message msg) {
		System.out.println(">>>" + msg.getFrom() + ": " + msg);
	}
}
