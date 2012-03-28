package com.m4f.utils.feeds.importer;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.m4f.business.domain.Course;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.School;
import com.m4f.utils.PageManager;
import com.m4f.utils.feeds.events.model.Dump;

public class SchoolImporter extends Importer {
	
	public void importCourses(Provider provider, School school, Dump dump) 
			throws ParserConfigurationException, SAXException, IOException, Exception {
		Map<String, List<Course>> parsedCourses = coursesParser.getCourses(school, dump);
		
		for(String lang : parsedCourses.keySet()) {
			courseStorage.store(parsedCourses.get(lang), new Locale(lang), school, provider);
		}
		
	}
	
	public void createCoursesCatalog(Provider provider, 
			School school, Locale locale) throws Exception {
		final int RANGE = 300;
		PageManager<Course> paginator = new PageManager<Course>();
		long total = courseService.countCoursesBySchool(school);
		paginator.setOffset(RANGE);
		paginator.setStart(0);
		paginator.setSize(total);
		for (Integer page : paginator.getTotalPagesIterator()) {
			int start = (page - 1) * RANGE;
			int end = (page) * RANGE;
			Collection<Course> courses = courseService.getCoursesBySchool(school.getId(), "id", locale,start, end);
			catalogBuilder.buildSeo(courses, school, provider, locale);
		}
	}
	
}