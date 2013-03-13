package com.m4f.business.service.ifc;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import com.m4f.business.domain.MediationService;
import com.m4f.business.domain.Provider;

public interface I18nProviderService {
	
	Provider createProvider();
	List<Long> getAllProviderIds() throws Exception;
	List<Long> getProviderIdsByMediations(List<Long> mediationsIds) throws Exception;
	List<Provider> getAllProviders(Locale locale) throws Exception;
	void save(Provider provider, Locale locale) throws Exception;
	Provider getProviderById(Long id, Locale locale) throws Exception;
	Provider findProviderByMediator(MediationService mediator, Locale locale) throws Exception;
	void deleteProvider(Provider provider, Locale locale) throws Exception;
	Collection<Provider> getAllProviders(Boolean mediator, Locale locale) throws Exception;
	Provider getProviderByMediationService(Long mediationServiceId, Locale locale) throws Exception;
	long count() throws Exception;
	Collection<Provider> getProviders(String ordering, int init, int end, Locale locale) throws Exception;
}