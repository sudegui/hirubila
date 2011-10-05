package com.m4f.business.service.extended.ifc;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import com.google.appengine.api.datastore.Category;
import com.m4f.business.domain.extended.ExtendedCourse;
import com.m4f.business.domain.extended.ExtendedSchool;

public interface I18nExtendedCourseService {
	
	ExtendedCourse createCourse();
	Collection<ExtendedCourse> getActiveCourses(String ordering, Locale locale) throws Exception;
	Collection<ExtendedCourse> getAllCourses(String ordering, Locale locale) throws Exception;
	Collection<ExtendedCourse> getCourses(String ordering, Locale locale, int init, int end) throws Exception;
	void save(ExtendedCourse course, Locale locale) throws Exception;
	ExtendedCourse getCourse(Long id, Locale locale) throws Exception;
	void erasure() throws Exception;
	void deleteLogic(ExtendedCourse course, Locale locale) throws Exception;
	void delete(ExtendedCourse course, Locale locale) throws Exception;
	void deleteLogicBySchool(Long schoolId, Locale locale) throws Exception;
	Collection<ExtendedCourse> getNewCourses(String ordering, Locale locale);
	Collection<ExtendedCourse> getCoursesBySchool(Long schoolId, String ordering, Locale locale);
	Collection<ExtendedCourse> getCoursesBySchool(ExtendedSchool school, String ordering, Locale locale);
	Collection<ExtendedCourse> getCoursesBySchool(Long schoolId, String ordering, Locale locale, int init, int end);
	Collection<ExtendedCourse> getCoursesBySchool(ExtendedSchool school, String ordering, Locale locale, int init, int end);
	Collection<ExtendedCourse> getCoursesByOwner(Long mediationService, String ordering, Locale locale, int init, int end);
	Collection<ExtendedCourse> getCoursesByOwner(Long mediationService, String ordering, Locale locale);
	long countCourses() throws Exception;
	long countCoursesBySchool(ExtendedSchool school) throws Exception;
	long countCoursesByOwner(Long mediationService) throws Exception;
	List<Category> getCoursesTags(Locale locale) throws Exception;
	Collection<ExtendedCourse> getCoursesBySchoolByMediation(Long schoolId, Long mediationService, String ordering,
			Locale locale);
			
	long countCoursesByTowns(Long townId) throws Exception;
	Collection<ExtendedCourse> getCoursesByTowns(List<Long> townIds, Locale locale) throws Exception;
}
