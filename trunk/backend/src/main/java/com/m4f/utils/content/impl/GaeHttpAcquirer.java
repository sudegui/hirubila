package com.m4f.utils.content.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.logging.Logger;

public class GaeHttpAcquirer extends BaseAcquirer{
	private static final Logger LOGGER = Logger.getLogger(GaeHttpAcquirer.class.getName());
	
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
	public ByteArrayOutputStream getContent(URI source)  throws Exception {
		int retries = 5;
		HttpURLConnection connection = null;
		ByteArrayOutputStream content = null;
		InputStream in = null;
		try {			
			while(retries > 0) {
				try {
					connection = (HttpURLConnection) source.toURL().openConnection();
					content = new ByteArrayOutputStream();
					in = new BufferedInputStream(connection.getInputStream());
					connection.setConnectTimeout(this.connectTimeout);
					connection.setReadTimeout(this.readTimeout);
					connection.connect();
					if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
						int b;
						while ((b = in.read()) != -1) {
							content.write(b);
						}
					    retries = -1; 
					} else {
						throw new IOException("Error stablishing connection, code " + 
								connection.getResponseCode());
					}
				} catch(Exception e) {
					LOGGER.warning("----------------------------------");
					LOGGER.warning("Failed getting soure: " + source);
					LOGGER.warning("Retries num: " + retries);
					LOGGER.warning("----------------------------------");
					retries--;
					if(retries <= 0) {
						throw e;
					}
				}
			}
		} catch(Exception e) {
			throw e;
		} finally {
			if(in != null) {
				in.close();
			}

		}
		return content;
	}
	
	

}
