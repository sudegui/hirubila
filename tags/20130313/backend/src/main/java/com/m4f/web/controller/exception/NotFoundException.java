package com.m4f.web.controller.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Resource not found")
public class NotFoundException extends Exception {

	public NotFoundException() {
		
	}
	
	public NotFoundException(String error) {
		super(error);
	}

	public NotFoundException(Error e) {
		super(e);
	}

}
