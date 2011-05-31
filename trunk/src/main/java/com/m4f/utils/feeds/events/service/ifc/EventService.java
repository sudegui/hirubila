package com.m4f.utils.feeds.events.service.ifc;

import java.util.List;
import java.util.Locale;

import com.m4f.utils.feeds.events.model.Dump;
import com.m4f.utils.feeds.events.model.StoreErrorEvent;
import com.m4f.utils.feeds.events.model.ParserErrorEvent;
import com.m4f.utils.feeds.events.model.StoreSuccessEvent;
import com.m4f.utils.feeds.events.model.SystemEvent;

public interface EventService {
	
	/**
	 * Generics Methods
	 */
	<T extends SystemEvent> void save(T event) throws Exception;
	<T extends SystemEvent> void delete(T event) throws Exception;
	<T extends SystemEvent> T getEvent(Class clazz, Long id) throws Exception;
	<T extends SystemEvent> void deleteAllEventsByDump(Dump dump) throws Exception;
	
	/*****************************************************************
	 * 				Parser Errors Events Methods 
	 *****************************************************************/
	
	ParserErrorEvent createParserError();
	List<ParserErrorEvent> getAllParserErrors() throws Exception;
	ParserErrorEvent getParserError(Long id) throws Exception;
	void deleteAllParseErrors() throws Exception;
	List<ParserErrorEvent> getParserErrorByDump(Dump dump) throws Exception;
	List<ParserErrorEvent> getParserErrorEventByDump(Dump dump, int init, int end, String ordering) throws Exception;
	void deleteParserErrorEventsByDump(Dump dump) throws Exception;
	long countParserErrorEventsByDump(Dump dump) throws Exception;
	long countParserErrorEventsByDump(Dump dump, Locale locale) throws Exception;
	
	/*****************************************************************
	 * 				Dumper Errors Events Methods 
	 *****************************************************************/
	StoreErrorEvent createStoreErrorEvent();
	List<StoreErrorEvent> getAllStoreErrorEvents() throws Exception;
	StoreErrorEvent getStoreErrorEvent(Long id) throws Exception;
	List<StoreErrorEvent> getStoreErrorEventsByDump(Dump dump) throws Exception;
	List<StoreErrorEvent> getStoreErrorEventByDump(Dump dump, int init, int end, String ordering) throws Exception;
	void deleteStoreErrorEventsByDump(Dump dump) throws Exception;
	long countStoreErrorEventsByDump(Dump dump) throws Exception;
	long countStoreErrorEventsByDump(Dump dump, Locale locale) throws Exception;
	
	/*****************************************************************
	 * 				Dumper Success Events Methods 
	 *****************************************************************/
	
	StoreSuccessEvent createStoreSuccessEvent();
	List<StoreSuccessEvent> getAllStoreSuccessEvents() throws Exception;
	StoreSuccessEvent getStoreSuccessEvent(Long id) throws Exception;
	List<StoreSuccessEvent> getStoreSuccessEventByDump(Dump dump) throws Exception;
	List<StoreSuccessEvent> getStoreSuccessEventByDump(Dump dump, Locale locale) throws Exception;
	List<StoreSuccessEvent> getStoreSuccessEventByDump(Dump dump, int init, int end, String ordering) throws Exception;
	List<StoreSuccessEvent> getStoreSuccessEventByDump(Dump dump, int init, int end, String ordering, Locale locale) throws Exception;
	void deleteStoreSuccessEventsByDump(Dump dump) throws Exception;
	long countStoreSuccessEventsByDump(Dump dump) throws Exception;
	long countStoreSuccessEventsByDump(Dump dump, Locale locale) throws Exception;
}