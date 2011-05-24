package com.m4f.business.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import com.m4f.business.domain.InternalUser;
import com.m4f.business.domain.MediationService;
import com.m4f.business.domain.Provider;
import com.m4f.business.service.ifc.I18nProviderService;
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
	public void deleteProvider(Provider provider, Locale locale)
			throws Exception {
		this.DAO.delete(provider, locale);
	}

	@Override
	public List<Provider> getAllProviders(Locale locale) throws Exception {
		return this.DAO.findAll(Provider.class, locale, null);
	}

	@Override
	public Provider getProviderById(Long id, Locale locale) throws Exception {
		return this.DAO.findById(Provider.class, locale, id);
	}

	@Override
	public void save(Provider provider, Locale locale) throws Exception {
		this.DAO.saveOrUpdate(provider, locale);
	}

	@Override
	public Collection<Provider> getAllProviders(Boolean mediator, Locale locale)
			throws Exception {
		if(mediator.booleanValue()) {
			return this.DAO.findEntities(Provider.class, locale, "mediationService != null", 
					"", new Object[] { }, null);
		} else {
			return this.DAO.findEntities(Provider.class, locale, "mediationService == null", 
					"", new Object[] { }, null);
		}
	}

	@Override
	public Provider findProviderByMediator(MediationService mediator, Locale locale) throws Exception {
		return this.DAO.findEntity(Provider.class, locale, "mediationService == mediationServiceParam", 
				"Long mediationServiceParam", new Object[] {mediator.getId()});
	}
	
	@Override
	public Provider getProviderByMediationService(Long mediationServiceId, Locale locale) throws Exception {
		return this.DAO.findEntity(Provider.class, locale, "mediationService == mediationParam", 
				"java.lang.Long mediationParam", new Object[]{mediationServiceId});
	}
	
	@Override
	public long count() throws Exception {
		return this.DAO.count(Provider.class);
	}

	@Override
	public Collection<Provider> getProviders(String ordering, int init, int end, Locale locale)
			throws Exception {
		return this.DAO.findEntitiesByRange(Provider.class, locale, init, end, ordering);
	}
	
	@Override
	public List<Long> getAllProviderIds() throws Exception {
		return this.DAO.getAllIds(Provider.class, null, null, null, null);
	}
}