package com.m4f.web.controller;


import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.m4f.business.domain.InternalUser;
import com.m4f.business.domain.MediationService;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.extended.Province;
import com.m4f.business.domain.extended.Region;
import com.m4f.business.service.ifc.IServiceLocator;
import com.m4f.utils.StackTraceUtil;
import com.m4f.web.controller.helper.ViewHelper;

@SessionAttributes(value={"provincesMap","langs","regionsMap"})
public abstract class BaseController {
	
	protected final String PROVIDER_QUEUE = "provider";
	protected final String SCHOOL_QUEUE = "school";
	//protected final String BATCH_QUEUE = "batch";
	
	
	private static final Logger LOGGER = Logger.getLogger(BaseController.class.getName());
	private Map<String,Locale> localesMap;
	private List<Locale> avLocales;
	
	@Autowired
	private ReloadableResourceBundleMessageSource messageSource;
	@Autowired
	protected IServiceLocator serviceLocator;
	@Autowired
	protected ApplicationContext ctx;
	@Autowired
	protected Validator validator;
	@Autowired
	protected ViewHelper viewHelper;
	
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
			if(this.avLocales == null) {
				this.avLocales = new ArrayList<Locale>();
				String[] languages = this.serviceLocator.getAppConfigurationService().getGlobalConfiguration().getLanguages().split("[,]");
				for(String language : languages) {
					Locale locale = new Locale(language);
					if(locale!=null) this.avLocales.add(new Locale(language));
				}
			}
			return this.avLocales;
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
			this.avLocales.add(Locale.getDefault());
			return this.avLocales;
		}
		
	}
	
	@ModelAttribute("langs")
	public Map<String, Locale> getAvailableMapLanguages() {
		try {
			if(this.avLocales == null) {
				this.localesMap = new HashMap<String,Locale>();
				String[] languages = this.serviceLocator.getAppConfigurationService().getGlobalConfiguration().getLanguages().split("[,]");
				this.avLocales = new ArrayList<Locale>();
				for(String language : languages) {
					Locale locale = new Locale(language);
					if(locale!=null) {
						this.avLocales.add(new Locale(language));
						this.localesMap.put(language, locale);
					}
				}
			}
			return this.localesMap;
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
			this.localesMap.put(Locale.getDefault().getLanguage(), Locale.getDefault());
			return this.localesMap;
		}
	}
	
	//@ModelAttribute("provincesMap")
	public Map<String, Map<Long,Province>> getProvincesMap() throws Exception {
		Map<String, Map<Long,Province>> provincesMap = 
			new HashMap<String, Map<Long,Province>>();
		try {
			for(Locale locale : this.getAvailableLanguages()) {
				provincesMap.put(locale.getLanguage(), 
						this.serviceLocator.getTerritorialService().getProvincesMap(locale));
			}
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
		}
		return provincesMap;
	}
	
	//@ModelAttribute("regionsMap")
	public Map<String, Map<Long,Region>> getRegionsMap() throws Exception {
		Map<String, Map<Long,Region>> regionsMap = 
			new HashMap<String, Map<Long,Region>>();
		try {
			for(Locale locale : this.getAvailableLanguages()) {
				regionsMap.put(locale.getLanguage(), 
						this.serviceLocator.getTerritorialService().getRegionsMap(locale));
			}
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
		}
		return regionsMap;
	}
	public void setPageSize(Integer pageSize) {
		try {
			this.serviceLocator.getAppConfigurationService().getGlobalConfiguration().setPageSize(pageSize);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
		}
	}

	public Integer getPageSize() {
		try {
			return this.serviceLocator.getAppConfigurationService().getGlobalConfiguration().getPageSize();
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			LOGGER.severe("Returning pageSize value: 10");
			return new Integer(10);
		}
	}
	
	protected MediationService getMediationService(Principal user) {
		try {
			InternalUser intUser = this.serviceLocator.getUserService().getUser(user.getName());
			return this.serviceLocator.getMediatorService().
				getMediationServiceByUser(intUser.getId(), Locale.getDefault());
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return null;
		}
	}
	
	protected Provider getProviderByUserName(String userName, 
			Locale locale) throws Exception {
		MediationService mediator = 
			this.serviceLocator.getMediatorService().getMediationServiceByUser(
					this.serviceLocator.getUserService().getUser(userName).getId(), 
					locale);
		if(mediator == null) {
			throw new Exception("The user " + userName + 
					" doesn't have a mediator assigned.");
		}
		Provider provider = this.serviceLocator.getProviderService().
			findProviderByMediator(mediator, Locale.getDefault());
		if(provider == null) {
			throw new Exception("Mediator with id " + mediator.getId() + 
					" hasn't provider.");
		}
		return provider;
	}

	/*@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		this.ctx = arg0;
		this.serviceLocator = (IServiceLocator) this.ctx.getBean(IServiceLocator.class);
		this.messageSource = (ReloadableResourceBundleMessageSource) this.ctx.getBean(ReloadableResourceBundleMessageSource.class);
		this.validator = (Validator) this.ctx.getBean("validator");
		this.viewHelper = (ViewHelper) this.ctx.getBean(ViewHelper.class);
	}*/
		
	
}