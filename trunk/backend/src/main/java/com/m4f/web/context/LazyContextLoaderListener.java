package com.m4f.web.context;

import java.io.Serializable;
import java.util.Calendar;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

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
	
	private void createDeferredTaskLoader() {
		LOGGER.severe("+++++ Task by time ........");
		TaskOptions taskOptions = TaskOptions.Builder.withPayload(new ContextLoaderTask());
		taskOptions.taskName("" + Calendar.getInstance().getTimeInMillis());
		//QueueFactory.getDefaultQueue().add(taskOptions);
	}
	
	
	private class ContextLoaderTask extends ContextLoader implements DeferredTask  {
	
		@Override
		public void run() {
			LOGGER.severe("+++++ Context loading started........");
			XmlWebApplicationContext rootContext = (XmlWebApplicationContext)this.getCurrentWebApplicationContext();
			ClassPathXmlApplicationContext servicesContext = (ClassPathXmlApplicationContext) rootContext.getBean("servicesContext");
			servicesContext.setParent(rootContext);
			servicesContext.setConfigLocation("services-config.xml");
			servicesContext.stop();
			servicesContext.refresh();
			servicesContext.start();
			LOGGER.severe("+++++ Context loading finished........");
		}
			
	}
	
}