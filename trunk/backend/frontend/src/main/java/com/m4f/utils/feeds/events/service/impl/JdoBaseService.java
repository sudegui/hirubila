package com.m4f.utils.feeds.events.service.impl;

import com.m4f.utils.dao.ifc.DAOSupport;

public class JdoBaseService {
	
	protected DAOSupport DAO;
	
	public JdoBaseService(DAOSupport dao) {
		this.DAO = dao;
	}
}
