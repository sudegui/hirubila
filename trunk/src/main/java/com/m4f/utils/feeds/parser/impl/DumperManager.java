package com.m4f.utils.feeds.parser.impl;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.validation.Validator;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;
import com.m4f.business.domain.Course;
import com.m4f.business.domain.CourseCatalog;
import com.m4f.business.domain.School;
import com.m4f.business.service.ifc.I18nCourseService;
import com.m4f.business.service.ifc.I18nSchoolService;
import com.m4f.business.service.ifc.I18nTerritorialService;
import com.m4f.business.service.ifc.ICourseHtmlService;
import com.m4f.utils.beans.BeanManager;
import com.m4f.utils.beans.exception.NotSameClassException;
import com.m4f.utils.feeds.events.model.Dump;
import com.m4f.utils.feeds.parser.ifc.DumperCapable;
import com.m4f.web.controller.task.TaskController;
import com.m4f.business.domain.Provider;

public class DumperManager implements DumperCapable {
	private static final Logger LOGGER = Logger.getLogger(DumperManager.class.getName());
	
	private Validator validator;
	private I18nSchoolService schoolService;
	private I18nCourseService courseService;
	private ICourseHtmlService courseCatalogService;
	private BeanManager beanManager;
	
	public DumperManager(Validator validator, I18nSchoolService schServce,
			I18nCourseService courseService, ICourseHtmlService courseCatalogService, BeanManager bManager) {
		this.validator = validator;
		this.schoolService = schServce;
		this.courseService = courseService;
		this.courseCatalogService = courseCatalogService;
		this.beanManager = bManager;
	}
	
	@Override
	public List<FieldError> dumpSchool(Dump dump, 
			School school, Locale locale, Provider provider) throws Exception {
		DataBinder dataBinder = new DataBinder(school);
		dataBinder.setValidator(validator);
		dataBinder.validate();
		if(!dataBinder.getBindingResult().hasErrors()) {
			this.selectiveSchoolStore(school, locale);
		}
		return dataBinder.getBindingResult().getFieldErrors();
	}
	
	
	
	@Override
	public List<FieldError> dumpCourse(Dump dump, Course course, Locale locale, School school, 
			Provider provider, String province, String region, String town) throws Exception {
		DataBinder dataBinder = new DataBinder(course);
		dataBinder.setValidator(validator);
		dataBinder.validate();
		if(!dataBinder.getBindingResult().hasErrors()) {
			if(this.selectiveCourseStore(course, locale)) {
				CourseCatalog courseCatalog = new CourseCatalog(course, locale.getLanguage(), school, provider.getName(), province, region, town);
				CourseCatalog oldCourseCatalog = this.courseCatalogService.getCourseCatalogByCourseId(course.getId(), locale);
				if(oldCourseCatalog != null) courseCatalog.setId(oldCourseCatalog.getId());
				this.courseCatalogService.save(courseCatalog);
				LOGGER.log(Level.FINE, new StringBuffer("Se anyade el curso: ").append(course).toString());
			} else {
				LOGGER.log(Level.FINE, new StringBuffer("No se anyade el curso: ").append(course).toString());
				CourseCatalog courseCatalog = new CourseCatalog(course, locale.getLanguage(), school, provider.getName(), province, region, town);
				CourseCatalog oldCourseCatalog = this.courseCatalogService.getCourseCatalogByCourseId(course.getId(), locale);
				if(oldCourseCatalog != null) courseCatalog.setId(oldCourseCatalog.getId());
				this.courseCatalogService.save(courseCatalog);
				LOGGER.log(Level.FINE, new StringBuffer("Se regenera su version del catalogo: ").append(course).toString());
			}
		}
		return dataBinder.getBindingResult().getFieldErrors();
	}
	
	private boolean selectiveCourseStore(Course newCourse, Locale locale) 
		throws NotSameClassException, Exception {
		boolean stored = false;
		Course oldCourse = this.courseService.getCourseByExternalId(newCourse.getExternalId(), locale);
		if(oldCourse == null) {
			newCourse.setCreated(Calendar.getInstance(new Locale("es")).getTime());
			//newCourse.setUpdated(Calendar.getInstance(new Locale("es")).getTime());
			newCourse.setActive(true);
			this.courseService.save(newCourse, locale);
			stored = true;
		} else if(!oldCourse.equals(newCourse) || !oldCourse.isTranslated()) {
			Set<String> properties = new HashSet<String>();
			properties.add("externalId");
			properties.add("title");
			properties.add("url");
			properties.add("start");
			properties.add("end");
			properties.add("information");
			properties.add("active");

			this.beanManager.mergeObjects(newCourse, oldCourse, properties);
			if(oldCourse.isTranslated()) oldCourse.setUpdated(Calendar.getInstance(new Locale("es")).getTime());
			newCourse.setId(oldCourse.getId());
			this.courseService.save(oldCourse, locale);
			stored = true;
		} else {
			LOGGER.log(Level.INFO, new StringBuffer("El curso: ").append(newCourse.toString())
					.append(" no se actualiza ni modifica porque ya estaba en BD.").toString());
		}
		return stored;
	}
	
	private void selectiveSchoolStore(School newSchool, Locale locale) 
		throws NotSameClassException, Exception {	
		School oldSchool;
		oldSchool = this.schoolService.getSchoolByExternalId(newSchool.getExternalId(), locale);
		if(oldSchool == null) {
			// Creacion de un centro nuevo.
			newSchool.setCreated(Calendar.getInstance(new Locale("es")).getTime());
			newSchool.setUpdated(Calendar.getInstance(new Locale("es")).getTime());		
			this.schoolService.save(newSchool, locale);
		} else {
			// Actualizacion de un centro existente.
			// Logica de actualizacion
			Set<String> properties = new HashSet<String>();
			properties.add("telephone");
			properties.add("fax");
			properties.add("zipCode");
			properties.add("webSite");
			properties.add("streetAddress");
			properties.add("city");
			this.beanManager.mergeObjects(newSchool.getContactInfo(), 
					oldSchool.getContactInfo(), properties);
			properties = new HashSet<String>();
			properties.add("externalId");
			properties.add("name");
			properties.add("feed");
			this.beanManager.mergeObjects(newSchool, oldSchool, properties);
			newSchool.setId(oldSchool.getId());
			oldSchool.setUpdated(Calendar.getInstance(new Locale("es")).getTime());
			this.schoolService.save(oldSchool, locale);
		}
	}
	
}