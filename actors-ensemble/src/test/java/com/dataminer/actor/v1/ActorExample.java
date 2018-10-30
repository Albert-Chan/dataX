package com.dataminer.actor.v1;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ActorExample {
	public static void main(String[] args) {
		Banker banker = new Banker("Mr. Banker");
		banker.setAssets(100_000);
		banker.start();
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(7);
		Borrower[] borrowers = new Borrower[10];
		for (int i = 0; i < 10; i++) {
			final int indexAsAmount = i;
			Borrower borrower = new Borrower(String.valueOf(i));
			executor.execute(() -> {
				borrower.start(); 
				try {
					borrower.send(new BorrowMessage(borrower, banker, "", indexAsAmount));
					borrower.send(new ReturnMessage(borrower, banker, "", indexAsAmount));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			borrowers[i] = borrower;
		}
		executor.shutdown();

		try {
			TimeUnit.SECONDS.sleep(20);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < 10; i++) {
			borrowers[i].stop();
		}

		banker.stop();
	}

}
