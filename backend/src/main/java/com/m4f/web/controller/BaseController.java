package com.m4f.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import com.m4f.business.service.ifc.I18nProviderService;
import com.m4f.business.service.ifc.I18nMediationService;
import com.m4f.utils.StackTraceUtil;
import com.m4f.utils.seo.SeoCatalogBuilder;
import com.m4f.utils.worker.WorkerFactory;
import com.m4f.business.service.ifc.IAppConfigurationService;
import com.m4f.business.service.ifc.I18nSchoolService;
import com.m4f.utils.feeds.parser.ifc.ISchoolsParser;
import com.m4f.utils.feeds.events.service.ifc.DumpService;
import com.m4f.utils.feeds.events.service.ifc.EventService;
import com.m4f.business.service.ifc.ICronTaskReportService;
import com.m4f.utils.feeds.parser.ifc.ICoursesParser;
import com.m4f.business.service.ifc.I18nMediationService;
import com.m4f.business.service.extended.ifc.I18nExtendedCourseService;
import com.m4f.business.service.extended.ifc.I18nExtendedSchoolService;
import com.m4f.business.service.extended.ifc.I18nInternalFeedService;
import com.m4f.business.service.ifc.I18nCourseService;
import com.m4f.business.service.ifc.ICatalogService;
import com.m4f.business.service.ifc.I18nInboxService;
import com.m4f.utils.feeds.parser.ifc.DumperCapable;

@SessionAttributes(value={"langs"})
public abstract class BaseController {
	
	private static final Logger LOGGER = Logger.getLogger(BaseController.class.getName());
	protected static List<String> connectedClients = new ArrayList<String>();
	
	@Autowired
	private ReloadableResourceBundleMessageSource messageSource;
	/*@Autowired
	protected IServiceLocator serviceLocator;*/
	
	@Autowired
	protected DumperCapable dumperManager;
	@Autowired
	protected I18nInboxService inboxService;
	@Autowired
	protected ICatalogService catalogService;
	@Autowired
	protected I18nCourseService courseService;
	@Autowired
	protected I18nExtendedCourseService extendedCourseService;
	@Autowired
	protected I18nExtendedSchoolService extendedSchoolService;
	@Autowired
	protected I18nInternalFeedService internalFeedService;
	@Autowired
	protected I18nMediationService mediatorService;
	@Autowired
	protected ICoursesParser coursesParser;
	@Autowired
	protected ICronTaskReportService cronTaskReportService;
	@Autowired
	protected DumpService dumpService;
	@Autowired
	protected EventService eventService;
	@Autowired
	protected ISchoolsParser schoolsParser;
	@Autowired
	protected IAppConfigurationService configurationService;
	@Autowired
	protected I18nSchoolService schoolService;
	@Autowired
	protected WorkerFactory workerFactory;
	@Autowired
	protected I18nProviderService providerService;
	@Autowired
	protected I18nMediationService mediationService;
	@Autowired
	protected Validator validator;
	@Autowired
	protected SeoCatalogBuilder catalogBuilder;
	
	protected String getMessage(String msg, Object... args) {
		List<Object> variableParts = new ArrayList<Object>();
		if(args != null) {
			for(Object arg : args) {
				variableParts.add(arg);
			}
		}
		String message = "????" + msg + "????";
		try {
			message = this.messageSource.getMessage(msg,
					variableParts.toArray(), null);
		} catch(Exception e) {
			LOGGER.info(StackTraceUtil.getStackTrace(e));
		}
		return message;
	}
	
	protected boolean isSupportedMimeType(List<String> supportedMimeTypes, 
			String targetMimeType) {
		for(String mimeType : supportedMimeTypes) {
			if(mimeType.equals(targetMimeType)) {
				return true;
			}
		}
		return false;
	}
	
	
	protected List<Locale> getAvailableLanguages() {
		try {
			return configurationService.getLocales();
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return new ArrayList<Locale>();
		}
		
	}
	
	@ModelAttribute("langs")
	public Map<String, Locale> getAvailableMapLanguages() {
		try {
			return configurationService.getLocalesMap();
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return new HashMap<String, Locale>();
		}
	}
	
	
	public void setPageSize(Integer pageSize) {
		try {
			configurationService.getGlobalConfiguration().setPageSize(pageSize);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
		}
	}

	public Integer getPageSize() {
		try {
			return configurationService.getGlobalConfiguration().getPageSize();
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			LOGGER.severe("Returning pageSize value: 10");
			return new Integer(10);
		}
	}
		
	
}