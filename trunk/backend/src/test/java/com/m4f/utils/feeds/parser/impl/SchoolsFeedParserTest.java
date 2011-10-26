package com.m4f.utils.feeds.parser.impl;

import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.Assert;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.School;
import com.m4f.utils.content.ifc.IAcquirer;
import com.m4f.utils.content.impl.GaeHttpAcquirer;
import com.m4f.utils.feeds.events.model.Dump;

@Ignore
public class SchoolsFeedParserTest {
	
	private SchoolsFeedParser schoolsFeedParser;
	
	@Before
	public void setUp() {
		IAcquirer contentAcquirer = new GaeHttpAcquirer();
		this.schoolsFeedParser = new SchoolsFeedParser(contentAcquirer);
	}
	
	@Test
	public void getSchoolsTest() throws Exception {
		Provider provider = new Provider();
		provider.setId(1L);
		provider.setName("Proveedor de prueba de Debabarrena");
		provider.setFeed("http://www.zerikasi.com/feed/ikasgida/debabarrena.php");
		List<School> schools = this.schoolsFeedParser.getSchools(provider);
		Assert.assertTrue(schools.size() > 0);
	}
}