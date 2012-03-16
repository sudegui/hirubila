package com.m4f.utils.feeds.importer.tasks;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.appengine.api.taskqueue.DeferredTask;
import com.m4f.business.domain.Course;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.School;
import com.m4f.utils.StackTraceUtil;
import com.m4f.utils.feeds.importer.SchoolImporter;
import com.m4f.utils.feeds.parser.ifc.ICourseStorage;
import com.m4f.utils.feeds.parser.ifc.ICoursesParser;

@SuppressWarnings("serial")
public class LoadSchoolCoursesTask extends SpringContextAwareTask implements DeferredTask {
	private static final Logger LOGGER = Logger.getLogger(LoadSchoolCoursesTask.class.getName());
	
	@Autowired 
	SchoolImporter schoolImporter;
	
	private Provider provider;
	private School school;
	
	
	public LoadSchoolCoursesTask(Provider p, School s) {
		this.provider = p;
		this.school = s;	
	}
	
	@Override
	public void run() {
		try {
			schoolImporter.importCourses(provider, school);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
		}
	}

}
