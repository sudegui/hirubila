package com.m4f.utils.feeds.parser.impl;

import java.util.List;
import java.util.Locale;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.Assert;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.School;
import com.m4f.business.service.ifc.I18nProviderService;
import com.m4f.business.service.ifc.I18nSchoolService;
import com.m4f.business.service.ifc.IAppConfigurationService;
import com.m4f.test.spring.GaeSpringContextTest;
import com.m4f.utils.feeds.parser.ifc.ISchoolsParser;
import com.m4f.utils.feeds.parser.ifc.ISchoolStorage;
import com.m4f.utils.feeds.parser.service.ifc.IParserService;
import org.springframework.beans.factory.annotation.Autowired;
import com.m4f.utils.feeds.parser.model.Feed;
import java.net.URI;

@Ignore
public class SchoolsFeedParserTest extends GaeSpringContextTest {
	
	@Autowired
	private ISchoolsParser schoolsFeedParser;
	@Autowired
	protected I18nSchoolService schoolService;
	@Autowired
	protected I18nProviderService providerService;
	@Autowired
	private IParserService parserService;
	@Autowired
	protected ISchoolStorage schoolStorage;
	@Autowired
	protected IAppConfigurationService configurationService;
	
	@Test
	public void getSchoolsTest() throws Exception {
		List<Provider> providers = providerService.getAllProviders(new Locale("es"));
		if(providers.size()==0) {
			Assert.fail("No hay providers");
		}
		Provider provider = providerService.getAllProviders(configurationService.getLocales().get(0)).get(0);
		int eventsSizeBefore = 0, eventsSizeAfter = 0;
		Feed feed = parserService.getFeed(new URI(provider.getFeed()));
		if(feed!=null) {
			eventsSizeBefore = parserService.getLoadEvents(feed).size();
		}
		List<School> schools = this.schoolsFeedParser.getSchools(provider, null);
		System.out.println("Parsed schools: " + schools.size());
		feed = parserService.getFeed(new URI(provider.getFeed()));
		if(feed!=null) {
			eventsSizeAfter = parserService.getLoadEvents(feed).size();
		}
		schoolStorage.store(schools, new Locale("es"), provider);
		Assert.assertTrue(eventsSizeAfter>eventsSizeBefore);
		
	}
	
}