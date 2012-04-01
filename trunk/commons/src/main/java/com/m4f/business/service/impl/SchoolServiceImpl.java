package com.m4f.business.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import com.m4f.business.domain.Provider;
import com.m4f.business.domain.School;
import com.m4f.business.service.ifc.I18nCourseService;
import com.m4f.business.service.ifc.I18nSchoolService;
import com.m4f.utils.cache.annotations.Cacheable;
import com.m4f.utils.cache.annotations.Cacheflush;
import com.m4f.utils.i18n.dao.ifc.I18nDAOSupport;
import com.m4f.utils.StackTraceUtil;

public class SchoolServiceImpl extends I18nDAOBaseService implements I18nSchoolService {
	
	private static final Logger LOGGER = Logger.getLogger(SchoolServiceImpl.class.getName());
	
	I18nCourseService courseService;
	
	public SchoolServiceImpl(I18nDAOSupport dao, I18nCourseService courseService) {
		super(dao);
		this.courseService = courseService;
	}
		
	@Override
	public School createSchool() {
		return (School) this.DAO.createInstance(School.class);
	}
		
	@Override
	public List<School> getAllSchools(String ordering, Locale locale) throws Exception {
		return this.DAO.findAll(School.class, locale, ordering);
	}

	@Override
	@Cacheflush(cacheName="schools")
	public void save(School school, Locale locale) throws Exception {
		this.DAO.saveOrUpdate(school, locale);
	}
	
	@Override
	@Cacheflush(cacheName="schools")
	public void save(Collection<School> schools, Locale locale) throws Exception {
		this.DAO.saveOrUpdateCollection(schools, locale);
	}
	
	@Override
	@Cacheable(cacheName="schools")
	public School getSchool(Long id, Locale locale) throws Exception {
		return this.DAO.findById(School.class, locale, id);
	}
	
	@Override
	@Cacheflush(cacheName="schools")
	public void delete(School school, Locale locale) throws Exception {		
		this.courseService.deleteLogicBySchool(school.getId(), locale);
		this.DAO.delete(school, locale);
	}
	
	@Override
	@Cacheflush(cacheName="schools")
	public void deleteAll(Locale locale) throws Exception {
		this.DAO.delete(this.DAO.findAll(School.class,locale, null), locale);
	}
	
	@Override
	@Cacheflush(cacheName="schools")
	public void erasure() throws Exception {
		this.DAO.erasure(School.class);
	}
	
	@Override
	@Cacheflush(cacheName="schools")
	public void updateSchools(List<School> newSchools, Locale locale)  throws Exception {
		Map<Long, School> indexedSchools = this.getSchools(locale);
		School school;
		for(School newSchool : newSchools) {
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
				LOGGER.severe(StackTraceUtil.getStackTrace(e));
			}
		}
	}		
	
	
	@Override
	@Cacheable(cacheName="schools")
	public List<School> getSchoolsByProvider(Long providerId, String ordering,
			Locale locale, int init, int end) throws Exception {
		List<School> schools = new ArrayList<School>();
		String filter = "provider == providerParam";
		String params = "java.lang.Long providerParam";
		schools.addAll(this.DAO.findEntitiesByRange(School.class, locale, filter, 
				params, new Long[] {providerId}, init, end, ordering));
		return schools;
	}
	
	@Override
	@Cacheable(cacheName="schools")
	public List<School> getSchoolsByProvider(Long providerId, String ordering,
			Locale locale) throws Exception {
		List<School> schools = new ArrayList<School>();
		String filter = "provider == providerParam";
		String params = "java.lang.Long providerParam";
		schools.addAll(this.DAO.findEntities(School.class, locale, filter, 
				params, new Long[] {providerId}, ordering));
		return schools;
	}
	
	@Override
	@Cacheable(cacheName="schools")
	public School getSchoolByExternalId(String externalId, Locale locale) {
		String filter = "externalId == externalIdParam";
		String params = "java.lang.String externalIdParam";
		School school = this.DAO.findEntity(School.class, locale, filter, 
				params, new Object[] {externalId});
		return school;
	}
	
	@Override
	@Cacheable(cacheName="schools")
	public List<School> getSchools(String ordering, Locale locale, int init, 
			int end) throws Exception {
		List<School> schools = new ArrayList<School>();
		schools.addAll(this.DAO.findEntitiesByRange(School.class, 
				locale,init, end, ordering));
		return schools;
	}
	
	@Override
	@Cacheable(cacheName="schools")
	public long countSchools() throws Exception {
		return this.DAO.count(School.class);
	}
	
	@Override
	@Cacheable(cacheName="schools")
	public long countSchoolsByProvider(Long providerId) throws Exception {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("provider", providerId);
		return this.DAO.count(School.class, filter);
	}
	
	@Override
	@Cacheable(cacheName="schools")
	public List<School> getSchoolsByProvider(Long providerId, String name, String ordering, Locale locale) {
		ArrayList<School> schools = new ArrayList<School>();
		String hack = name + "\ufffd";
		String filter = "provider == providerParam && name >= nameParam && name < hackParam";
		String params = "java.lang.Long providerParam, java.lang.String nameParam, java.lang.String hackParam";
		schools.addAll(this.DAO.findEntities(School.class, locale, filter, 
				params, new Object[] {providerId, name, hack}, ordering));
		
		return schools;
	}
	
	@Override
	@Cacheable(cacheName="schools")
	public List<School> findByName(String name, Locale locale) {
		ArrayList<School> schools = new ArrayList<School>();
		String filter = "search.contains(nameParam)";
		schools.addAll(this.DAO.findEntities(School.class, locale, filter, "java.lang.String nameParam", new Object[]{name}, "name"));
		return schools;
	}
	
	@Override
	@Cacheable(cacheName="schools")
	public List<Long> getAllSchoolIds() throws Exception {
		return this.DAO.getAllIds(School.class, "id",null, null, null);
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
	private Map<Long, School> getSchools(Locale locale) throws Exception {
		Map<Long, School> indexedSchools = new HashMap<Long, School>();
		List<School> schools = this.DAO.findAll(School.class, locale, null);
		for(School school : schools) {
			indexedSchools.put(school.getId(), school);
		}
		return indexedSchools;
	}
}