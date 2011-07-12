package com.m4f.business.service.ifc;

import com.m4f.business.service.exception.ContextNotActiveException;
import com.m4f.business.service.exception.ServiceNotFoundException;
import com.m4f.business.service.extended.ifc.I18nExtendedCourseService;
import com.m4f.business.service.extended.ifc.I18nExtendedSchoolService;
import com.m4f.business.service.ifc.I18nCourseService;
import com.m4f.business.service.ifc.I18nInboxService;
import com.m4f.business.service.ifc.I18nMediationService;
import com.m4f.business.service.ifc.I18nProviderService;
import com.m4f.business.service.ifc.I18nSchoolService;
import com.m4f.business.service.ifc.I18nTerritorialService;
import com.m4f.business.service.ifc.UserService;
import com.m4f.utils.feeds.events.service.ifc.DumpService;
import com.m4f.utils.feeds.events.service.ifc.EventService;
import com.m4f.business.service.extended.ifc.I18nInternalFeedService;
import com.m4f.utils.i18n.service.ifc.I18nService;
import com.m4f.utils.search.ifc.ISearchEngine;
import com.m4f.business.service.ifc.IPhraseSearchService;
import com.m4f.utils.feeds.parser.ifc.ISchoolsParser;
import com.m4f.utils.feeds.parser.ifc.ICoursesParser;
import com.m4f.utils.feeds.parser.ifc.DumperCapable;

public interface IServiceLocator {
	
	void init();
	I18nSchoolService getSchoolService() throws ServiceNotFoundException, ContextNotActiveException;
	I18nCourseService getCourseService() throws ServiceNotFoundException, ContextNotActiveException;
	UserService getUserService() throws ServiceNotFoundException, ContextNotActiveException;
	I18nExtendedCourseService getExtendedCourseService() throws ServiceNotFoundException, ContextNotActiveException;
	I18nExtendedSchoolService getExtendedSchoolService() throws ServiceNotFoundException, ContextNotActiveException;
	I18nInboxService getInboxService() throws ServiceNotFoundException, ContextNotActiveException;
	I18nMediationService getMediatorService() throws ServiceNotFoundException, ContextNotActiveException;
	I18nProviderService getProviderService() throws ServiceNotFoundException, ContextNotActiveException;
	I18nTerritorialService getTerritorialService() throws ServiceNotFoundException, ContextNotActiveException;
	DumpService getDumpService() throws ServiceNotFoundException, ContextNotActiveException;
	EventService getEventService() throws ServiceNotFoundException, ContextNotActiveException;
	I18nInternalFeedService getInternalFeedService() throws ServiceNotFoundException, ContextNotActiveException;
	I18nService getI18nService() throws ServiceNotFoundException, ContextNotActiveException;
	ISearchEngine getSearchEngine() throws ServiceNotFoundException, ContextNotActiveException;
	IPhraseSearchService getPhraseSearchService() throws ServiceNotFoundException, ContextNotActiveException;
	ISchoolsParser getSchoolsParser() throws ServiceNotFoundException, ContextNotActiveException;
	ICoursesParser getCoursesParser() throws ServiceNotFoundException, ContextNotActiveException;
	DumperCapable getDumperManager() throws ServiceNotFoundException, ContextNotActiveException;
	ICatalogService getCatalogService() throws ServiceNotFoundException, ContextNotActiveException;
	IAppConfigurationService getAppConfigurationService() throws ServiceNotFoundException, ContextNotActiveException;
	ICronTaskReportService getCronTaskReportService() throws ServiceNotFoundException, ContextNotActiveException;
	TransversalBusinessService getTransversalService() throws ServiceNotFoundException, ContextNotActiveException;
	
}