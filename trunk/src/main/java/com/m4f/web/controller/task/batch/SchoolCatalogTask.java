package com.m4f.web.controller.task.batch;

import java.util.List;
import java.util.Locale;
import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;
import com.m4f.business.domain.School;
import com.m4f.business.service.exception.ContextNotActiveException;
import com.m4f.business.service.exception.ServiceNotFoundException;
import com.m4f.business.service.ifc.I18nSchoolService;


public class  SchoolCatalogTask implements DeferredTask  {
	
	private List<Locale> locales;
	private I18nSchoolService schoolService;
	
	public SchoolCatalogTask(List<Locale> locales, I18nSchoolService schoolService) {
		this.locales = locales;
		this.schoolService = schoolService;
	}
	
	@Override
	public void run() {
		Queue queue = QueueFactory.getDefaultQueue();
		for(Locale locale : locales) { 
			try {
				for(School school : schoolService.getAllSchools(null, locale)) {
					TaskOptions options = TaskOptions.Builder.withUrl("/task/catalog/create");
					options.param("schoolId", school.getId().toString());
					options.param("lang", locale.getLanguage());
					options.method(Method.POST);
					queue.add(options);	
				}
			} catch (ServiceNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ContextNotActiveException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}
}
