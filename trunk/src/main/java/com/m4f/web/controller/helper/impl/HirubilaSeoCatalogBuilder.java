package com.m4f.web.controller.helper.impl;

import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.m4f.business.domain.Course;
import com.m4f.business.domain.CourseCatalog;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.School;
import com.m4f.business.domain.extended.Province;
import com.m4f.business.domain.extended.Region;
import com.m4f.business.domain.extended.Town;
import com.m4f.business.service.exception.ContextNotActiveException;
import com.m4f.business.service.exception.ServiceNotFoundException;
import com.m4f.business.service.ifc.IServiceLocator;
import com.m4f.utils.seo.SeoCatalogBuilder;


public class HirubilaSeoCatalogBuilder implements SeoCatalogBuilder {

	
	private static final Logger LOGGER = Logger.getLogger(HirubilaSeoCatalogBuilder.class.getName());
	
	@Autowired
	protected IServiceLocator serviceLocator;
	
	
	
	@Override
	public void buildSeoEntity(Long courseId, List<Locale> locales) 
		throws ServiceNotFoundException, ContextNotActiveException, Exception {		
		for(Locale locale : locales) {	
				LOGGER.info(new StringBuffer("Generacion de instancia de catalogo del curso id: ")
					.append(courseId).append(" y locale: ").append(locale).toString());
				
				Course course = this.serviceLocator.getCourseService().getCourse(courseId, locale);
				School school = this.serviceLocator.getSchoolService().getSchool(course.getSchool(), locale);
				Provider provider = this.serviceLocator.getProviderService().getProviderById(course.getProvider(), locale);
				
				// Territorial data
				String townName = school.getContactInfo() != null && 
					school.getContactInfo().getCity() != null ? 
					school.getContactInfo().getCity() : "";
				
				List<Town> towns = this.serviceLocator.getTerritorialService().
					findTownsByName(townName, locale);
				
				Town town = new Town();
				Province province = new Province();
				Region region = new Region();
				if(towns != null && towns.size() > 0) {
					town = towns.get(0);
					region = this.serviceLocator.getTransversalService().getRegionsMap().get(locale.getLanguage()).get(town.getRegion());
					province = this.serviceLocator.getTransversalService().getProvincesMap().get(locale.getLanguage()).get(town.getProvince());
				}						
				CourseCatalog catalog = new CourseCatalog(course, locale.getLanguage(), 
					school, provider.getName(), province.getName(), region.getName(), town.getName());
				
				CourseCatalog catalogOld = 
					this.serviceLocator.getCatalogService().getCourseCatalogByCourseId(courseId, locale);
				
				if(catalogOld != null) {
					catalog.setId(catalogOld.getId());
				}
				
				LOGGER.info(new StringBuffer("Fin generacion instancia de catalago del curso: ")
					.append(courseId).append(" y locale: ").append(locale).toString());
				
				this.serviceLocator.getCatalogService().save(catalog);
			}
	}

}