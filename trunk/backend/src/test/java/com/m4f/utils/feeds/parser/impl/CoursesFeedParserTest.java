package com.m4f.utils.feeds.parser.impl;

import com.m4f.business.domain.Course;
import com.m4f.business.domain.School;
import com.m4f.business.domain.Provider;
import com.m4f.business.service.ifc.I18nProviderService;
import com.m4f.business.service.ifc.I18nSchoolService;
import com.m4f.business.service.ifc.IAppConfigurationService;
import com.m4f.test.spring.GaeSpringContextTest;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.m4f.utils.feeds.parser.ifc.ICourseStorage;
import com.m4f.utils.feeds.parser.ifc.ICoursesParser;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Ignore
public class CoursesFeedParserTest extends GaeSpringContextTest {

	@Autowired
	private ICoursesParser coursesParser;
	@Autowired
	protected I18nProviderService providerService;
	@Autowired
	protected I18nSchoolService schoolService;
	@Autowired
	protected IAppConfigurationService configurationService;
	@Autowired
	protected ICourseStorage courseStorage;
	
	@Test
	public void getCoursesTest() throws Exception {
		List<Long> providers = providerService.getAllProviderIds();
		if(providers.isEmpty()) {
			System.out.println("No hay providers");
		}
		Long providerId = providers.get(0);
		List<School> schools = schoolService.getSchoolsByProvider(providerId, null, null); 
		if(schools.isEmpty()) {
			System.out.println("No hay centros");
		}
		for(School school : schools) {
			Map<String, List<Course>> courses = coursesParser.getCourses(school);
			for(String lang : courses.keySet() ) {
				Locale locale = new Locale(lang);
				Provider provider = providerService.getProviderById(providerId, locale);
				courseStorage.store(courses.get(lang), locale, school, provider);
			}
			
		}
	}
	
	
}
