package com.m4f.business.service.ifc;

import java.net.URL;
import java.util.Collection;
import java.util.Locale;
import com.m4f.business.domain.Course;
import com.m4f.business.domain.CourseCatalog;

public interface ICourseHtmlService {
	
	// COURSECATALOG
	CourseCatalog createCourseCatalog();
	void save(CourseCatalog courseCatalog) throws Exception;
	CourseCatalog getCourseCatalogById(Long id) throws Exception;
	CourseCatalog getCourseCatalogByCourseId(Long courseId, Locale locale) throws Exception;
	long countCourseCatalog(Locale locale) throws Exception;
	long countCourseCatalog(boolean reglated, Locale locale) throws Exception;
	Collection<CourseCatalog> getCoursesCatalog(String ordering, Locale locale, int init, int end);
	Collection<CourseCatalog> getCoursesCatalog(boolean reglated, String ordering, Locale locale, int init, int end);
	void deleteCourseCatalogByCourseId(Long courseId) throws Exception;
	//Collection<CourseCatalog> getCourses(Locale locale, String ordering, int init, int end) throws Exception;
	void delete(CourseCatalog course) throws Exception;
}