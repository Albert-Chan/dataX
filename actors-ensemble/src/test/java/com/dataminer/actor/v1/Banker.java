package com.dataminer.actor.v1;

import com.dataminer.actor.v1.Actor;
import com.dataminer.actor.v1.Message;

public class Banker extends Actor {
	private int assets;
	private int lockedAmount = 0;

	public Banker(String id) {
		super(id);
	}
	
	public void onReceive(Message msg) throws InterruptedException {
		System.out.println(msg.getFrom() + ": " + msg);
		Message response;
		// others ask for money
		if (msg instanceof BorrowMessage) {
			BorrowMessage bm = (BorrowMessage) msg;
			int askFor = bm.getAskFor();
			if (askFor > assets - lockedAmount) {
				response = new Message(this, msg.getFrom(), String.format("no enough assets(%d) to rent.", askFor));
			} else {
				lockedAmount += askFor;
				// ... other operations
				lockedAmount -= askFor;
				assets -= askFor;
				response = new Message(this, msg.getFrom(), String.format("rent out %d.", askFor));
			}

		} else

		// others return money
		if (msg instanceof ReturnMessage) {
			ReturnMessage rm = (ReturnMessage) msg;
			int returnBack = rm.getReturnBack();
			assets += returnBack;
			response = new Message(this, msg.getFrom(), String.format("Confirmed. %d returned.", returnBack));

		} else {
			response = new Message(this, msg.getFrom(), "Unknown message");
		}

		send(response);
		System.out.println("Banker: " + response);
		return;
	}

	public int getAssets() {
		return assets;
	}

	public void setAssets(int assets) {
		this.assets = assets;
	}
}
