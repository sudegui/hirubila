package com.m4f.utils.feeds.parser.impl;

import com.m4f.business.domain.School;
import com.m4f.test.spring.GaeSpringContextTest;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.m4f.utils.feeds.parser.ifc.ICoursesParser;

@Ignore
public class CoursesFeedParserTest extends GaeSpringContextTest {

	@Autowired
	private ICoursesParser coursesParser;
	
	@Test
	public void getCoursesTest() throws Exception {
		
		//coursesParser.getCourses(school);
		
	}
	
	
}
