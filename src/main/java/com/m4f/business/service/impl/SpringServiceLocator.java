package com.m4f.business.service.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.m4f.business.domain.InternalUser;
import com.m4f.business.domain.MediationService;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.extended.Province;
import com.m4f.business.domain.extended.Region;
import com.m4f.business.service.ifc.I18nCourseService;
import com.m4f.business.service.ifc.I18nInboxService;
import com.m4f.business.service.ifc.I18nMediationService;
import com.m4f.business.service.ifc.I18nProviderService;
import com.m4f.business.service.ifc.I18nSchoolService;
import com.m4f.business.service.ifc.I18nTerritorialService;
import com.m4f.business.service.ifc.IAppConfigurationService;
import com.m4f.business.service.ifc.ICatalogService;
import com.m4f.business.service.ifc.ICronTaskReportService;
import com.m4f.business.service.ifc.IPhraseSearchService;
import com.m4f.business.service.ifc.IServiceLocator;
import com.m4f.business.service.ifc.TransversalBusinessService;
import com.m4f.business.service.ifc.UserService;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import com.m4f.business.service.exception.ContextNotActiveException;
import com.m4f.business.service.exception.ServiceNotFoundException;
import com.m4f.business.service.extended.ifc.I18nExtendedCourseService;
import com.m4f.business.service.extended.ifc.I18nExtendedSchoolService;
import com.m4f.business.service.extended.ifc.I18nInternalFeedService;
import com.m4f.utils.StackTraceUtil;
import com.m4f.utils.feeds.events.service.ifc.DumpService;
import com.m4f.utils.feeds.events.service.ifc.EventService;
import com.m4f.utils.feeds.parser.ifc.DumperCapable;
import com.m4f.utils.feeds.parser.ifc.ICoursesParser;
import com.m4f.utils.feeds.parser.ifc.ISchoolsParser;
import com.m4f.utils.i18n.service.ifc.I18nService;
import com.m4f.utils.search.ifc.ISearchEngine;

public class SpringServiceLocator implements IServiceLocator, ApplicationContextAware, TransversalBusinessService {
	
	private static final Logger LOGGER = Logger.getLogger(SpringServiceLocator.class.getName());
	private ApplicationContext ctx;
	private ClassPathXmlApplicationContext servicesCtx;
	
	@Override
	public void setApplicationContext(ApplicationContext appCtx)
			throws BeansException {
		this.ctx = appCtx;
		this.servicesCtx = (ClassPathXmlApplicationContext) this.ctx.getBean("servicesContext");
	}

	@Override
	public I18nSchoolService getSchoolService() throws ServiceNotFoundException, ContextNotActiveException {
		return this.getService(I18nSchoolService.class);
	}

	@Override
	public I18nCourseService getCourseService() throws ServiceNotFoundException, ContextNotActiveException {
		return this.getService(I18nCourseService.class);
	}
	
	@Override
	public UserService getUserService() throws ServiceNotFoundException, ContextNotActiveException {
		return this.getService(UserService.class);
	}
	
	@Override
	public I18nExtendedCourseService getExtendedCourseService() throws ServiceNotFoundException, ContextNotActiveException {
		return this.getService(I18nExtendedCourseService.class);
	}
	
	@Override
	public I18nExtendedSchoolService getExtendedSchoolService() throws ServiceNotFoundException, ContextNotActiveException {
		return this.getService(I18nExtendedSchoolService.class);
	}
	
	@Override
	public I18nInboxService getInboxService() throws ServiceNotFoundException, ContextNotActiveException {
		return this.getService(I18nInboxService.class);
	}
	
	@Override
	public I18nMediationService getMediatorService() throws ServiceNotFoundException, ContextNotActiveException {
		return this.getService(I18nMediationService.class);
	}
	
	@Override
	public I18nProviderService getProviderService() throws ServiceNotFoundException, ContextNotActiveException {
		return this.getService(I18nProviderService.class);
	}
	
	@Override
	public I18nTerritorialService getTerritorialService() throws ServiceNotFoundException, ContextNotActiveException {
		return this.getService(I18nTerritorialService.class);
	}
	
	@Override
	public DumpService getDumpService() throws ServiceNotFoundException, ContextNotActiveException {
		return this.getService(DumpService.class);
	}
	
	@Override
	public EventService getEventService() throws ServiceNotFoundException, ContextNotActiveException {
		return this.getService(EventService.class);
	}
	
	@Override
	public I18nInternalFeedService getInternalFeedService() throws ServiceNotFoundException, ContextNotActiveException {
		return this.getService(I18nInternalFeedService.class);
	}
	
