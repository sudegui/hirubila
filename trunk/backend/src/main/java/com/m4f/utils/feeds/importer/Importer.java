package com.m4f.utils.feeds.importer;

import org.springframework.beans.factory.annotation.Autowired;

import com.m4f.business.service.ifc.I18nCourseService;
import com.m4f.business.service.ifc.I18nSchoolService;
import com.m4f.business.service.ifc.IAppConfigurationService;
import com.m4f.utils.feeds.parser.ifc.ICourseStorage;
import com.m4f.utils.feeds.parser.ifc.ICoursesParser;
import com.m4f.utils.feeds.parser.ifc.ISchoolStorage;
import com.m4f.utils.feeds.parser.ifc.ISchoolsParser;
import com.m4f.utils.seo.ifc.SeoCatalogBuilder;

public abstract class Importer {
	
	@Autowired
	protected static I18nSchoolService schoolService;
	@Autowired
	protected static IAppConfigurationService configurationService;
	@Autowired
	protected static ISchoolsParser schoolsParser;
	@Autowired
	protected static ISchoolStorage schoolStorage;
	@Autowired
	protected static ICourseStorage courseStorage;
	@Autowired
	protected static ICoursesParser coursesParser;
	@Autowired
	protected static I18nCourseService courseService;
	@Autowired
	protected static SeoCatalogBuilder catalogBuilder;
	protected static final int RANGE = 100;
	
}