package com.m4f.business.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.m4f.utils.i18n.dao.ifc.I18nDAOSupport;

public abstract class I18nDAOBaseService {
	
	protected I18nDAOSupport DAO;
	
	public I18nDAOBaseService(I18nDAOSupport dao) {
		this.DAO = dao;
	}
	
	protected Object[] valuesArray(HashMap<String, Object> values) {
		ArrayList list = new ArrayList();
		
		if(values != null) {
			Iterator<String> it = values.keySet().iterator();
			while(it.hasNext()) {
				String key = it.next();
				list.add(values.get(key));
			}
		}
		
		return list.toArray();
	}

}