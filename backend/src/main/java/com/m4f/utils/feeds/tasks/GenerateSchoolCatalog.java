package com.m4f.utils.feeds.tasks;

import java.util.Collection;
import java.util.Locale;
import java.util.logging.Logger;
import com.google.appengine.api.taskqueue.DeferredTask;
import com.m4f.business.domain.Course;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.School;
import com.m4f.business.service.ifc.I18nCourseService;
import com.m4f.utils.PageManager;
import com.m4f.utils.StackTraceUtil;
import com.m4f.utils.seo.ifc.SeoCatalogBuilder;

public class GenerateSchoolCatalog extends SpringContextAwareTask implements DeferredTask {
	
	private static final Logger LOGGER = Logger.getLogger(GenerateSchoolCatalog.class.getName());
	private Provider provider;
	private School school;
	private Locale locale;
	
	public GenerateSchoolCatalog(Provider p, School s, Locale l) {
		this.provider = p;
		this.school = s;
		this.locale = l;
	}
	
	@Override
	public void run() {
		SeoCatalogBuilder catalogBuilder = this.getCurrentWebApplicationContext().getBean(SeoCatalogBuilder.class);
		I18nCourseService courseService = this.getCurrentWebApplicationContext().getBean(I18nCourseService.class);
		if(catalogBuilder==null || courseService==null) return;
		try {
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
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
		}
		
	}

}
