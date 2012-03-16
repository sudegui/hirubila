package com.m4f.business.service.ifc;

import java.util.Locale;
import java.util.Map;
import java.util.List;
import com.m4f.business.domain.GlobalConfiguration;

public interface IAppConfigurationService {
    GlobalConfiguration getGlobalConfiguration();
    void saveGlobalConfiguration(GlobalConfiguration configuration) throws Exception;
    void loadDefaultConfiguration() throws Exception;
    void deleteGlobalConfiguration() throws Exception;
    Map<String, Locale> getLocalesMap() throws Exception;
    List<Locale> getLocales() throws Exception;
    Locale getDefaultLocale() throws Exception;
}
