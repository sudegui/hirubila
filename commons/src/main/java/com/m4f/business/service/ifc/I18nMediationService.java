package com.m4f.business.service.ifc;

import java.util.List;
import com.m4f.business.domain.MediationService;
import java.util.Collection;
import java.util.Locale;

public interface I18nMediationService {
	
	MediationService createMediationService();
	List<Long> getAllMediationServiceIds() throws Exception;
	List<Long> getAllMediationServiceIds(Boolean hasFeed) throws Exception;
	List<Long> getAllMediationServiceManualIds() throws Exception;
	List<MediationService> getAllMediationService(Locale locale) throws Exception;
	Collection<MediationService> getMediationServices(boolean manual, Locale locale) throws Exception;
	Collection<MediationService> getMediationServices(String ordering, int init, int end, Locale locale) throws Exception;
	MediationService getMediationService(Long id, Locale locale) throws Exception;
	MediationService getMediationServiceByUser(Long idUser, Locale locale) throws Exception;
	void save(MediationService mediator, Locale locale) throws Exception;
	void delete(MediationService mediator, Locale locale) throws Exception;
	long count() throws Exception;
	
	Collection<MediationService> getMediationServicesByProvince(Long provinceId, Locale locale) throws Exception;
}