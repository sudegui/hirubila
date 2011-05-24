package com.m4f.utils.feeds.events.service.impl;

import com.m4f.utils.i18n.dao.ifc.DAOSupport;

public class JdoBaseService {
	
	protected DAOSupport DAO;
	
	public JdoBaseService(DAOSupport dao) {
		this.DAO = dao;
	}
}
