package com.dataminer.consumer.impl;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import com.dataminer.consumer.IDataConsumer;
import com.dataminer.websocket.client.SenderClient;

public class WebSocketConsumer implements IDataConsumer {
	private SenderClient client = new SenderClient();

	@Override
	public void consume(String dataAsJson) throws IOException, InterruptedException, ExecutionException {
		client.send(dataAsJson);
	}
}
