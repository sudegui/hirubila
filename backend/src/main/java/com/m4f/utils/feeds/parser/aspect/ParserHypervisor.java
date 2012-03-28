package com.m4f.utils.feeds.parser.aspect;

import java.util.Calendar;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.Text;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.School;
import com.m4f.utils.StackTraceUtil;
import com.m4f.utils.feeds.events.model.Dump;
import com.m4f.utils.feeds.events.model.ParserErrorEvent;
import com.m4f.utils.feeds.events.service.ifc.EventService;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.aspectj.lang.annotation.AfterThrowing;

@Aspect
public class ParserHypervisor {
	
	@Autowired
	private EventService eventService;
	private static final Logger LOGGER = Logger.getLogger(ParserHypervisor.class.getName());
	
	
	public ParserHypervisor() { }
	
	public ParserHypervisor(EventService eventService) {
		this.eventService = eventService;
	}
	
	
	/**
	 * Intercepts exceptions produced into methods declared in 
	 * com.m4f.utils.feeds.ifc.ISchoolsParser interface and insert
	 * an entry into error log subsystem.
	 * 
	 * @param ex, exception launched.
	 * @param provider, argument of method that cause the exception.
	 * @throws Exception 
	 */
	
	@AfterThrowing(
	pointcut = "target(com.m4f.utils.feeds.parser.ifc.ISchoolsParser) && args(provider, dump))",
	argNames="provider,dump, error", throwing= "error")
	public void registerProviderError(Provider provider, Dump dump, Throwable error) {
		LOGGER.severe("Registering feed processing error in provider " + provider.getName());
		if(dump != null) {
			ParserErrorEvent parserError = this.eventService.createParserError();
			parserError.setEntityClass(provider.getClass().getName());
			parserError.setEntityId(provider.getId());
			parserError.setCause(new Text(StackTraceUtil.getStackTrace(error)));
			parserError.setWhen(Calendar.getInstance().getTime());
			parserError.setDumpId(dump.getId());
			try {
				this.eventService.save(parserError);
			} catch(Exception e) {
				LOGGER.severe(StackTraceUtil.getStackTrace(e));
			}
		}
	}
	
	/**
	 * Intercepts exceptions produced into methods declared in 
	 * com.m4f.utils.feeds.ifc.ICoursesParser interface and insert
	 * an entry into error log subsystem.
	 * 
	 * @param ex, exception launched
	 * @param school, argument of method that cause the exception.
	 */
	@AfterThrowing(
	pointcut = "target(com.m4f.utils.feeds.parser.ifc.ICoursesParser) && args(school, dump))",
	argNames="school,dump, error", throwing= "error")
	public void registerSchoolError(School school, Dump dump, Throwable error) {
		LOGGER.severe("Registering feed processing error in school " + school.getName());
		if(dump != null) {
			ParserErrorEvent parserError = this.eventService.createParserError();
			parserError.setEntityClass(school.getClass().getName());
			parserError.setEntityId(school.getId());
			parserError.setCause(new Text(StackTraceUtil.getStackTrace(error)));
			parserError.setWhen(Calendar.getInstance().getTime());
			parserError.setDumpId(dump.getId());
			try {
				this.eventService.save(parserError);
			} catch(Exception e) {
				LOGGER.severe(StackTraceUtil.getStackTrace(e));
			}
		}
	}
}