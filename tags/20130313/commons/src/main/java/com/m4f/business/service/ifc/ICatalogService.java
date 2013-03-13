package com.m4f.business.service.ifc;

import java.util.Collection;
import java.util.Locale;
import com.m4f.business.domain.CourseCatalog;

public interface ICatalogService {
	
	// COURSECATALOG
	CourseCatalog createCourseCatalog();
	void save(CourseCatalog courseCatalog) throws Exception;
	void save(Collection<CourseCatalog> courses) throws Exception;
	void delete(Collection<CourseCatalog> courses) throws Exception;
	void delete(CourseCatalog course) throws Exception;
	void deleteCourseCatalogByCourseId(Long courseId) throws Exception;
	CourseCatalog getCourseCatalogById(Long id) throws Exception;
	CourseCatalog getCourseCatalogByCourseId(Long courseId, Locale locale) throws Exception;
	long countCourseCatalog(Locale locale) throws Exception;
	long countCourseCatalog(boolean reglated, Locale locale) throws Exception;
	Collection<CourseCatalog> getCoursesCatalog(String ordering, Locale locale, int init, int end);
	Collection<CourseCatalog> getCoursesCatalog(boolean reglated, String ordering, Locale locale, int init, int end);
	
}