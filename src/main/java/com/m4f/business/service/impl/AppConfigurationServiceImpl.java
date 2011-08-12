package com.m4f.business.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;

import com.m4f.business.domain.GlobalConfiguration;
import com.m4f.business.service.ifc.IAppConfigurationService;
import com.m4f.utils.StackTraceUtil;
import com.m4f.utils.dao.ifc.DAOSupport;

public class AppConfigurationServiceImpl extends DAOBaseService implements IAppConfigurationService {
	
	private GlobalConfiguration defaultConfiguration;
	private GlobalConfiguration globalConfiguration = null;
	private Map<String,Locale> localesMap;
	private List<Locale> avLocales;
	
	public AppConfigurationServiceImpl(DAOSupport dao, GlobalConfiguration defaultConfiguration) {
		super(dao);
		this.defaultConfiguration = defaultConfiguration;
	}

	@Override
	public void deleteGlobalConfiguration() throws Exception {
		this.DAO.deleteAll(GlobalConfiguration.class);
		this.loadGlobalConfiguration();
	}

	@Override
	public GlobalConfiguration getGlobalConfiguration() {
		if(this.globalConfiguration == null) {
            this.loadGlobalConfiguration();
	    }
	    return this.globalConfiguration;
	}

	@Override
	public void loadDefaultConfiguration() throws Exception {
		this.deleteGlobalConfiguration();
        this.saveGlobalConfiguration(this.defaultConfiguration);
	}

	@Override
	public void saveGlobalConfiguration(GlobalConfiguration configuration)
			throws Exception {
		this.DAO.saveOrUpdate(configuration);
		this.globalConfiguration = configuration;
	}
	

	@Override
	public Map<String, Locale> getLocalesMap() throws Exception {
		if(this.localesMap == null) {
			this.loadLocales();
		}
		return this.localesMap;
	}

	@Override
	public List<Locale> getLocales()throws Exception {
		if(this.avLocales == null) {
			this.loadLocales();
		}
		return this.avLocales;
	}
	
	private void loadLocales() {
		this.avLocales = new ArrayList<Locale>();
		this.localesMap = new HashMap<String,Locale>();
		String[] languages = this.getGlobalConfiguration().getLanguages().split("[,]");
		for(String language : languages) {
			Locale locale = new Locale(language);
			if(locale!=null) {
				this.avLocales.add(new Locale(language));
				this.localesMap.put(language, locale);
			}
		}
	}
	
	private void loadGlobalConfiguration()  {
		try {
			List<GlobalConfiguration> configurations = this.DAO.findAll(GlobalConfiguration.class);
			if(configurations != null && configurations.size() > 0) {
	        	this.globalConfiguration = configurations.get(0);
	        } else {
	        	this.globalConfiguration = this.defaultConfiguration;
	        }
		} catch (Exception e) {
			this.globalConfiguration = this.defaultConfiguration;
		}
	}
	
	
}