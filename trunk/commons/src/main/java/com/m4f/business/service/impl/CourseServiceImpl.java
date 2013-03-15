package com.m4f.business.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import com.google.appengine.api.datastore.Category;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.m4f.business.domain.Course;
import com.m4f.business.domain.CourseCatalog;
import com.m4f.business.domain.School;
import com.m4f.business.service.ifc.I18nCourseService;
import com.m4f.utils.cache.annotations.Cacheable;
import com.m4f.utils.cache.annotations.Cacheflush;
import com.m4f.utils.cache.annotations.CatalogCacheable;
import com.m4f.utils.dao.GaeFilter;
import com.m4f.utils.i18n.dao.ifc.I18nDAOSupport;

public class CourseServiceImpl extends I18nDAOBaseService implements I18nCourseService {
	
	private static final Logger LOGGER = Logger.getLogger(CourseServiceImpl.class.getName());
	
	public CourseServiceImpl(I18nDAOSupport dao) {
		super(dao);
	}

	@Override
	public Course createCourse() {
		return this.DAO.createInstance(Course.class);
	}

	@Override
	@Cacheflush(cacheName="courses")
	public void delete(Course course, Locale locale) throws Exception {
		this.DAO.delete(course, locale);		
	}
	
	@Override
	@Cacheflush(cacheName="courses")
	public void delete(Collection<Course> courses, Locale locale) throws Exception {
		for(Course course : courses) {
			this.DAO.delete(course, locale);
		}
	}

	@Override
	@Cacheable(cacheName="courses")
	public Collection<Course> getAllCourses(String ordering, Locale locale) throws Exception {
		return this.DAO.findAll(Course.class, locale, ordering);
	}

	@Override
	//@Cacheable(cacheName="courses")
	@CatalogCacheable(cacheName="coursesCatalog")
	public Course getCourse(Long id, Locale locale) throws Exception {
		return this.DAO.findById(Course.class, locale, id);
	}

	@Override
	@Cacheflush(cacheName="courses")
	public void save(Course course, Locale locale) throws Exception {
		this.DAO.saveOrUpdate(course, locale);
	}
	
	@Override
	@Cacheflush(cacheName="courses")
	public void save(Collection<Course> courses, Locale locale) throws Exception {
		this.DAO.saveOrUpdateCollection(courses, locale);
	}
	
	@Override
	@Cacheflush(cacheName="courses")
	public void erasure() throws Exception {
		this.DAO.erasure(Course.class);
	}
	
	@Override
	@Cacheflush(cacheName="courses")
	public void deleteLogic(Course course, Locale locale) throws Exception {
		course.setActive(false);
		this.DAO.saveOrUpdate(course, locale);
	}
	
	@Override
	@Cacheflush(cacheName="courses")
	public void deleteLogicBySchool(Long schoolId, Locale locale) throws Exception {
		Collection<Course> courses = this.getCoursesBySchool(schoolId, null, locale);
		for(Course course : courses) {
			this.deleteLogic(course, locale);
		}
		
	}

	@Override
	@Cacheable(cacheName="courses")
	public Collection<Course> getActiveCourses(String ordering, Locale locale) throws Exception {
		return this.DAO.findEntities(Course.class, locale, "active == activeParam", 
				"Boolean activeParam", new Boolean[] {Boolean.TRUE}, ordering);
	}
	
	@Override
	@Cacheable(cacheName="courses")
	public Collection<Course> getNewCourses(String ordering, Locale locale, int init, int end ) {
		return this.DAO.findEntitiesByRange(Course.class, locale, "updated == null && active == activeParam", 
				"Boolean activeParam", new Boolean[] {Boolean.TRUE}, init, end,ordering);
	}
	
	@Override
	@Cacheable(cacheName="courses")
	public long countNewCourses() {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("updated", null);
		filter.put("active", Boolean.TRUE);
		return this.DAO.count(Course.class, filter);
	}
	
