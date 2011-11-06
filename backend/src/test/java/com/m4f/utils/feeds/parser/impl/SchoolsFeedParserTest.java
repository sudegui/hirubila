package com.m4f.utils.feeds.parser.impl;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.Assert;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.School;
import com.m4f.business.service.ifc.I18nProviderService;
import com.m4f.business.service.ifc.I18nSchoolService;
import com.m4f.test.spring.GaeSpringContextTest;
import com.m4f.utils.feeds.parser.ifc.ISchoolsParser;
import com.m4f.utils.feeds.parser.ifc.IStorage;
import com.m4f.utils.feeds.parser.service.ifc.IParserService;
import org.springframework.beans.factory.annotation.Autowired;
import com.m4f.utils.feeds.parser.model.Feed;
import java.net.URI;


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
	protected IStorage<School> schoolStorage;
	
	@Test
	public void getSchoolsTest() throws Exception {
		Provider provider = providerService.createProvider();
		provider.setId(1L);
		provider.setName("Proveedor de prueba de Debabarrena");
		provider.setFeed("http://www.elespazio.com/xml_centros.asp");
		int eventsSizeBefore = 0, eventsSizeAfter = 0;
		Feed feed = parserService.getFeed(new URI(provider.getFeed()));
		if(feed!=null) {
			eventsSizeBefore = parserService.getLoadEvents(feed).size();
		}
		List<School> schools = this.schoolsFeedParser.getSchools(provider);
		System.out.println("Parsed schools: " + schools.size());
		feed = parserService.getFeed(new URI(provider.getFeed()));
		if(feed!=null) {
			eventsSizeAfter = parserService.getLoadEvents(feed).size();
		}
		
		schoolStorage.store(schools, new Locale("es"), provider);
		Assert.assertTrue(eventsSizeAfter>eventsSizeBefore);
		
	}
	
}