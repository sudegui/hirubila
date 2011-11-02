package com.m4f.utils.content.impl;

import java.net.HttpURLConnection;
import java.net.URI;
import java.io.ByteArrayOutputStream;

public class GaeHttpAcquirer extends BaseAcquirer{
	
	
	private int timeout = 1000 * 10; // In milliseconds
	
	public int getTimeout() {
		return this.timeout;
	}
	
	public void setTimeout(int time) {
		this.timeout = time;
	}
	
	@Override
	public ByteArrayOutputStream getContent(URI source) throws Exception {
		HttpURLConnection connection = null;
		connection = (HttpURLConnection) source.toURL().openConnection();
		connection.setConnectTimeout(this.timeout); //10 seg.
		connection.setReadTimeout(this.timeout); //10 seg.
		connection.connect();
		byte [] buf = null;
		if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			buf = this.getBytesFromInputStream(connection.getInputStream());
		} else {
			throw new Exception("Error stablishing connection, code " + 
					connection.getResponseCode());
		}
		ByteArrayOutputStream content = new ByteArrayOutputStream(); 
		content.write(buf); 
		return content;
	}
	
	

}
