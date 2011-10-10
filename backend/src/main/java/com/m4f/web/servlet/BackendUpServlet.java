package com.m4f.web.servlet;

import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.backends.BackendService;
import com.google.appengine.api.backends.BackendServiceFactory;

public class BackendUpServlet extends HttpServlet {
	
	private static final Logger LOGGER = Logger.getLogger(BackendUpServlet.class.getName());
	
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