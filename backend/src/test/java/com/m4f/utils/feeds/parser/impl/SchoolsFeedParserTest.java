package com.m4f.utils.feeds.parser.impl;

import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.Assert;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.School;
import com.m4f.business.service.ifc.I18nProviderService;
import com.m4f.business.service.ifc.I18nSchoolService;
import com.m4f.test.spring.GaeSpringContextTest;
import com.m4f.utils.content.ifc.ContentAcquirer;
import com.m4f.utils.content.impl.GaeHttpAcquirer;
import com.m4f.utils.feeds.parser.ifc.ISchoolsParser;
import org.springframework.beans.factory.annotation.Autowired;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import java.util.Locale;

public class SchoolsFeedParserTest extends GaeSpringContextTest {
	
	@Autowired
	private ISchoolsParser schoolsFeedParser;
	@Autowired
	protected I18nSchoolService schoolService;
	@Autowired
	protected I18nProviderService providerService;
	
	
	
	@Test
	public void getSchoolsTest() throws Exception {
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Provider provider = providerService.createProvider();
		provider.setId(1L);
		provider.setName("Proveedor de prueba de Debabarrena");
		provider.setFeed("http://www.zerikasi.com/feed/ikasgida/debabarrena.php");
		List<School> schools = this.schoolsFeedParser.getSchools(provider);
		System.out.println("Schools size: " + schools.size());
	}
	
}