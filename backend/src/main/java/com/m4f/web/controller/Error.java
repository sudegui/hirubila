package com.m4f.web.controller;

public class Error {
	
	private String message;
	
	public Error(String m) {
		this.message = m;
	}

	public void setMessage(String m) {
		this.message = m;
	}
	
	public String getMessage() {
		return this.message;
	}
}