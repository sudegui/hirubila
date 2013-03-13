package com.m4f.business.service.ifc;

import java.util.List;
import java.util.Locale;
import java.util.Collection;
import com.m4f.business.domain.School;

public interface I18nSchoolService {
	School createSchool();
	List<School> getAllSchools(String ordering, Locale locale) throws Exception;
	void save(School school, Locale locale) throws Exception;
	void save(Collection<School> schools, Locale locale) throws Exception;
	School getSchool(Long id, Locale locale) throws Exception;
	School getSchoolByExternalId(String externalId, Locale locale);
	List<School> getSchoolsByProvider(Long providerId, String ordering, Locale locale, int init, int end) throws Exception;
	void delete(School school, Locale locale) throws Exception;
	void deleteAll(Locale locale) throws Exception;
	void erasure() throws Exception;
	void updateSchools(List<School> newSchools, Locale locale) throws Exception;
	List<School> getSchools(String ordering, Locale locale, int init, int end) throws Exception;
	long countSchools() throws Exception;
	long countSchoolsByProvider(Long providerId) throws Exception;
	List<School> getSchoolsByProvider(Long providerId, String name, String ordering, Locale locale) throws Exception;
	List<School> getSchoolsByProvider(Long providerId, String ordering, Locale locale) throws Exception;
	
	List<School> findByName(String name, Locale locale);
	
	List<Long> getAllSchoolIds(int init, int end) throws Exception;
}