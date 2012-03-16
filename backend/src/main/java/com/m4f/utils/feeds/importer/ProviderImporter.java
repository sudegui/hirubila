package com.m4f.utils.feeds.importer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;

import com.m4f.business.domain.Course;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.School;
import com.m4f.utils.PageManager;
import com.m4f.utils.seo.ifc.SeoCatalogBuilder;


public class ProviderImporter extends Importer {
	
	@Autowired
	SchoolImporter schoolImporter;
	
	@Autowired
	protected SeoCatalogBuilder catalogBuilder;
	
	public void importSchools(Provider provider) throws Exception {
		Collection<School> schools = this.schoolsParser.getSchools(provider);
		/**
		 * El feed de centros no es multidioma, con lo cual no hay que procesar cada una de las locales
		 * for(Locale locale : this.configurationService.getLocales()) {this.storeSchools(provider,schools,locale);}	
		 */
		storeSchools(provider, schools, configurationService.getDefaultLocale());
	}
	
	public  void importCourses(Provider provider) throws Exception {
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
	
	
	private  void storeSchools(Provider provider, Collection<School> schools, 
			Locale locale) throws Exception {
		schoolStorage.store(schools, locale, provider);
	}
	
	public void createLoadTask(Provider provider, School school) throws Exception {
		schoolImporter.importCourses(provider, school);
	}
	
	public void createCourseCatalogTask(Provider provider, School school, Collection<Course> courses, Locale locale) throws Exception {
		this.catalogBuilder.buildSeo(courses, school, provider, locale);
	}
	
	/*private  void createLoadTask(Provider p, School s) {
		TaskOptions taskOptions = TaskOptions.Builder.withPayload(new LoadSchoolCoursesTask(p,s));
		taskOptions.taskName(p.getId() + "-" + s.getId() + 
				"-" + Calendar.getInstance().getTimeInMillis());
		Queue queue = QueueFactory.getQueue(configurationService.getGlobalConfiguration().SCHOOL_QUEUE);
		queue.add(taskOptions);
	}*/
	
}