	@Override
	@CatalogCacheable(cacheName="coursesCatalog")
	public Collection<Course> getUpdatedCourses(Date from, Boolean reglated, String ordering, Locale locale, int init, int end) {
		if(from != null) { 
			return this.DAO.findEntitiesByRange(Course.class, locale, "updated >= fromDate && regulated == reglatedParam && active == activeParam", 
				"java.util.Date fromDate,  java.lang.Boolean reglatedParam, java.lang.Boolean activeParam", new Object[] {from, reglated, Boolean.TRUE}, init, end, ordering);
		} else {
			return this.DAO.findEntitiesByRange(Course.class, locale, "regulated == reglatedParam && active == activeParam", 
					"java.lang.Boolean reglatedParam, java.lang.Boolean activeParam", new Object[] {reglated, Boolean.TRUE}, init, end, ordering);
		}
	}
	
	@Override
	@CatalogCacheable(cacheName="coursesCatalog")
	public long countUpdatedCourses(Date from, Boolean reglated) {
		ArrayList<GaeFilter> filters = new ArrayList<GaeFilter>();
		if(from != null) {
			GaeFilter filter = new GaeFilter("updated", FilterOperator.GREATER_THAN_OR_EQUAL, from);
			filters.add(filter);
		}
		
		if(reglated != null) {
			GaeFilter filter = new GaeFilter("regulated", FilterOperator.EQUAL, reglated);
			filters.add(filter);
		}
		
		return this.DAO.count(Course.class, filters);
	}
	
	@Override
	@CatalogCacheable(cacheName="coursesCatalog")
	public long countTESTnoUPDATED() {
		ArrayList<GaeFilter> filters = new ArrayList<GaeFilter>();
		
		GaeFilter filter = new GaeFilter("updated", FilterOperator.EQUAL, null);
		
		filters.add(filter);
				
		return this.DAO.count(Course.class, filters);
	}
	
	@Override
	@CatalogCacheable(cacheName="coursesCatalog")
	public Collection<Course> getCoursesNoUPDATED(int init, int end) {
		return this.DAO.findEntitiesByRange(Course.class, null, "updated == null", "", new Object[]{}, init, end, null);
	}
	
	
	@Override
	@Cacheable(cacheName="courses")
	public Collection<Course> getCoursesBySchool(Long schoolId, String ordering, Locale locale) {
		return this.findCoursesBySchoolId(schoolId, ordering, locale);
	}
	
	@Override
	@Cacheable(cacheName="courses")
	public Collection<Course> getCoursesBySchool(School school, String ordering, Locale locale) {
		return this.findCoursesBySchoolId(school.getId(), ordering, locale);
	}
	
	@Override
	@Cacheable(cacheName="courses")
	public Course getCourseByExternalId(String externalId, Locale locale) {
		String filter = "externalId == externalIdParam";
		String params = "java.lang.String externalIdParam";
		Course course = this.DAO.findEntity(Course.class, locale, filter, 
				params, new Object[] {externalId});
		return course;
	}
	
	@Override
	@Cacheable(cacheName="courses")
	public Collection<Course> getCoursesByExternalId(String externalId, Locale locale) {
		String filter = "externalId == externalIdParam";
		String params = "java.lang.String externalIdParam";
		Collection<Course> course = this.DAO.findEntities(Course.class, locale, filter, 
				params, new Object[] {externalId}, "id");
		return course;
	}
	
	@Override
	@Cacheable(cacheName="courses")
	public long count() throws Exception {
/*		Map<String, Object> filter = new HashMap<String, Object>();

		filter.put("regulated", Boolean.FALSE);
		filter.put("regulated", Boolean.TRUE);
		return this.DAO.count(Course.class, filter);
*/		
		long numReglated = this.countCourses(Boolean.TRUE, null);
		long numNoReglated = this.countCourses(Boolean.FALSE, null);
		return numReglated + numNoReglated; 
		//return this.DAO.count(Course.class);
	}
	
	@Override
	@Cacheable(cacheName="courses")
	public long countCoursesBySchool(School school) throws Exception {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("school", school.getId());
		return this.DAO.count(Course.class, filter);
	}

	@Override
	@Cacheable(cacheName="courses")
	public Collection<Course> getCourses(String ordering, Locale locale, int init, int end)
			throws Exception {
		String order = "regulated";
		if(ordering != null && !("").equals(ordering)) {
			order += ", " + ordering;
		}
		return this.DAO.findEntitiesByRange(Course.class, locale, "regulated != null", "", new Object[]{}, init, end, order);
	}

