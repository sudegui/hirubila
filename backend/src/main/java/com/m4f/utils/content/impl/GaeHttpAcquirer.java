package com.m4f.utils.content.impl;

import java.net.HttpURLConnection;
import java.net.URI;
import java.io.ByteArrayOutputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class GaeHttpAcquirer extends BaseAcquirer{
	
	
	private int connectTimeout = 1000 * 60; // In milliseconds
	private int readTimeout = 1000 * 20; // In milliseconds
	
	
	
	public int getConnectTimeout() {
		return this.connectTimeout;
	}
	
	public void setConnectTimeout(int time) {
		this.connectTimeout = time;
	}
	
	public int getReadTimeout() {
		return this.readTimeout;
	}
	
	public void setReadTimeout(int time) {
		this.readTimeout = time;
	}
	
	@Override
	public ByteArrayOutputStream getContent(URI source)  throws IOException {
		HttpURLConnection connection = null;
		connection = (HttpURLConnection) source.toURL().openConnection();
		ByteArrayOutputStream content = new ByteArrayOutputStream();
		InputStream in = new BufferedInputStream(connection.getInputStream());
		connection.setConnectTimeout(this.connectTimeout);
		connection.setReadTimeout(this.readTimeout);
		try {
			connection.connect();
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				int b;
			      while ((b = in.read()) != -1) {
			        content.write(b);
			      }
			} else {
				throw new IOException("Error stablishing connection, code " + 
						connection.getResponseCode());
			}
		} catch(IOException e) {
			throw e;
		} finally {
			in.close();
		}
		return content;
	}
	
	

}
