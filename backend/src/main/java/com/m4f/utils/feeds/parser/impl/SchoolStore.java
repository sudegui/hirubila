package com.m4f.utils.feeds.parser.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Collection;
import java.util.Set;
import java.util.logging.Logger;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;

import com.m4f.business.domain.Course;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.School;
import com.m4f.utils.beans.exception.NotSameClassException;
import com.m4f.utils.feeds.parser.ifc.ISchoolStorage;

public class SchoolStore extends StoreBase<School> implements ISchoolStorage {
	
	private static final Logger LOGGER = Logger.getLogger(SchoolStore.class.getName());
	private List<School> entities  = new ArrayList<School>();
	
	private class SchoolComparator implements Comparator<School> {
		@Override
		public int compare(School o1, School o2) {
			if(o1.toString().equals(o2.toString())) return 0;
			return -1;
		}
		
	}
	
	@Override
	public Map<School, List<FieldError>> store(Collection<School> objs, 
			Locale locale, Provider provider) throws Exception {
		Map<School , List<FieldError>> executions = new HashMap<School , List<FieldError>>();
		for(School school : objs) {
			DataBinder dataBinder = new DataBinder(school);
			dataBinder.setValidator(validator);
			dataBinder.validate();
			executions.put(school, dataBinder.getBindingResult().getFieldErrors());
			if(!dataBinder.getBindingResult().hasErrors()) {
				this.selectiveSchoolStore(school, locale);
			}
		}
		this.flush(locale);
		return executions;
	}
	
	private void selectiveSchoolStore(School newSchool, Locale locale) 
			throws NotSameClassException, Exception {	
			newSchool.setCreated(Calendar.getInstance(new Locale("es")).getTime());
			newSchool.setUpdated(newSchool.getCreated());
			School oldSchool;
			oldSchool = this.schoolService.getSchoolByExternalId(newSchool.getExternalId(), locale);
			if(oldSchool == null) {
				// Creacion de un centro nuevo.
				LOGGER.info("Adding new school: " + newSchool.getName());
				this.entities.add(newSchool);
			} else {
				// Actualizacion de un centro existente.
				// Logica de actualizacion
				SchoolComparator comparator = new SchoolComparator();
				if(comparator.compare(newSchool, oldSchool) == 0) {
					LOGGER.info("Existing school but NOT MODIFIED");
					return;
				}
				LOGGER.severe("Existing school but MODIFIED");
				Set<String> properties = new HashSet<String>();
				properties.add("telephone");
				properties.add("fax");
				properties.add("zipCode");
				properties.add("webSite");
				properties.add("streetAddress");
				properties.add("city");
				properties.add("email");
				this.beanManager.mergeObjects(newSchool.getContactInfo(), 
						oldSchool.getContactInfo(), properties);
				properties = new HashSet<String>();
				properties.add("externalId");
				properties.add("name");
				properties.add("feed");
				this.beanManager.mergeObjects(newSchool, oldSchool, properties);
				//newSchool.setId(oldSchool.getId());
				//newSchool.setCreated(oldSchool.getCreated());
				oldSchool.setUpdated(Calendar.getInstance(new Locale("es")).getTime());
				//newSchool.setUpdated(oldSchool.getUpdated());
				this.entities.add(oldSchool);
			}
		}
	
	@Override
	protected void flush(Locale locale) throws Exception {
		schoolService.save(entities, locale);
	}

}