package com.dataminer.rest.pojo;

import java.util.concurrent.atomic.AtomicLong;

public class Greeting {

	private final long id;
	private final String name;
	private final String content;
	private static final AtomicLong counter = new AtomicLong();

	public Greeting(String name, String content) {
		this.id = counter.incrementAndGet();
		this.name = name;
		this.content = content;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getContent() {
		return content;
	}
}