package com.m4f.business.service.impl;

import com.m4f.utils.i18n.dao.ifc.DAOSupport;

public abstract class DAOBaseService {
	protected DAOSupport DAO;
	
	public DAOBaseService(DAOSupport dao) {
		this.DAO = dao;
	}
	
	
}
