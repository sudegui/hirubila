package com.m4f.web.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.m4f.business.service.ifc.IServiceLocator;
import com.m4f.web.context.LazyContextLoaderListener;

public class WarmUpServlet extends HttpServlet {
	
	private static final Logger LOGGER = Logger.getLogger(LazyContextLoaderListener.class.getName());
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
 		throws IOException {
		LOGGER.severe("+++++++++++++++++++++++++++++++++++++++++++++klao");
		ApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		IServiceLocator serviceLocator = (IServiceLocator)context.getBean("serviceLocator");
		serviceLocator.init();
	}
	
}