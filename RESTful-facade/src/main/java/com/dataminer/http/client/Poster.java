package com.dataminer.http.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

public class Poster {

	public static void main(String[] args) throws ClientProtocolException, IOException {
		String url = "http://localhost:8080/add";

		post(url, Map.of("key", "123456"));
	}

	public static void post(String url, Map<String, String> pairs) throws ClientProtocolException, IOException {
		HttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);

		// Request parameters and other properties.
		List<NameValuePair> params = new ArrayList<NameValuePair>(pairs.size());

		for (Entry<String, String> e : pairs.entrySet()) {
			params.add(new BasicNameValuePair(e.getKey(), e.getValue()));
		}

		httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

		// Execute and get the response.
		HttpResponse response = client.execute(httpPost);
		HttpEntity entity = response.getEntity();

		if (entity != null) {
			InputStream instream = entity.getContent();
			try {
				System.out.println(">>>>>>>>>>>>>>>>>>>" + new String(getBytesFromInputStream(instream)));
			} finally {
				instream.close();
			}
		}
	}
	
	private static byte[] getBytesFromInputStream(InputStream input){
		try {
			byte[] bytes = null;
			byte[] tmpBytes = new byte[1024];
			int num;
			while ((num = input.read(tmpBytes)) != -1) {
				if (bytes == null) {
					bytes = new byte[num];
					System.arraycopy(tmpBytes, 0, bytes, 0, num);
				} else {
					byte[] oldBytes = bytes;
					bytes = new byte[oldBytes.length + num];
					System.arraycopy(oldBytes, 0, bytes, 0, oldBytes.length);
					System.arraycopy(tmpBytes, 0, bytes, oldBytes.length, num);
				}
			}
			return bytes;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
