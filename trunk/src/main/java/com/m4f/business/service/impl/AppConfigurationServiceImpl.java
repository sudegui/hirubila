package com.m4f.business.service.impl;

import java.util.List;

import com.m4f.business.domain.GlobalConfiguration;
import com.m4f.business.service.ifc.IAppConfigurationService;
import com.m4f.utils.i18n.dao.ifc.DAOSupport;

public class AppConfigurationServiceImpl extends DAOBaseService implements IAppConfigurationService {
	private GlobalConfiguration defaultConfiguration;
	private GlobalConfiguration globalConfiguration = null;
	
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
