package com.dataminer.actor.v1;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public abstract class Actor {
	private String id;

	private BlockingQueue<Message> mailbox = new LinkedBlockingQueue<>();
	private Thread looper;
	private volatile boolean isToClose = false;

	public Actor(String id) {
		this.id = id;
	}

	public void send(Message msg) throws InterruptedException {
		msg.getTo().receive(msg);
	}

	public String getId() {
		return this.id;
	}

	public abstract void onReceive(Message msg) throws InterruptedException;

	public void receive(Message msg) throws InterruptedException {
		mailbox.put(msg);
	}

	public void start() {
		looper = new Thread(() -> {
			while (!isToClose) {
				try {
					Message msg = mailbox.poll(100, TimeUnit.MILLISECONDS);
					if (null != msg) {
						onReceive(msg);
					}
				} catch (InterruptedException e) {

				}
			}
		});
		looper.start();
	}

	public void stop() {
		this.isToClose = true;
	}

}
