package com.m4f.utils.feeds.events.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.m4f.utils.feeds.events.model.Dump;
import com.m4f.utils.feeds.events.model.StoreErrorEvent;
import com.m4f.utils.feeds.events.model.StoreSuccessEvent;
import com.m4f.utils.feeds.events.model.ParserErrorEvent;
import com.m4f.utils.feeds.events.model.SystemEvent;
import com.m4f.utils.feeds.events.service.ifc.EventService;
import com.m4f.utils.i18n.dao.ifc.DAOSupport;

public class JdoEventServiceImpl extends JdoBaseService implements EventService {
	
	public JdoEventServiceImpl(DAOSupport dao) {
		super(dao);
	}
	
	/**
	 * Generics Methods implementation
	 */
	
	@Override
	public <T extends SystemEvent> void save(T event) throws Exception {
		this.DAO.saveOrUpdate(event);
	}
	
	@Override
	public <T extends SystemEvent> void delete(T event) throws Exception {
		this.DAO.delete(event);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends SystemEvent> T getEvent(Class clazz, Long id) throws Exception {
		return (T) this.DAO.findById(clazz, id);
	}
	
	public <T extends SystemEvent> void deleteAllEventsByDump(Dump dump) throws Exception {
		this.deleteParserErrorEventsByDump(dump);
		this.deleteStoreErrorEventsByDump(dump);
		this.deleteStoreSuccessEventsByDump(dump);
	}
	
	/*****************************************************************
	 * 				Parser Errors Events Methods 
	 *****************************************************************/
	
	@Override
	public ParserErrorEvent createParserError() {
		return (ParserErrorEvent)this.DAO.createInstance(ParserErrorEvent.class);
	}

	@Override
	public List<ParserErrorEvent> getAllParserErrors() throws Exception {
		return this.DAO.findAll(ParserErrorEvent.class);
	}

	@Override
	public ParserErrorEvent getParserError(Long id) throws Exception {
		return (ParserErrorEvent)this.DAO.findById(ParserErrorEvent.class, id);
	}
	
	@Override
	public List<ParserErrorEvent> getParserErrorByDump(Dump dump) throws Exception {
		String filter = "dumpId == dumpIdParam";
		String params = "java.lang.Long dumpIdParam";
		return (List<ParserErrorEvent>) this.DAO.findEntities(ParserErrorEvent.class, 
				filter, params, new Long[] {dump.getId()}, null);
	}
	
	@Override
	public List<ParserErrorEvent> getParserErrorEventByDump(Dump dump, int init, 
			int end, String ordering) throws Exception {
		String filter = "dumpId == dumpIdParam";
		String params = "java.lang.Long dumpIdParam";
		return (List<ParserErrorEvent>) this.DAO.findEntities(ParserErrorEvent.class, 
				filter, params, new Long[] {dump.getId()}, null);
	}
	
	@Override
	public void deleteAllParseErrors() throws Exception {
		this.DAO.delete(this.DAO.findAll(ParserErrorEvent.class));
	}
	
	@Override
	public void deleteParserErrorEventsByDump(Dump dump) throws Exception {
		this.DAO.delete(this.getParserErrorByDump(dump));
	}
	
	@Override
	public long countParserErrorEventsByDump(Dump dump) throws Exception {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("dumpId", dump.getId());
		return this.DAO.count(ParserErrorEvent.class, filter);
	}
	
	@Override
	public long countParserErrorEventsByDump(Dump dump, Locale locale) throws Exception {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("dumpId", dump.getId());
		filter.put("language", locale.getLanguage());
		return this.DAO.count(ParserErrorEvent.class, filter);
	}
	
	/*****************************************************************
	 * 				Dumper Errors Events Methods 
	 *****************************************************************/
	
	@Override
	public StoreErrorEvent createStoreErrorEvent() {
		return (StoreErrorEvent)this.DAO.createInstance(StoreErrorEvent.class);
	}

	@Override
	public List<StoreErrorEvent> getAllStoreErrorEvents() throws Exception {
		return this.DAO.findAll(StoreErrorEvent.class);
	}
	
	@Override
	public StoreErrorEvent getStoreErrorEvent(Long id) throws Exception {
		return (StoreErrorEvent)this.DAO.findById(StoreErrorEvent.class, id);
	}
	
	@Override
	public List<StoreErrorEvent> getStoreErrorEventsByDump(Dump dump) throws Exception {
		String filter = "dumpId == dumpIdParam";
		String params = "java.lang.Long dumpIdParam";
		return (List<StoreErrorEvent>) this.DAO.findEntities(StoreErrorEvent.class, 
				filter, params, new Long[] {dump.getId()}, null);
	}
	
	@Override
	public List<StoreErrorEvent> getStoreErrorEventByDump(Dump dump, int init, 
			int end, String ordering) throws Exception {
		String filter = "dumpId == dumpIdParam";
		String params = "java.lang.Long dumpIdParam";
		return (List<StoreErrorEvent>) this.DAO.findEntities(StoreErrorEvent.class, 
				filter, params, new Long[] {dump.getId()}, null);
	}
	
	
	@Override
	public void deleteStoreErrorEventsByDump(Dump dump) throws Exception {
		this.DAO.delete(this.getStoreErrorEventsByDump(dump));
	}
	
	@Override
	public long countStoreErrorEventsByDump(Dump dump) throws Exception {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("dumpId", dump.getId());
		return this.DAO.count(StoreErrorEvent.class, filter);
	}
	
	@Override
	public long countStoreErrorEventsByDump(Dump dump, Locale locale) throws Exception {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("dumpId", dump.getId());
		filter.put("language", locale.getLanguage());
		return this.DAO.count(StoreErrorEvent.class, filter);
	}
	
	/*****************************************************************
	 * 				Dumper Success Events Methods 
	 *****************************************************************/
	@Override
	public StoreSuccessEvent createStoreSuccessEvent() {
		return (StoreSuccessEvent) this.DAO.createInstance(StoreSuccessEvent.class);
	}
	
	@Override
	public List<StoreSuccessEvent> getAllStoreSuccessEvents() throws Exception {
		return this.DAO.findAll(StoreSuccessEvent.class);
	}
	
	@Override
	public StoreSuccessEvent getStoreSuccessEvent(Long id) throws Exception {
		return (StoreSuccessEvent) this.DAO.findById(StoreSuccessEvent.class, id);
	}
	
	@Override
	public List<StoreSuccessEvent> getStoreSuccessEventByDump(Dump dump) throws Exception {
		String filter = "dumpId == dumpIdParam";
		String params = "java.lang.Long dumpIdParam";
		return (List<StoreSuccessEvent>) this.DAO.findEntities(StoreSuccessEvent.class, 
				filter, params, new Long[] {dump.getId()}, null);
	}
	
	@Override
	public List<StoreSuccessEvent> getStoreSuccessEventByDump(Dump dump, 
			Locale locale) throws Exception {
		String filter = "dumpId == dumpIdParam && language == langParam";
		String params = "java.lang.Long dumpIdParam,java.lang.String langParam";
		return (List<StoreSuccessEvent>) this.DAO.findEntities(StoreSuccessEvent.class, 
				filter, params, new Object[] {dump.getId(),locale.getLanguage()}, null);
	}
	
	@Override
	public List<StoreSuccessEvent> getStoreSuccessEventByDump(Dump dump, int init, 
			int end, String ordering) throws Exception {
		String filter = "dumpId == dumpIdParam";
		String params = "java.lang.Long dumpIdParam";
		return (List<StoreSuccessEvent>) this.DAO.findEntitiesByRange(StoreSuccessEvent.class, 
				filter, params, new Long[] {dump.getId()}, null, init, end);
	}
	
	@Override
	public List<StoreSuccessEvent> getStoreSuccessEventByDump(Dump dump, int init, 
			int end, String ordering, Locale locale) throws Exception {
		String filter = "dumpId == dumpIdParam && language == langParam";
		String params = "java.lang.Long dumpIdParam,java.lang.String langParam";
		return (List<StoreSuccessEvent>) this.DAO.findEntities(StoreSuccessEvent.class, 
				filter, params, new Object[] {dump.getId(),locale.getLanguage()}, null);
	}
	
	@Override
	public void deleteStoreSuccessEventsByDump(Dump dump) throws Exception {
		this.DAO.delete(this.getStoreSuccessEventByDump(dump));
	}
	
	@Override
	public long countStoreSuccessEventsByDump(Dump dump) throws Exception {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("dumpId", dump.getId());
		return this.DAO.count(StoreSuccessEvent.class, filter);
	}
	
	
	@Override
	public long countStoreSuccessEventsByDump(Dump dump, 
			Locale locale) throws Exception {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("dumpId", dump.getId());
		filter.put("language", locale.getLanguage());
		return this.DAO.count(StoreSuccessEvent.class, filter);
	}
	
}