	@Override
	public I18nService getI18nService() throws ServiceNotFoundException, ContextNotActiveException {
		return this.getService(I18nService.class);
	}
	
	@Override
	public ISearchEngine getSearchEngine() throws ServiceNotFoundException, ContextNotActiveException {
		return this.getService(ISearchEngine.class);
	}
	
	@Override
	public IPhraseSearchService getPhraseSearchService() throws ServiceNotFoundException, ContextNotActiveException {
		return this.getService(IPhraseSearchService.class);
	}
	
	@Override
	public ISchoolsParser getSchoolsParser() throws ServiceNotFoundException, ContextNotActiveException {
		return this.getService(ISchoolsParser.class);
	}
	
	@Override
	public ICoursesParser getCoursesParser() throws ServiceNotFoundException, ContextNotActiveException {
		return this.getService(ICoursesParser.class);
	}
	
	@Override
	public DumperCapable getDumperManager() throws ServiceNotFoundException, ContextNotActiveException {
		return this.getService(DumperCapable.class);
	}
	
	@Override
	public ICatalogService getCatalogService() throws ServiceNotFoundException, ContextNotActiveException {
		return this.getService(ICatalogService.class);
	}
	
	@Override
	public IAppConfigurationService getAppConfigurationService() throws ServiceNotFoundException, ContextNotActiveException {
		return this.getService(IAppConfigurationService.class);
	}
	
	@Override
	public ICronTaskReportService getCronTaskReportService() throws ServiceNotFoundException, ContextNotActiveException {
		return this.getService(ICronTaskReportService.class);
	}
	
	@Override
	public TransversalBusinessService getTransversalService()
			throws ServiceNotFoundException, ContextNotActiveException {
		return this;
	}
	
	public void init() {
		long init = Calendar.getInstance().getTimeInMillis();
		LOGGER.severe("---Starting init Service Locator Bean " );
		this.servicesCtx.setConfigLocation("services-config.xml");
		this.servicesCtx.stop();
		this.servicesCtx.refresh();
		this.servicesCtx.start();
		long end = Calendar.getInstance().getTimeInMillis();
		LOGGER.severe("---Ending init Service Locator Bean, elapsed time " + 
				((end-init)/1000) );
    }
	
	private <T extends Object> T getService(Class<T> clazz) 
		throws ServiceNotFoundException, ContextNotActiveException {
		T bean = null;
		try {
			if(!this.servicesCtx.isRunning()) {
				throw new ContextNotActiveException("#### Context is not RUNNING....");
			}
			if(!this.servicesCtx.isActive()) {
				throw new ContextNotActiveException("#### Context is not ACTIVE....");
			}
			bean = this.servicesCtx.getBean(clazz);
		} catch(NoSuchBeanDefinitionException ex) {
			this.servicesCtx.setConfigLocation("services-config.xml");
			this.servicesCtx.stop();
			this.servicesCtx.refresh();
			this.servicesCtx.start();
			bean = this.servicesCtx.getBean(clazz);
		}
		return bean;
	}

	@Override
	public MediationService getMediationServiceByUser(String username) {
		try {
			InternalUser intUser = this.getUserService().getUser(username);
			return this.getMediatorService().getMediationServiceByUser(intUser.getId(), null);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return null;
		}
	}

	@Override
	public Provider getProviderByUserName(String userName, Locale locale)
			throws Exception {
		MediationService mediator = 
			this.getMediatorService().getMediationServiceByUser(
					this.getUserService().getUser(userName).getId(), 
					locale);
		if(mediator == null) {
			throw new Exception("The user " + userName + 
					" doesn't have a mediator assigned.");
		}
		Provider provider = this.getProviderService().findProviderByMediator(mediator, null);
		if(provider == null) {
			throw new Exception("Mediator with id " + mediator.getId() + 
					" hasn't provider.");
		}
		return provider;
	}

	@Override
	public Map<String, Map<Long, Province>> getProvincesMap() throws Exception {
		Map<String, Map<Long,Province>> provincesMap = new HashMap<String, Map<Long,Province>>();	
		for(Locale locale : this.getAppConfigurationService().getLocales()) {
			provincesMap.put(locale.getLanguage(), this.getTerritorialService().getProvincesMap(locale));
		}
		return provincesMap;
	}

	@Override
	public Map<String, Map<Long, Region>> getRegionsMap() throws Exception {
		Map<String, Map<Long,Region>> regionsMap = new HashMap<String, Map<Long,Region>>();
		for(Locale locale : this.getAppConfigurationService().getLocales()) {
			regionsMap.put(locale.getLanguage(), this.getTerritorialService().getRegionsMap(locale));
		}
		return regionsMap;
	}

}