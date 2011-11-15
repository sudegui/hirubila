package com.m4f.utils.feeds.tasks;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;
import com.google.appengine.api.taskqueue.DeferredTask;
import com.m4f.business.domain.Course;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.School;
import com.m4f.utils.StackTraceUtil;
import com.m4f.utils.feeds.parser.ifc.ICourseStorage;
import com.m4f.utils.feeds.parser.ifc.ICoursesParser;

@SuppressWarnings("serial")
public class LoadSchoolCoursesTask extends SpringContextAwareTask implements DeferredTask {
	
	private static final Logger LOGGER = Logger.getLogger(LoadSchoolCoursesTask.class.getName());
	
	private Provider provider;
	private School school;
	
	
	public LoadSchoolCoursesTask(Provider p, School s) {
		this.provider = p;
		this.school = s;	
	}
	
	@Override
	public void run() {
		try {
			ICourseStorage courseStorage = this.getCurrentWebApplicationContext().getBean(ICourseStorage.class);
			ICoursesParser coursesParser = this.getCurrentWebApplicationContext().getBean(ICoursesParser.class);
			Map<String, List<Course>> parsedCourses = coursesParser.getCourses(school);
			for(String lang : parsedCourses.keySet()) {
				courseStorage.store(parsedCourses.get(lang), new Locale(lang), school, provider);
			}
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
		}
		
	}

}
