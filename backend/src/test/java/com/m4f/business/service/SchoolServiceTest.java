package com.m4f.business.service;

import java.util.Locale;
import java.util.logging.Logger;
import com.m4f.business.domain.School;
import com.m4f.business.service.impl.SchoolServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import org.junit.Ignore;
import com.m4f.utils.i18n.dao.impl.jdo.JdoI18nDAO;

@Ignore
public class SchoolServiceTest {
	
	private static final Logger LOGGER = Logger.getLogger(SchoolServiceTest.class.getName());
	private SchoolServiceImpl schoolService = null;
	//private final LocalServiceTestHelper helper =new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

	@Before
	public void setUp() {
		//helper.setUp();
		//this.schoolService = new SchoolServiceImpl(new JdoI18nDAO());		
	}
	
	@After
    public void tearDown() {
        //helper.tearDown();
    }
	
	@Test
	public void addSchool() throws Exception {
		Locale locale = Locale.getDefault();
		/*LOGGER.info("ISO3 Language: " + locale.getISO3Language());
		LOGGER.info("ISO3 Country: " + locale.getISO3Country());
		LOGGER.info("Language: " + locale.getLanguage());*/
		School newSchool = new School();
		LOGGER.info("Class.name: " + newSchool.getClass().getName());
		LOGGER.info("Class.canonicalName: " + newSchool.getClass().getCanonicalName());
	}
}