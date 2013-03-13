package com.m4f.business.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import com.m4f.business.domain.InternalUser;
import com.m4f.business.domain.MediationService;
import com.m4f.business.domain.Provider;
import com.m4f.business.service.ifc.I18nMediationService;
import com.m4f.utils.cache.annotations.Cacheable;
import com.m4f.utils.cache.annotations.Cacheflush;
import com.m4f.utils.i18n.dao.ifc.I18nDAOSupport;

public class MediationServiceImpl extends I18nDAOBaseService implements I18nMediationService {

	private static final Logger LOGGER = Logger.getLogger(MediationServiceImpl.class.getName());
	
	public MediationServiceImpl(I18nDAOSupport dao) {
		super(dao);
	}
	
	@Override
	public MediationService createMediationService() {
		return this.DAO.createInstance(MediationService.class);
	}

	@Override
	@Cacheflush(cacheName="mediations")
	public void delete(MediationService mediator, Locale locale) throws Exception {
		mediator.setDeleted(Boolean.TRUE);
		this.DAO.saveOrUpdate(mediator, locale);		
	}
	
	@Override
	@Cacheflush(cacheName="mediations")
	public void save(MediationService mediator, Locale locale) throws Exception {
		this.DAO.saveOrUpdate(mediator, locale);
	}
	
	@Override
	@Cacheable(cacheName="mediations")
	public MediationService getMediationService(Long id, Locale locale) throws Exception {
		return this.DAO.findById(MediationService.class, locale, id);
	}
	
	@Override
	@Cacheable(cacheName="mediations")
	public List<MediationService> getAllMediationService(Locale locale) throws Exception {
		return  new ArrayList<MediationService>(this.DAO.findEntities(MediationService.class, locale, "deleted == deletedParam", 
				"Boolean deletedParam", new Object[] {Boolean.FALSE }, null)); 
		//return this.DAO.findAll(MediationService.class, locale, null);
	}
	
	@Override
	@Cacheable(cacheName="mediations")
	public Collection<MediationService> getMediationServices(boolean automatic, Locale locale) throws Exception {
		return this.DAO.findEntities(MediationService.class, locale, 
				"hasFeed == manualParam && deleted == deletedParam", "java.lang.Boolean manualParam, Boolean deletedParam", new Object[]{ new Boolean(automatic), Boolean.FALSE }, null);
	}

	@Override
	public MediationService getMediationServiceByUser(Long idUser, 
			Locale locale) throws Exception {
		return this.DAO.findEntity(MediationService.class, locale, 
				"members == userParam && deleted == deletedParam", "Long userParam, Boolean deletedParam", new Object[]{ idUser, Boolean.FALSE });
	}

	@Override
	@Cacheable(cacheName="mediations")
	public Collection<MediationService> getMediationServices(String ordering, int init,
			int end, Locale locale) throws Exception {
		return this.DAO.findEntitiesByRange(MediationService.class, locale, "deleted == deletedParam", 
				"Boolean deletedParam", new Object[] {Boolean.FALSE }, init, end, ordering);
	}

	@Override
	public long count() throws Exception {
		Map<String, Object> filter = new HashMap<String, Object>();
		
		filter.put("deleted", Boolean.FALSE);
		return this.DAO.count(MediationService.class, filter);
	}

	@Override
	@Cacheable(cacheName="mediations")
	public Collection<MediationService> getMediationServicesByProvince(Long provinceId, Locale locale) throws Exception {
		return this.DAO.findEntities(MediationService.class, locale, 
				"province == provinceParam && deleted == deletedParam", "Long provinceParam, Boolean deletedParam", new Object[]{ provinceId, Boolean.FALSE }, "name");
	}

	@Override
	public List<Long> getAllMediationServiceIds(Boolean hasFeed) throws Exception {
		return this.DAO.getAllIds(MediationService.class, "id",
				"hasFeed == hasParam && deleted == deletedParam", "java.lang.Boolean hasParam, Boolean deletedParam",  
				new Object[]{ hasFeed, Boolean.FALSE});
	}
	
	@Override
	public List<Long> getAllMediationServiceIds() throws Exception {
		return this.DAO.getAllIds(MediationService.class, null, "deleted == deletedParam", 
				"Boolean deletedParam", new Object[] {Boolean.FALSE });
	}
	
	@Override
	public List<Long> getAllMediationServiceManualIds() throws Exception {
		return this.DAO.getAllIds(MediationService.class, "id",
				"hasFeed == manualParam && deleted == deletedParam", "java.lang.Boolean manualParam, Boolean deletedParam",  
				new Object[]{ Boolean.FALSE,Boolean.FALSE});
	}
}