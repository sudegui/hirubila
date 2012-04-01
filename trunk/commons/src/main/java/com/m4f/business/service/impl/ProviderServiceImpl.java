package com.m4f.business.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.m4f.business.domain.Course;
import com.m4f.business.domain.InternalUser;
import com.m4f.business.domain.MediationService;
import com.m4f.business.domain.Provider;
import com.m4f.business.service.ifc.I18nProviderService;
import com.m4f.utils.cache.annotations.Cacheable;
import com.m4f.utils.cache.annotations.Cacheflush;
import com.m4f.utils.i18n.dao.ifc.I18nDAOSupport;

public class ProviderServiceImpl extends I18nDAOBaseService implements I18nProviderService {

	public ProviderServiceImpl(I18nDAOSupport dao) {
		super(dao);
	}

	@Override
	public Provider createProvider() {
		return this.DAO.createInstance(Provider.class);
	}

	@Override
	@Cacheflush(cacheName="courses")
	public void deleteProvider(Provider provider, Locale locale)
			throws Exception {
		provider.setDeleted(Boolean.TRUE);
		this.DAO.saveOrUpdate(provider, locale);
	}

	@Override
	@Cacheable(cacheName="providers")
	public List<Provider> getAllProviders(Locale locale) throws Exception {
		return new ArrayList<Provider>(this.DAO.findEntities(Provider.class, locale, "deleted == deletedParam", 
				"Boolean deletedParam", new Object[] {Boolean.FALSE }, null));
	}

	@Override
	@Cacheable(cacheName="providers")
	public Provider getProviderById(Long id, Locale locale) throws Exception {
		return this.DAO.findById(Provider.class, locale, id);
	}

	@Override
	@Cacheflush(cacheName="courses")
	public void save(Provider provider, Locale locale) throws Exception {
		this.DAO.saveOrUpdate(provider, locale);
	}

	@Override
	@Cacheable(cacheName="providers")
	public Collection<Provider> getAllProviders(Boolean mediator, Locale locale)
			throws Exception {
		if(mediator.booleanValue()) {
			return this.DAO.findEntities(Provider.class, locale, "mediationService != null && deleted == deletedParam", 
					"Boolean deletedParam", new Object[] {Boolean.FALSE }, null);
		} else {
			return this.DAO.findEntities(Provider.class, locale, "mediationService == null", 
					"Boolean deletedParam", new Object[] {Boolean.FALSE }, null);
		}
	}

	@Override
	@Cacheable(cacheName="providers")
	public Provider findProviderByMediator(MediationService mediator, Locale locale) throws Exception {
		return this.DAO.findEntity(Provider.class, locale, "mediationService == mediationServiceParam && deleted == deletedParam", 
				"Long mediationServiceParam, Boolean deletedParam", new Object[] {mediator.getId(), Boolean.FALSE});
	}
	
	@Override
	@Cacheable(cacheName="providers")
	public Provider getProviderByMediationService(Long mediationServiceId, Locale locale) throws Exception {
		return this.DAO.findEntity(Provider.class, locale, "mediationService == mediationParam && deleted == deletedParam", 
				"java.lang.Long mediationParam, Boolean deletedParam", new Object[]{mediationServiceId, Boolean.FALSE});
	}
	
	@Override
	@Cacheable(cacheName="providers")
	public long count() throws Exception {
		Map<String, Object> filter = new HashMap<String, Object>();
		
		filter.put("deleted", Boolean.FALSE);
		return this.DAO.count(Provider.class, filter);
	}

	@Override
	@Cacheable(cacheName="providers")
	public Collection<Provider> getProviders(String ordering, int init, int end, Locale locale)
			throws Exception {
		return this.DAO.findEntitiesByRange(Provider.class, locale, "deleted == deletedParam", "Boolean deletedParam",  new Object[]{Boolean.FALSE}, init, end, ordering);
	}
	
	@Override
	@Cacheable(cacheName="providers")
	public List<Long> getAllProviderIds() throws Exception {
		return this.DAO.getAllIds(Provider.class, "id", "deleted == deletedParam", "Boolean deletedParam",  new Object[]{Boolean.FALSE});
	}
}