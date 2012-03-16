package com.m4f.business.service.ifc;

import java.util.Locale;
import java.util.Map;
import com.m4f.business.domain.MediationService;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.extended.Province;
import com.m4f.business.domain.extended.Region;

public interface TransversalBusinessService {
		
	MediationService getMediationServiceByUser(String username);
	Provider getProviderByUserName(String userName, Locale locale) throws Exception;
	Map<String, Map<Long,Province>> getProvincesMap() throws Exception;
	Map<String, Map<Long,Region>> getRegionsMap() throws Exception;
	
}