package com.m4f.utils.feeds.parser.aspect;

import java.util.logging.Logger;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.School;
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
	pointcut = "target(com.m4f.utils.feeds.parser.ifc.ISchoolsParser) && args(provider))",
	argNames="provider,error", throwing= "error")
	public void registerProviderError(Provider provider, Throwable error) {
		LOGGER.severe("Registering feed processing error in provider " + provider.getName());
		/*ParserErrorEvent parserError = this.eventService.createParserError();
		parserError.setEntityClass(provider.getClass().getName());
		parserError.setEntityId(provider.getId());
		parserError.setCause(new Text(StackTraceUtil.getStackTrace(ex)));
		parserError.setWhen(Calendar.getInstance().getTime());
		parserError.setDumpId(dump.getId());
		try {
			this.eventService.save(parserError);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(ex));
		}*/		
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
	pointcut = "target(com.m4f.utils.feeds.parser.ifc.ICoursesParser) && args(school))",
	argNames="school,error", throwing= "error")
	public void registerSchoolError(School school, Throwable error) {
		LOGGER.severe("Registering feed processing error in school " + school.getName());
		/*ParserErrorEvent parserError = this.eventService.createParserError();
		parserError.setEntityClass(school.getClass().getName());
		parserError.setEntityId(school.getId());
		parserError.setCause(new Text(StackTraceUtil.getStackTrace(ex)));
		parserError.setWhen(Calendar.getInstance().getTime());
		parserError.setDumpId(dump.getId());
		try {
			this.eventService.save(parserError);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(ex));
		}*/
	}
}