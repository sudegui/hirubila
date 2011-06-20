package com.m4f.business.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import com.m4f.business.domain.CourseCatalog;
import com.m4f.business.service.ifc.ICatalogService;
import com.m4f.utils.cache.annotations.Cacheable;
import com.m4f.utils.cache.annotations.Cacheflush;
import com.m4f.utils.i18n.dao.ifc.DAOSupport;

public class GaeJdoCatalogService extends DAOBaseService implements ICatalogService {
	
	public GaeJdoCatalogService(DAOSupport dao) {
		super(dao);
	}

	@Override
	public CourseCatalog createCourseCatalog() {
		return this.DAO.createInstance(CourseCatalog.class);
	}

	@Override
	@Cacheflush(cacheName="coursesCatalog")
	public void delete(CourseCatalog course) throws Exception {
		this.DAO.delete(course);
	}
	
	@Override
	@Cacheable(cacheName="coursesCatalog")
	public CourseCatalog getCourseCatalogById(Long id) throws Exception {
		return this.DAO.findById(CourseCatalog.class, id);
	}

	@Override
	@Cacheable(cacheName="coursesCatalog")
	public CourseCatalog getCourseCatalogByCourseId(Long courseId, Locale locale) throws Exception {		
		String filter = "courseId == courseIdParam && lang == langParam";
		String params = "java.lang.String courseIdParam, java.lang.String langParam";
		CourseCatalog course = this.DAO.findEntity(CourseCatalog.class, filter, 
				params, new Object[] {courseId, locale.getLanguage()});
		return course;
	}

	@Override
	@Cacheflush(cacheName="coursesCatalog")
	public void save(CourseCatalog courseCatalog) throws Exception {
		this.DAO.saveOrUpdate(courseCatalog);
	}
	
	@Override
	@Cacheable(cacheName="coursesCatalog")
	public long countCourseCatalog(Locale locale) throws Exception {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("lang", locale.getLanguage());
		return this.DAO.count(CourseCatalog.class, filter);
	}

	
	@Override
	@Cacheable(cacheName="coursesCatalog")
	public Collection<CourseCatalog> getCoursesCatalog(String ordering,
			Locale locale, int init, int end) {
		String filter = "lang == langParam"; 
		String params = "java.lang.String langParam";
		return this.DAO.findEntitiesByRange(CourseCatalog.class, filter, params, 
				new Object[]{locale.getLanguage()}, ordering, init, end);
	}
	
	@Override
	public long countCourseCatalog(boolean reglated, Locale locale)
			throws Exception {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("lang", locale.getLanguage());
		filter.put("regulated", reglated);
		return this.DAO.count(CourseCatalog.class, filter);
	}

	@Override
	public Collection<CourseCatalog> getCoursesCatalog(boolean reglated,
			String ordering, Locale locale, int init, int end) {
		String filter = "lang == langParam && regulated == reglatedParam"; 
		String params = "java.lang.String langParam, java.lang.Boolean reglatedParam";
		return this.DAO.findEntitiesByRange(CourseCatalog.class, filter, params, 
				new Object[]{locale.getLanguage(), reglated}, ordering, init, end);
	}
	
	@Override
	public void deleteCourseCatalogByCourseId(Long courseId)
			throws Exception {
		Collection<CourseCatalog> coursesCatalog = this.getAllByCourseId(courseId);
		for(CourseCatalog courseCatalog : coursesCatalog) {
			this.delete(courseCatalog);
		}
	}
	
	private Collection<CourseCatalog> getAllByCourseId(Long courseId) {
		String filter = "courseId == courseIdParam";
		String params = "java.lang.Long courseIdParam";
		Collection<CourseCatalog> coursesCatalog = this.DAO.findEntities(CourseCatalog.class, filter, 
				params, new Object[] {courseId}, null);
		return coursesCatalog;
	}
}