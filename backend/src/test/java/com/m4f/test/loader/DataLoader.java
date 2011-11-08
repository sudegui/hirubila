package com.m4f.test.loader;

import java.util.List;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.m4f.business.domain.Provider;
import com.m4f.business.service.ifc.I18nProviderService;
import com.m4f.business.service.ifc.IAppConfigurationService;
import com.m4f.test.spring.GaeSpringContextTest;

@Ignore
public class DataLoader extends GaeSpringContextTest {
	
	@Autowired
	protected I18nProviderService providerService;
	@Autowired
	protected IAppConfigurationService configurationService;
	
	
	@Test
	public void loadData() throws Exception {
		Provider provider = providerService.createProvider();
		provider.setName("Proveedor local");
		provider.setFeed("http://localhost/feeds/zentruak.xml");
		providerService.save(provider, new Locale("es"));
		List<Provider> providers = providerService.getAllProviders(new Locale("es"));
		if(providers.size()==0) {
			Assert.fail("No hay providers");
		}
	}
	
}