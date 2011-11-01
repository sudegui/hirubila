package com.m4f.utils.content;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import com.m4f.business.domain.Provider;
import com.m4f.business.service.ifc.I18nProviderService;
import com.m4f.test.spring.GaeSpringContextTest;
import com.m4f.utils.feeds.parser.ifc.ISchoolsParser;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.SAXException;

public class GaeHttpAcquirerTest extends GaeSpringContextTest {
	
	@Autowired
	private ISchoolsParser schoolsFeedParser;
	@Autowired
	protected I18nProviderService providerService;
	
	@Test
	public void testConfiguration() throws ParserConfigurationException, SAXException, IOException, Exception {
		Provider provider = providerService.createProvider();
		provider.setId(1L);
		provider.setName("Proveedor de prueba de Debabarrena");
		provider.setFeed("http://www.zerikasi.com/feed/ikasgida/debabarrena.php");
		System.out.println("Initializing import process");
		schoolsFeedParser.getSchools(provider);
		System.out.println("Ending import process");
	}
	
}