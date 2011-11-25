package com.m4f.utils.feeds.importer.tasks;

import java.util.Locale;
import java.util.logging.Logger;
import com.google.appengine.api.taskqueue.DeferredTask;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.School;
import com.m4f.utils.StackTraceUtil;
import com.m4f.utils.feeds.importer.SchoolImporter;

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
		try {
			SchoolImporter.createCoursesCatalog(provider, school, locale);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
		}
	}

}
