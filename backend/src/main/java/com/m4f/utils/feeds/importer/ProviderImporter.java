package com.m4f.utils.feeds.importer;

import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.School;
import com.m4f.utils.PageManager;
import com.m4f.utils.feeds.importer.tasks.LoadSchoolCoursesTask;

public class ProviderImporter extends Importer {
	
	
	public static void importSchools(Provider provider) throws Exception {
		Collection<School> schools = schoolsParser.getSchools(provider);
		/**
		 * El feed de centros no es multidioma, con lo cual no hay que procesar cada una de las locales
		 * for(Locale locale : this.configurationService.getLocales()) {this.storeSchools(provider,schools,locale);}	
		 */
		storeSchools(provider, schools, configurationService.getDefaultLocale());
	}
	
	public static void importCourses(Provider provider) throws Exception {
		PageManager<School> paginator = new PageManager<School>();
		long total = schoolService.countSchoolsByProvider(provider.getId());
		paginator.setOffset(RANGE);
		paginator.setStart(0);
		paginator.setSize(total);
		for (Integer page : paginator.getTotalPagesIterator()) {
			int start = (page - 1) * RANGE;
			int end = (page) * RANGE;
			Collection<School> schools = schoolService.getSchoolsByProvider(provider.getId(), 
					"updated", null, start, end);
			for(School school : schools) {
				createLoadTask(provider, school);
			}
		}
	}
	
	private static void storeSchools(Provider provider, Collection<School> schools, 
			Locale locale) throws Exception {
		schoolStorage.store(schools, locale, provider);
	}
	
	private static void createLoadTask(Provider p, School s) {
		TaskOptions taskOptions = TaskOptions.Builder.withPayload(new LoadSchoolCoursesTask(p,s));
		taskOptions.taskName(p.getId() + "-" + s.getId() + 
				"-" + Calendar.getInstance().getTimeInMillis());
		Queue queue = QueueFactory.getQueue(configurationService.getGlobalConfiguration().SCHOOL_QUEUE);
		queue.add(taskOptions);
	}
	
}