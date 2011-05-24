package com.m4f.business.service.ifc;

import com.m4f.business.domain.GlobalConfiguration;

public interface IAppConfigurationService {

    public GlobalConfiguration getGlobalConfiguration();
    public void saveGlobalConfiguration(GlobalConfiguration configuration) throws Exception;
    public void loadDefaultConfiguration() throws Exception;
    public void deleteGlobalConfiguration() throws Exception;
}
