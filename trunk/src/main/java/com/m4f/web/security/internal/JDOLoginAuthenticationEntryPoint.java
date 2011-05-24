package com.m4f.web.security.internal;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;


public class JDOLoginAuthenticationEntryPoint implements
		AuthenticationEntryPoint {
	
	private static final Logger LOGGER = Logger.getLogger(JDOLoginAuthenticationEntryPoint.class.getName());
	
	@Override
	public void commence(HttpServletRequest arg0, HttpServletResponse arg1,
			AuthenticationException arg2) throws IOException, ServletException {
		// TODO Auto-generated method stub

	}

}
