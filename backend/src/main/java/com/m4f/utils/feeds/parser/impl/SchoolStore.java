package com.m4f.utils.feeds.parser.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Collection;
import java.util.Set;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.School;
import com.m4f.utils.beans.exception.NotSameClassException;
import com.m4f.utils.feeds.parser.ifc.IStorage;

public class SchoolStore extends StoreBase<School> implements IStorage<School> {
	
	@Override
	public Map<School, List<FieldError>> store(Collection<School> schools, Locale locale, 
			Provider provider) throws Exception {
		Map<School , List<FieldError>> executions = new HashMap<School , List<FieldError>>();
		for(School school : schools) {
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
			School oldSchool;
			oldSchool = this.schoolService.getSchoolByExternalId(newSchool.getExternalId(), locale);
			if(oldSchool == null) {
				// Creacion de un centro nuevo.
				newSchool.setCreated(Calendar.getInstance(new Locale("es")).getTime());
				newSchool.setUpdated(Calendar.getInstance(new Locale("es")).getTime());
				this.entities.add(newSchool);
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
				oldSchool.setUpdated(Calendar.getInstance(new Locale("es")).getTime());
				this.entities.add(oldSchool);
			}
		}
	
	@Override
	protected void flush(Locale locale) throws Exception {
		schoolService.save(entities, locale);

	}

}