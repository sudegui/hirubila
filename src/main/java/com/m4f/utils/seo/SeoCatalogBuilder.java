package com.m4f.utils.seo;

import java.util.List;
import java.util.Locale;
import com.m4f.business.service.exception.ContextNotActiveException;
import com.m4f.business.service.exception.ServiceNotFoundException;

public interface SeoCatalogBuilder {
	
	void buildSeoEntity(Long courseId, List<Locale> locales) throws ServiceNotFoundException, ContextNotActiveException, Exception;
}