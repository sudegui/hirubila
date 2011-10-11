package com.m4f.utils.feeds.aop;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.FieldError;
import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;
import com.m4f.business.domain.Course;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.School;
import com.m4f.utils.feeds.events.model.Dump;
import com.m4f.utils.feeds.events.model.StoreErrorEvent;
import com.m4f.utils.feeds.events.model.StoreSuccessEvent;
import com.m4f.utils.seo.SeoCatalogBuilder;
import com.m4f.utils.worker.WorkerFactory;
import com.m4f.utils.StackTraceUtil;
import com.m4f.business.service.ifc.IAppConfigurationService;
import com.m4f.utils.feeds.events.service.ifc.EventService;

public class DumperHypervisor {
	
	
	private static final Logger LOGGER = Logger.getLogger(DumperHypervisor.class.getName());
	
	@Autowired
	private SeoCatalogBuilder catalogBuilder;	
	@Autowired
	private EventService eventService;
	@Autowired
	protected WorkerFactory workerFactory;
	@Autowired
	protected IAppConfigurationService configurationService;
	
	public void registerSchoolOperation(Dump dump, Provider provider, School school, 
			Locale locale, List<FieldError> retVal)  {
		/*Un centro que cumple esta condición ya existía en la base de datos.*/
		if(school.getId() == null) {
			return;
		}
		if(!retVal.isEmpty()) {
			this.registerSchoolError(dump, retVal, school, locale);
		} else {
			this.registerSchoolSuccess(dump, school, locale);
		}
	}
	
	private void registerSchoolSuccess(Dump dump, School school, Locale locale) {
		StoreSuccessEvent dumperSuccess = eventService.createStoreSuccessEvent();
		dumperSuccess.setEntityClass(School.class.getName());
		dumperSuccess.setEntityId(school.getId());
		dumperSuccess.setWhen(Calendar.getInstance(new Locale("es")).getTime());
		dumperSuccess.setDumpId(dump.getId());
		dumperSuccess.setLanguage(locale.getLanguage());
		try {
			eventService.save(dumperSuccess);
		}catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
		}
	}
	
	private void registerSchoolError(Dump dump, List<FieldError> errors, 
			School school, Locale locale)  {
		StoreErrorEvent dumperError = 
			eventService.createStoreErrorEvent();
		StringBuffer sb = new StringBuffer();
		for(FieldError error : errors) {
			sb.append(error.getField() + ":" + error.getRejectedValue() + 
					":" + error.getDefaultMessage());
			sb.append("\n");
		}
		dumperError.setEntityClass(School.class.getName());
		dumperError.setCause(new Text(sb.toString()));
		dumperError.setEntityId(school.getId());
		dumperError.setWhen(Calendar.getInstance(new Locale("es")).getTime());
		dumperError.setDumpId(dump.getId());
		dumperError.setLanguage(locale.getLanguage());
		try {
			byte[] objectBytes = this.serializeObject(school);
			dumperError.setObject(new Blob(objectBytes));
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
		} finally {
			try {
				eventService.save(dumperError);
			} catch(Exception e) {
				LOGGER.severe(StackTraceUtil.getStackTrace(e));
			}
		}
	}
	
	public void registerCourseOperation(Dump dump, Course course, 
			Locale locale, List<FieldError> retVal) throws  Exception {
		/*Un curso que cumple esta condición ya existía en la base de datos.*/
		if(course.getId() == null) {
			return;
		}
		if(!retVal.isEmpty()) {
			this.registerCourseError(dump, retVal, course, locale);
		} else {
			this.createCourseCatalog(course, locale);
			try {
				this.registerCourseSuccess(dump, course, locale);
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	private void createCourseCatalog(Course course, Locale locale) 
		throws Exception {
		this.catalogBuilder.buildSeoEntity(course, locale);
	}
	
	private void _createCourseCatalog(Course course, Locale locale) 
		throws  Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("courseId", course.getId().toString());
		params.put("language", locale.getLanguage());
		workerFactory.createWorker().addWork(configurationService.getGlobalConfiguration().CATALOG_QUEUE, 
				"/task/catalog/create", params);
	}
	
	private void registerCourseError(Dump dump, List<FieldError> errors, 
			Course course, Locale locale) {
		StoreErrorEvent dumperError = eventService.createStoreErrorEvent();
		StringBuffer sb = new StringBuffer();
		for(FieldError error : errors) {
			sb.append(error.getField() + ":" + error.getRejectedValue() + 
					":" + error.getDefaultMessage());
			sb.append("\n");
		}
		dumperError.setEntityClass(Course.class.getName());
		dumperError.setCause(new Text(sb.toString()));
		dumperError.setEntityId(course.getId());
		dumperError.setWhen(Calendar.getInstance(new Locale("es")).getTime());
		dumperError.setDumpId(dump.getId());
		dumperError.setLanguage(locale.getLanguage());
		try {
			byte[] objectBytes = this.serializeObject(course);
			dumperError.setObject(new Blob(objectBytes));
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
		} finally {
			try {
				eventService.save(dumperError);
			} catch(Exception e) {
				LOGGER.severe(StackTraceUtil.getStackTrace(e));
			}
		}
	}
	
	private void registerCourseSuccess(Dump dump, Course course, Locale locale) {
		StoreSuccessEvent dumperSuccess = eventService.createStoreSuccessEvent();
		dumperSuccess.setEntityClass(Course.class.getName());
		dumperSuccess.setEntityId(course.getId());
		dumperSuccess.setWhen(Calendar.getInstance(new Locale("es")).getTime());
		dumperSuccess.setDumpId(dump.getId());
		dumperSuccess.setLanguage(locale.getLanguage());
		try {
			eventService.save(dumperSuccess);
		}catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
		}
	}
	
	private byte[] serializeObject(Object obj) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
		ObjectOutput out = new ObjectOutputStream(bos);
		out.writeObject(obj);
		out.close();
		// Get the bytes of the serialized object
	    byte[] buf = bos.toByteArray();
	    return buf;
	}
	
}