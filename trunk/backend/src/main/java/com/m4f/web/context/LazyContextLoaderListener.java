package com.m4f.web.context;

import java.io.Serializable;
import java.util.Calendar;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.XmlWebApplicationContext;

@SuppressWarnings("serial")
public class LazyContextLoaderListener extends ContextLoaderListener implements Serializable {
	
	private static final Logger LOGGER = Logger.getLogger(LazyContextLoaderListener.class.getName());

	/*Application startup event.*/ 
	public void contextInitialized(ServletContextEvent ce) {
		long init = Calendar.getInstance().getTimeInMillis();
		LOGGER.severe("++ Starting load context");
		XmlWebApplicationContext rootCtx = 
			(XmlWebApplicationContext)this.initWebApplicationContext(ce.getServletContext());
		rootCtx.setAllowBeanDefinitionOverriding(true);
		long end = Calendar.getInstance().getTimeInMillis();
		LOGGER.severe("++ Ending load context, elapsed time " + ((end-init)/1000) + " seconds");
	 }
 
	/* Application Shutdown Event */
	public void contextDestroyed(ServletContextEvent ce) {
         super.contextDestroyed(ce);
	}
	
}