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
	protected I18nSchoolService schoolService;
	@Autowired
	protected IAppConfigurationService configurationService;
	@Autowired
	protected ISchoolsParser schoolsParser;
	@Autowired
	protected ISchoolStorage schoolStorage;
	@Autowired
	protected ICourseStorage courseStorage;
	@Autowired
	protected ICoursesParser coursesParser;
	@Autowired
	protected I18nCourseService courseService;
	@Autowired
	protected SeoCatalogBuilder catalogBuilder;
	public static final int RANGE = 100;
	
}