package com.m4f.web.controller.helper.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
import com.m4f.business.service.ifc.I18nCourseService;
import com.m4f.business.service.ifc.I18nProviderService;
import com.m4f.business.service.ifc.I18nSchoolService;
import com.m4f.business.service.ifc.IAppConfigurationService;
import com.m4f.business.service.ifc.ICatalogService;
import com.m4f.utils.seo.SeoCatalogBuilder;
import com.m4f.business.service.ifc.I18nTerritorialService;

public class HirubilaSeoCatalogBuilder implements SeoCatalogBuilder {

	private static final Logger LOGGER = Logger.getLogger(HirubilaSeoCatalogBuilder.class.getName());
	
	@Autowired
	protected I18nCourseService courseService;
	@Autowired
	protected I18nProviderService providerService;
	@Autowired
	protected I18nSchoolService schoolService;
	@Autowired
	protected I18nTerritorialService territorialService;
	@Autowired
	protected IAppConfigurationService configurationService;
	@Autowired
	protected ICatalogService catalogService;
	
	@Override
	public void buildSeoEntity(Long courseId, List<Locale> locales) 
		throws ServiceNotFoundException, ContextNotActiveException, Exception {		
		for(Locale locale : locales) {	
				LOGGER.info(new StringBuffer("Generacion de instancia de catalogo del curso id: ")
					.append(courseId).append(" y locale: ").append(locale).toString());
				Course course = courseService.getCourse(courseId, locale);
				this.buildSeoEntity(course, locale);
		}
	}
	
	@Override
	public void buildSeoEntity(Course course, Locale locale) 
		throws ServiceNotFoundException, ContextNotActiveException, Exception {
		
		School school = schoolService.getSchool(course.getSchool(), locale);
		Provider provider = providerService.getProviderById(course.getProvider(), locale);
		
		// Territorial data
		String townName = school.getContactInfo() != null && 
			school.getContactInfo().getCity() != null ? 
			school.getContactInfo().getCity() : "";
		
		List<Town> towns = territorialService.findTownsByName(townName, locale);
		
		Town town = new Town();
		Province province = new Province();
		Region region = new Region();
		if(towns != null && towns.size() > 0) {
			town = towns.get(0);
			region = this.getRegionsMap().get(locale.getLanguage()).get(town.getRegion());
			province = this.getProvincesMap().get(locale.getLanguage()).get(town.getProvince());
		}						
		CourseCatalog catalog = new CourseCatalog(course, locale.getLanguage(), 
			school, provider.getName(), province.getName(), region.getName(), town.getName());
		
		CourseCatalog catalogOld = catalogService.getCourseCatalogByCourseId(course.getId(), locale);
		if(catalogOld != null) {
			catalog.setId(catalogOld.getId());
		}
		
		LOGGER.info(new StringBuffer("Fin generacion instancia de catalago del curso: ")
			.append(course.getId()).append(" y locale: ").append(locale).toString());
		catalogService.save(catalog);

	}
	
	
	private Map<String, Map<Long, Region>> getRegionsMap() throws Exception {
		Map<String, Map<Long,Region>> regionsMap = new HashMap<String, Map<Long,Region>>();
		for(Locale locale : configurationService.getLocales()) {
			regionsMap.put(locale.getLanguage(), territorialService.getRegionsMap(locale));
		}
		return regionsMap;
	}
	
	private Map<String, Map<Long, Province>> getProvincesMap() throws Exception {
		Map<String, Map<Long,Province>> provincesMap = new HashMap<String, Map<Long,Province>>();	
		for(Locale locale : configurationService.getLocales()) {
			provincesMap.put(locale.getLanguage(), territorialService.getProvincesMap(locale));
		}
		return provincesMap;
	}

}