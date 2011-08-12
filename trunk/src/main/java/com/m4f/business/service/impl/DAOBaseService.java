package com.m4f.business.service.impl;

import com.m4f.utils.dao.ifc.DAOSupport;

public abstract class DAOBaseService {
	protected DAOSupport DAO;
	
	public DAOBaseService(DAOSupport dao) {
		this.DAO = dao;
	}
	
	
}
