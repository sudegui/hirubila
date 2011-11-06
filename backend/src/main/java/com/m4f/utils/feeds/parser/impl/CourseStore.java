package com.m4f.utils.feeds.parser.impl;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Collection;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import com.m4f.business.domain.Course;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.School;
import com.m4f.business.service.ifc.I18nCourseService;
import com.m4f.utils.beans.BeanManager;
import com.m4f.utils.beans.exception.NotSameClassException;
import com.m4f.utils.feeds.parser.ifc.IStorage;

public class CourseStore extends StoreBase<Course> implements IStorage<Course> {
	
	@Override
	public Map<Course , List<FieldError>> store(Collection<Course> courses, 
			Locale locale, Provider provider) throws Exception {
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
		return executions;
	}
	
	private void selectiveCourseStore(Course newCourse, Locale locale) 
			throws NotSameClassException, Exception {
			Course oldCourse = this.courseService.getCourseByExternalId(newCourse.getExternalId(), locale);
			if(oldCourse == null) {
				/*Alta nueva: el curso no estaba registrado en la base de datos.*/
				newCourse.setCreated(Calendar.getInstance(new Locale("es")).getTime());
				newCourse.setActive(true);
				this.entities.add(newCourse);
			} else if(!oldCourse.equals(newCourse) || !oldCourse.isTranslated()) {
				/*Modificacion: el curso estaba registrado en la base de datos, pero se modifica.*/
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
				this.entities.add(oldCourse);
			}
			
		}

	@Override
	protected void flush(Locale locale) throws Exception  {
		courseService.save(entities, locale);
	}
	
}