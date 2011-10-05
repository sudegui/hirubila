package com.m4f.utils.feeds.aop;

import java.util.Calendar;
import java.util.logging.Logger;
import com.google.appengine.api.datastore.Text;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.School;
import com.m4f.utils.StackTraceUtil;
import com.m4f.utils.feeds.events.model.Dump;
import com.m4f.utils.feeds.events.model.ParserErrorEvent;
import com.m4f.utils.feeds.events.service.ifc.EventService;

public class ParserHypervisor {
	
	private EventService eventService;
	private static final Logger LOGGER = Logger.getLogger(ParserHypervisor.class.getName());
	
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
	public void registerProviderError(Dump dump, Throwable ex, Provider provider) {
		ParserErrorEvent parserError = this.eventService.createParserError();
		parserError.setEntityClass(provider.getClass().getName());
		parserError.setEntityId(provider.getId());
		parserError.setCause(new Text(StackTraceUtil.getStackTrace(ex)));
		parserError.setWhen(Calendar.getInstance().getTime());
		parserError.setDumpId(dump.getId());
		try {
			this.eventService.save(parserError);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(ex));
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
	public void registerSchoolError(Dump dump, Throwable ex, School school) {
		ParserErrorEvent parserError = this.eventService.createParserError();
		parserError.setEntityClass(school.getClass().getName());
		parserError.setEntityId(school.getId());
		parserError.setCause(new Text(StackTraceUtil.getStackTrace(ex)));
		parserError.setWhen(Calendar.getInstance().getTime());
		parserError.setDumpId(dump.getId());
		try {
			this.eventService.save(parserError);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(ex));
		}
	}
}