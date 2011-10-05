package com.m4f.business.service.extended.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import com.m4f.business.domain.extended.ExtendedSchool;
import com.m4f.business.domain.extended.Town;
import com.m4f.business.service.extended.ifc.I18nExtendedCourseService;
import com.m4f.business.service.extended.ifc.I18nExtendedSchoolService;
import com.m4f.business.service.ifc.I18nTerritorialService;
import com.m4f.business.service.impl.I18nDAOBaseService;
import com.m4f.utils.i18n.dao.ifc.I18nDAOSupport;

public class ExtendedSchoolServiceImpl extends I18nDAOBaseService implements I18nExtendedSchoolService {
	
	private static final Logger LOGGER = Logger.getLogger(ExtendedSchoolServiceImpl.class.getName());
	
	I18nExtendedCourseService courseService;
	I18nTerritorialService territorialService;
	
	public ExtendedSchoolServiceImpl(I18nDAOSupport dao, I18nExtendedCourseService courseService, 
			I18nTerritorialService territorialService) {
		super(dao);
		this.courseService = courseService;
		this.territorialService = territorialService;
	}

	@Override
	public ExtendedSchool createSchool() {
		return (ExtendedSchool) this.DAO.createInstance(ExtendedSchool.class);
	}
	
	@Override
	public long countSchools() throws Exception {
		return this.DAO.count(ExtendedSchool.class);
	}

	@Override
	public void delete(ExtendedSchool school, Locale locale) throws Exception {
		this.courseService.deleteLogicBySchool(school.getId(), locale);
		this.DAO.delete(school, locale);		
	}

	@Override
	public void deleteAll(Locale locale) throws Exception {
		this.DAO.delete(this.DAO.findAll(ExtendedSchool.class, locale, null), locale);
	}

	@Override
	public void erasure() throws Exception {
		this.DAO.erasure(ExtendedSchool.class);
	}

	@Override
	public List<ExtendedSchool> getAllSchools(String order, Locale locale) throws Exception {
		return this.DAO.findAll(ExtendedSchool.class, locale, null);
	}

	@Override
	public List<ExtendedSchool> getSchools(String ordering, int init, int end, Locale locale)
			throws Exception {
		List<ExtendedSchool> schools = new ArrayList<ExtendedSchool>();
		schools.addAll(this.DAO.findEntitiesByRange(ExtendedSchool.class, 
				locale,init, end, ordering));
		return schools;
	}

	@Override
	public ExtendedSchool getSchool(Long id, Locale locale) throws Exception {
		return this.DAO.findById(ExtendedSchool.class, locale, id);
	}

	@Override
	public void save(ExtendedSchool school, Locale locale) throws Exception {
		this.DAO.saveOrUpdate(school, locale);		
	}

	@Override
	public void updateSchools(List<ExtendedSchool> newSchools, Locale locale)
			throws Exception {
		Map<Long, ExtendedSchool> indexedSchools = this.getSchools(null, locale);
		ExtendedSchool school;
		for(ExtendedSchool newSchool : newSchools) {
			if(indexedSchools.containsKey(newSchool.getId())) {
				school = indexedSchools.get(newSchool.getId());
				indexedSchools.remove(newSchool.getId());
				// TODO Copy newSchool into school
			} else {
				school = newSchool;
			}
			try {
				this.DAO.saveOrUpdate(school, locale);
			} catch(Exception e) {
				
			}
		}
		
	}
	@Override
	public List<ExtendedSchool> getSchoolsByTown(Long townId, String ordering, Locale locale) throws Exception {
		List<ExtendedSchool> schools = new ArrayList<ExtendedSchool>();
		String filter = "town == townParam";
		String params = "java.lang.Long townParam";
		schools.addAll(this.DAO.findEntities(ExtendedSchool.class, locale, filter, params, new Object[] {townId}, ordering));
		
		return schools;
	}
	
	@Override
	public long countSchoolsByTerritorial(Long provinceId, Long regionId, Long townId) throws Exception {
		long count = 0;
		if(townId != null) {
			ArrayList<Long> ids = new ArrayList<Long>();
			ids.add(townId);
			count = this.DAO.countByIds(ExtendedSchool.class, "town", ids);
		} else if(regionId != null) {
			Collection<Town> towns = this.territorialService.getTownsByRegion(regionId, 
					Locale.getDefault(),null);
			List<Long> ids = new ArrayList<Long>();
			for(Town town : towns) ids.add(town.getId());
			count = this.DAO.countByIds(ExtendedSchool.class, "town", ids);
		} else if(provinceId != null) {
			Collection<Town> towns = this.territorialService.getTownsByProvince(provinceId, Locale.getDefault());
			List<Long> ids = new ArrayList<Long>();
			for(Town town : towns) ids.add(town.getId());
			count = this.DAO.countByIds(ExtendedSchool.class, "town", ids);
		} else {
			count = this.countSchools();
		}
		
		return count;
	}
	
	@Override
	public List<ExtendedSchool> getSchoolsByTerritorial(Long provinceId, Long regionId, Long townId, String ordering, int init, int end, Locale locale) throws Exception {
		List<ExtendedSchool> schools = new ArrayList<ExtendedSchool>();
		if(townId != null) {
			String filter = "town == townParam";
			String params = "java.lang.Long townParam";
			schools.addAll(this.DAO.findEntities(ExtendedSchool.class, locale, filter, params, new Object[] {townId}, ""));
		} else if(regionId != null) {
			Collection<Town> towns = this.territorialService.getTownsByRegion(regionId, locale, null);
			List<Long> ids = new ArrayList<Long>();
			for(Town town : towns) ids.add(town.getId());
			schools.addAll(this.DAO.findEntitiesByIds(ExtendedSchool.class, locale, "town", ids, init, end, ordering));
		} else if(provinceId != null) {
			Collection<Town> towns = this.territorialService.getTownsByProvince(provinceId, locale);
			List<Long> ids = new ArrayList<Long>();
			for(Town town : towns) ids.add(town.getId());
			schools.addAll(this.DAO.findEntitiesByIds(ExtendedSchool.class, locale, "town", ids, init, end, ordering));
		} else {
			schools.addAll(this.getSchools(ordering, init, end, locale));
		}
		
		return schools;
	}

	/*********************************************************************
	 * 						PRIVATE METHODS
	 ********************************************************************/
	
	/*********************************************************************
	 * 
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	private Map<Long, ExtendedSchool> getSchools(String ordering, Locale locale) throws Exception {
		Map<Long, ExtendedSchool> indexedSchools = new HashMap<Long, ExtendedSchool>();
		List<ExtendedSchool> schools = this.DAO.findAll(ExtendedSchool.class, locale, ordering);
		for(ExtendedSchool school : schools) {
			indexedSchools.put(school.getId(), school);
		}
		return indexedSchools;
	}
}