	@Override
	@Cacheable(cacheName="courses")
	public Collection<Course> getCoursesBySchool(Long schoolId, String ordering,
			Locale locale, int init, int end) {
		return this.findCoursesBySchoolId(schoolId, ordering, locale, init, end);
	}
	
	@Override
	@Cacheable(cacheName="courses")
	public Collection<Course> getCoursesBySchool(School school, String ordering,
			Locale locale, int init, int end) {
		return this.findCoursesBySchoolId(school.getId(), ordering, locale, init, end);
	}
	
	@Override
	@Cacheable(cacheName="courses")
	public long countCoursesByProvider(Long providerId) throws Exception {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("provider", providerId);
		return this.DAO.count(Course.class, filter);
	}
	
	@Override
	@Cacheable(cacheName="courses")
	public Collection<Course> getCoursesByProvider(Long providerId, String ordering,
			Locale locale, int init, int end) {
		return this.findCoursesByProviderlId(providerId, ordering, locale, init, end);
	}
	
	@Override
	@Cacheable(cacheName="courses")
	public Collection<Course> getCoursesByProvider(Long providerId, String ordering, Locale locale) {
		return this.findCoursesByProviderlId(providerId, ordering, locale);
	}
	
	@Override
	@Cacheable(cacheName="courses")
	public List<Category> getCoursesTags(Locale locale) {
		HashMap<String, Category> mapa = new HashMap<String, Category>();
		Collection<Category> collection = this.DAO.getCategories(Course.class, "tags", locale);
		Iterator it = collection.iterator();
		while (it.hasNext()) {
			Set set = (Set) it.next();
			Object[] array = set.toArray();
			for (Object o : array) {
				if (o instanceof Category) {
					Category c = (Category) o;
					mapa.put(c.getCategory(), c);
				}
			}
		}
		return new ArrayList<Category>(mapa.values());
	}
	
	/**************************************************************************
	 * 
	 *							PRIVATE METHODS
	 *
	 **************************************************************************/
	
	
	private Collection<Course> findCoursesBySchoolId(Long schoolId, String ordering,
			Locale locale) {
		return this.DAO.findEntities(Course.class, locale, 
				"school == schoolParam", 
				"Long schoolParam", 
				new Object[] {schoolId}, ordering);
	}
	
	private Collection<Course> findCoursesBySchoolId(Long schoolId, String ordering,
			Locale locale, int init, int end) {
		String filter = "school == schoolParam";
		String params = "java.lang.Long schoolParam";
		return this.DAO.findEntitiesByRange(Course.class, locale, filter, 
				params, new Long[] {schoolId}, init, end, ordering);
	}	
	
	private Collection<Course> findCoursesByProviderlId(Long providerId, String ordering,
			Locale locale, int init, int end) {
		String filter = "provider == providerParam";
		String params = "java.lang.Long providerParam";
		return this.DAO.findEntitiesByRange(Course.class, locale, filter, 
				params, new Long[] {providerId}, init, end, ordering);
	}
	
	private Collection<Course> findCoursesByProviderlId(Long providerId, String ordering,
			Locale locale) {
		String filter = "provider == providerParam";
		String params = "java.lang.Long providerParam";
		return this.DAO.findEntities(Course.class, locale, filter, 
				params, new Long[] {providerId}, ordering);
	}
	
	
	@Override
	@Cacheable(cacheName="coursesCatalog")
	public long countCourses(boolean reglated, Locale locale) throws Exception {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("regulated", reglated);
		return this.DAO.count(Course.class, filter);
	}
	
	@Override
	//@CatalogCacheable(cacheName="coursesCatalog")
	@CatalogCacheable(cacheName="coursesCatalog")
	public Collection<Course> getCourses(boolean reglated,
			String ordering, Locale locale, int init, int end) {
		String filter = "regulated == reglatedParam"; 
		String params = "java.lang.Boolean reglatedParam";
		
		return this.DAO.findEntitiesByRange(Course.class, locale, filter, 
				params, new Object[] {reglated}, init, end, ordering);
	}
}