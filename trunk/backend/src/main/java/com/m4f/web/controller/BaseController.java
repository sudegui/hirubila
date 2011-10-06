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
import com.m4f.business.service.ifc.IServiceLocator;
import com.m4f.utils.StackTraceUtil;
import com.m4f.utils.seo.SeoCatalogBuilder;

@SessionAttributes(value={"langs"})
public abstract class BaseController {
	
	private static final Logger LOGGER = Logger.getLogger(BaseController.class.getName());
	protected static List<String> connectedClients = new ArrayList<String>();
	
	@Autowired
	private ReloadableResourceBundleMessageSource messageSource;
	@Autowired
	protected IServiceLocator serviceLocator;
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
			return this.serviceLocator.getAppConfigurationService().getLocales();
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return new ArrayList<Locale>();
		}
		
	}
	
	@ModelAttribute("langs")
	public Map<String, Locale> getAvailableMapLanguages() {
		try {
			return this.serviceLocator.getAppConfigurationService().getLocalesMap();
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return new HashMap<String, Locale>();
		}
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
		
	
}