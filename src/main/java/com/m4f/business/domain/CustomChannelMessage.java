package com.m4f.business.domain;

public class CustomChannelMessage {
	
	private String html = "";
	
	public CustomChannelMessage(String message) {
		this.html = message;
	}
	
	public String getHtml() {
		return this.html;
	}
	
	public void setHtml(String h) {
		this.html = h;
	}
}