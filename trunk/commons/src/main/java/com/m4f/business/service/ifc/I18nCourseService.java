package com.m4f.business.service.ifc;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.google.appengine.api.datastore.Category;
import com.m4f.business.domain.Course;
import com.m4f.business.domain.CourseCatalog;
import com.m4f.business.domain.School;

public interface I18nCourseService {
	
	Course createCourse();
	Collection<Course> getActiveCourses(String ordering, Locale locale) throws Exception;
	Collection<Course> getAllCourses(String ordering, Locale locale) throws Exception;
	Collection<Course> getCourses(String ordering, Locale locale, int init, int end) throws Exception;
	void save(Course course, Locale locale) throws Exception;
	void save(Collection<Course> courses, Locale locale) throws Exception;
	Course getCourse(Long id, Locale locale) throws Exception;
	Course getCourseByExternalId(String externalId, Locale locale);
	void erasure() throws Exception;
	void deleteLogic(Course course, Locale locale) throws Exception;
	void delete(Collection<Course> courses, Locale locale) throws Exception;
	void delete(Course course, Locale locale) throws Exception;
	void deleteLogicBySchool(Long schoolId, Locale locale) throws Exception;
	Collection<Course> getNewCourses(String ordering, Locale locale, int init, int end);
	long countNewCourses();
	Collection<Course> getUpdatedCourses(Date from, String ordering, Locale locale, int init, int end);
	long countUpdatedCourses(Date from);
	Collection<Course> getCoursesBySchool(Long schoolId, String ordering, Locale locale);
	Collection<Course> getCoursesBySchool(School school, String ordering, Locale locale);
	Collection<Course> getCoursesBySchool(Long schoolId, String ordering, Locale locale, int init, int end);
	Collection<Course> getCoursesBySchool(School school, String ordering, Locale locale, int init, int end);
	Collection<Course> getCoursesByProvider(Long providerId, String ordering, Locale locale, int init, int end);
	Collection<Course> getCoursesByProvider(Long providerId, String ordering, Locale locale);
	long count() throws Exception;
	long countCoursesBySchool(School school) throws Exception;
	long countCoursesByProvider(Long providerId) throws Exception;
	List<Category> getCoursesTags(Locale locale) throws Exception;
}
