package com.m4f.business.service.exception;

public class ServiceNotFoundException extends Exception {
	
	public ServiceNotFoundException() {
		super();
	}

	public ServiceNotFoundException(String arg0) {
		super(arg0);
	}

	public ServiceNotFoundException(Throwable arg0) {
		super(arg0);
	}

	public ServiceNotFoundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
}