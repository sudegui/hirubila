package com.m4f.business.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.m4f.business.domain.InternalUser;
import com.m4f.business.domain.MediationService;
import com.m4f.business.service.ifc.UserService;
import com.m4f.utils.dao.ifc.DAOSupport;

public class UserServiceImpl extends DAOBaseService implements UserService {
	
	private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class.getName());
	
	public UserServiceImpl(DAOSupport dao) {
		super(dao);
	}

	@Override
	public InternalUser createUser() {
		return (InternalUser) this.DAO.createInstance(InternalUser.class);
	}

	@Override
	public void delete(InternalUser user) throws Exception {
		user.setDeleted(Boolean.TRUE);
		this.DAO.saveOrUpdate(user);		
	}

	@Override
	public List<InternalUser> getAllUser() throws Exception {
		//return this.DAO.findAll(InternalUser.class, null);
		return  new ArrayList<InternalUser>(this.DAO.findEntities(InternalUser.class, "deleted == deletedParam", 
				"Boolean deletedParam", new Object[] {Boolean.FALSE }, null)); 
	}

	@Override
	public InternalUser getUser(Long id) throws Exception {
		return (InternalUser) this.DAO.findById(InternalUser.class, id);
	}

	@Override
	public InternalUser getUser(String email) throws Exception {
		return this.DAO.findEntity(InternalUser.class, "email == emailParam && deleted == deletedParam", 
				"String emailParam, Boolean deletedParam", new Object[]{email, Boolean.FALSE});
	}

	@Override
	public void save(InternalUser user) throws Exception {
		this.DAO.saveOrUpdate(user);
	}

	
	@Override
	public Collection<InternalUser> findUsersByMediationService(MediationService mediationService) 
		throws Exception {
		return this.DAO.findCollectionById(InternalUser.class, mediationService.getMembers());
	}

	@Override
	public long countUsers() throws Exception {
		Map<String, Object> filter = new HashMap<String, Object>();
		
		filter.put("deleted", Boolean.FALSE);
		return this.DAO.count(InternalUser.class, filter);
	}

	@Override
	public Collection<InternalUser> getUsersByRange(String ordering, int init, int end)
			throws Exception {
		return this.DAO.findEntitiesByRange(InternalUser.class, "deleted == deletedParam", 
				"Boolean deletedParam", new Object[] {Boolean.FALSE }, ordering, init, end);
	}
	
}