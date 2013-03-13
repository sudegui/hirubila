package com.m4f.business.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import com.m4f.business.domain.Inbox;
import com.m4f.business.domain.ResultSearchEmail;
import com.m4f.business.domain.Inbox.ORIGIN;
import com.m4f.business.domain.Inbox.TYPE;
import com.m4f.business.domain.Inbox.USER;
import com.m4f.business.service.ifc.I18nInboxService;
import com.m4f.utils.i18n.dao.ifc.I18nDAOSupport;

public class InboxServiceImpl extends I18nDAOBaseService implements I18nInboxService {
	private static final Logger LOGGER = Logger.getLogger(InboxServiceImpl.class.getName());
	
	public InboxServiceImpl(I18nDAOSupport dao) {
		super(dao);
	}
	
	@Override
	public Inbox createInbox() {
		return (Inbox) this.DAO.createInstance(Inbox.class);
	}

	@Override
	public void delete(Inbox inbox, Locale locale) throws Exception {
		inbox.setActive(Boolean.FALSE);
		this.DAO.saveOrUpdate(inbox, locale);
	}

	@Override
	public List<Inbox> getAllInbox(Locale locale) throws Exception {
		return this.DAO.findAll(Inbox.class, locale, null);
	}
	
	@Override
	public Long count(Inbox.ORIGIN origin) throws Exception {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("origin", origin.getValue());
		return this.DAO.count(Inbox.class, filter);
	}
	
	@Override
	public Inbox getInbox(Long id, Locale locale) throws Exception {
		return this.DAO.findById(Inbox.class, locale, id);
	}

	@Override
	public void save(Inbox inbox, Locale locale) throws Exception {
		this.DAO.saveOrUpdate(inbox, locale);
	}
	
	@Override
	public Collection<Inbox> getAllInbox(Boolean active, Boolean readed, TYPE type, ORIGIN origin, USER user,
			Locale locale,int init,	int end, String ordering) throws Exception {
		StringBuffer filter = new StringBuffer();
		//HashMap<String, Object> values = new HashMap<String, Object>();
		List values = new ArrayList();
		StringBuffer params = new StringBuffer();
		
		if(type != null) {
			filter.append("type == typeParam");
			//values.put("typeParam", type.getValue());
			values.add(type.getValue());
			params.append("java.lang.String typeParam");
		}
		
		if(origin != null) {
			if(filter.length() != 0) { 
				filter.append(" && ");
				params.append(",");
			}
			filter.append("origin == originParam");
			//values.put("originParam", origin.getValue());
			values.add(origin.getValue());
			params.append("java.lang.String originParam");
		}
		
		if(user != null) {
			if(filter.length() != 0) { 
				filter.append(" && ");
				params.append(",");
			}
			filter.append("user == userParam");
			//values.put("userParam", user.getValue());
			values.add(user.getValue());
			params.append("java.lang.String userParam");
		}
		
		if(active != null) {
			if(filter.length() != 0) { 
				filter.append(" && ");
				params.append(",");
			}
			filter.append("active == activeParam");
			//values.put("activeParam", active);
			values.add(active);
			params.append("java.lang.Boolean activeParam");
		}
				
		if(readed != null) {
			if(filter.length() != 0) {
				filter.append(" && ");
				params.append(",");
			}
			filter.append("readed == readedParam");
			//values.put("readedParam", readed);
			values.add(readed);
			params.append("java.lang.Boolean readedParam");
		}

		return this.DAO.findEntitiesByRange(Inbox.class, locale, filter.toString(), params.toString(), values.toArray(), init, end, ordering);	
	}

	@Override
	public Long count(Boolean active, Boolean readed, TYPE type, ORIGIN origin, USER user) throws Exception {
		HashMap<String, Object> filter = new HashMap<String, Object>();
		
		if(type != null) {
			filter.put("type", type.getValue());
		}
		
		if(origin != null) {
			filter.put("origin", origin.getValue());
		}
		
		if(user != null) {
			filter.put("user", user.getValue());
		}
		
		if(active != null) {
			filter.put("active", active);
		}
				
		if(readed != null) {
			filter.put("readed", readed);
		}
		return this.DAO.count(Inbox.class, filter);
	}
	
	@Override
	public ResultSearchEmail createResultSearchEmail() {
		return this.DAO.createInstance(ResultSearchEmail.class);
	}

	@Override
	public ResultSearchEmail getResultSearchEmail(Long id, Locale locale)
			throws Exception {
		return this.DAO.findById(ResultSearchEmail.class, locale, id);
	}

	@Override
	public void save(ResultSearchEmail resultSearchEmail, Locale locale) throws Exception {
		this.DAO.saveOrUpdate(resultSearchEmail, locale);
	}
}
