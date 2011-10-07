package com.m4f.web.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.appengine.api.backends.BackendService;
import com.google.appengine.api.backends.BackendServiceFactory;
import com.m4f.web.context.LazyContextLoaderListener;

public class BackendUpServlet extends HttpServlet {
	
	private static final Logger LOGGER = Logger.getLogger(LazyContextLoaderListener.class.getName());
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
 		throws IOException {
		BackendService backendsApi = BackendServiceFactory.getBackendService();
		int currentInstance = backendsApi.getCurrentInstance();
		String backend = backendsApi.getCurrentBackend();
		String address = backendsApi.getBackendAddress(backend);
		LOGGER.severe("--- Current BackednName: " + backend + 
				" instance: " + currentInstance + " address: " + address);
	}
	
}