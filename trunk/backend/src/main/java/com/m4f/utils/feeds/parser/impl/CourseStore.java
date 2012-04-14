package com.m4f.utils.feeds.parser.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Collection;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import com.m4f.business.domain.Course;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.School;
import com.m4f.utils.beans.exception.NotSameClassException;
import com.m4f.utils.feeds.parser.ifc.ICourseStorage;
import com.m4f.utils.seo.ifc.SeoCatalogBuilder;

public class CourseStore extends StoreBase<Course> implements ICourseStorage {
	
	private static final Logger LOGGER = Logger.getLogger(CourseStore.class.getName());
	private List<Course> entities  = new ArrayList<Course>();
	
	@Autowired
	protected SeoCatalogBuilder catalogBuilder;
	
	private class CourseComparator implements Comparator<Course> {

		@Override
		public int compare(Course o1, Course o2) {
			if(o1.toString().equals(o2.toString())) return 0;
			return -1;
		}
		
	}
	
	@Override
	public Map<Course , List<FieldError>> store(Collection<Course> courses, 
			Locale locale, School school, Provider provider) throws Exception {
		this.entities = new ArrayList<Course>(); 
		Map<Course , List<FieldError>> executions = new HashMap<Course , List<FieldError>>();
		for(Course course : courses) {
			course.setRegulated(provider.getRegulated());
			DataBinder dataBinder = new DataBinder(course);
			dataBinder.setValidator(validator);
			dataBinder.validate();
			executions.put(course, dataBinder.getBindingResult().getFieldErrors());
			if(!dataBinder.getBindingResult().hasErrors()) {
				this.selectiveCourseStore(course, locale);
			}
		}
		this.flush(locale);
		
		// CREANDO EL CATALOGO!!!!
		//catalogBuilder.buildSeo(this.entities, school, provider, locale);
		return executions;
	}
	
	private void selectiveCourseStore(Course newCourse, Locale locale) 
			throws NotSameClassException, Exception {
			newCourse.setCreated(Calendar.getInstance(new Locale("es")).getTime());
			newCourse.setUpdated(newCourse.getCreated());	
			Course oldCourse = this.courseService.getCourseByExternalId(newCourse.getExternalId(), locale);
			if(oldCourse == null) { // Alta nueva: el curso no estaba registrado en la base de datos.
				LOGGER.info("Curso NO existente: (" + locale.getLanguage() + ")" + newCourse.getTitle());
				LOGGER.info("NewCourse: " + newCourse.toString());
				newCourse.setActive(true);
				this.entities.add(newCourse);
			} else if(!oldCourse.equals(newCourse) || !oldCourse.isTranslated()) {
				CourseComparator comparator = new CourseComparator();
				/*if(comparator.compare(newCourse, oldCourse) == 0) {
					LOGGER.info("Curso existente y NO MODIFICADO");
					return;
				} */
				LOGGER.info("Curso existente y MODIFICADO " + "(" + locale.getLanguage() + ")");
				LOGGER.info("OldCourse: " + oldCourse.toString());
				LOGGER.info("NewCourse: " + newCourse.toString());
				/*Modificacion: el curso estaba registrado en la base de datos, pero se modifica.*/
				/*Set<String> properties = new HashSet<String>();
				properties.add("externalId");
				properties.add("title");
				properties.add("url");
				properties.add("start");
				properties.add("end");
				properties.add("information");
				properties.add("active");
				this.beanManager.mergeObjects(newCourse, oldCourse, properties);*/
				oldCourse.setTitle(newCourse.getTitle());
				oldCourse.setUrl(newCourse.getUrl());
				oldCourse.setStart(newCourse.getStart());
				oldCourse.setEnd(newCourse.getEnd());
				oldCourse.setInformation(newCourse.getInformation());
				oldCourse.setActive(true);
				if(oldCourse.isTranslated()) oldCourse.setUpdated(Calendar.getInstance(new Locale("es")).getTime());
				//newCourse.setId(oldCourse.getId());
				//newCourse.setCreated(oldCourse.getCreated());
				//newCourse.setUpdated(oldCourse.getUpdated());
				this.entities.add(oldCourse);
				LOGGER.info("Se guarda este: (" + locale.getLanguage() + ")" + oldCourse.toString());
			} else {
				LOGGER.info("Curso existente y NO MODIFICADO ID:" + oldCourse.getId());
			}
			
		}

	@Override
	protected void flush(Locale locale) throws Exception  {
		courseService.save(this.entities, locale);
	}
	
}