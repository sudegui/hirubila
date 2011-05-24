package com.m4f.utils.feeds.events.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.m4f.business.domain.School;
import com.m4f.utils.feeds.events.service.ifc.EventService;
import com.m4f.utils.feeds.events.model.Dump;
import com.m4f.utils.feeds.events.service.ifc.DumpService;
import com.m4f.utils.i18n.dao.ifc.DAOSupport;

public class JdoDumpServiceImpl extends JdoBaseService implements DumpService {
	
	private EventService eventService;
	
	public JdoDumpServiceImpl(DAOSupport dao, EventService eService) {
		super(dao);
		this.eventService = eService;
	}
	
	@Override
	public Dump createDump() {
		return (Dump) this.DAO.createInstance(Dump.class);
	}

	@Override
	public void delete(Dump d) throws Exception {
		this.DAO.delete(d);
		this.eventService.deleteAllEventsByDump(d);
	}

	@Override
	public void deleteAllDumps() throws Exception {
		this.DAO.delete(this.getAllDumps());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Dump> getAllDumps() throws Exception {
		return this.DAO.findAll(Dump.class);
	}
	
	@Override
	public List<Dump> getDumpsByOwner(Long ownerId, int init, 
			int end, String ordering) {
		List<Dump> dumps = new ArrayList<Dump>();
		String filter = "owner == ownerParam";
		String params = "java.lang.Long ownerParam";
		dumps.addAll(this.DAO.findEntitiesByRange(Dump.class, filter, 
				params, new Long[] {ownerId}, ordering, init, end));
		return dumps;
	}
	
	@Override
	public Dump getLastDumpByOwner(Long ownerId) throws Exception{
		List<Dump> dumps = this.getDumpsByOwner(ownerId, 0, 1, "-launched");
		if(dumps != null && dumps.size() > 0) return dumps.get(0);
		return null;
	}
	
	@Override
	public List<Dump> getDumpsByOwner(Long ownerId, int init, int end, Date start, Date finish, String ordering) throws Exception {
		ArrayList<Dump> dumps = new ArrayList<Dump>();
		StringBuffer filter = new StringBuffer();
		StringBuffer params = new StringBuffer();
		if(start != null) {
			filter.append("launched >= startParam");
			params.append("java.util.Date startParam");
		}
		if(finish != null) {
			if(!("").equals(filter.toString())) {
				filter.append(" && ");
				filter.append(", ");
			}
			filter.append("launched <= finishParam");
			params.append("java.util.Date finishParam");
		}
		if(!"".equals(filter.toString())) {
			dumps.addAll(this.DAO.findEntitiesByRange(Dump.class, filter.toString(), 
					params.toString(), new Long[] {ownerId}, null, init, end));
		} else {
			dumps.addAll(this.getDumpsByOwner(ownerId, init, end, ordering));
		}
		
		return dumps;
	}
	
	@Override
	public Dump getDump(Long id) throws Exception {
		return (Dump) this.DAO.findById(Dump.class, id);
	}

	@Override
	public void save(Dump d) throws Exception {
		this.DAO.saveOrUpdate(d);
	}
	
	@Override
	public Long countDumps() {
		return this.DAO.count(Dump.class);
	}
	
	@Override
	public Long countDumpsByOwner(Long ownerId) {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("owner", ownerId);
		return this.DAO.count(Dump.class, filter);
	}
	
		
}