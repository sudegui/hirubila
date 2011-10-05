package com.m4f.business.service.extended.ifc;

import java.util.List;
import java.util.Locale;

import com.m4f.business.domain.extended.ExtendedSchool;

public interface I18nExtendedSchoolService {
	ExtendedSchool createSchool();
	List<ExtendedSchool> getAllSchools(String ordering, Locale locale) throws Exception;
	void save(ExtendedSchool school, Locale locale) throws Exception;
	ExtendedSchool getSchool(Long id, Locale locale) throws Exception;
	void delete(ExtendedSchool school, Locale locale) throws Exception;
	void deleteAll(Locale locale) throws Exception;
	void erasure() throws Exception;
	void updateSchools(List<ExtendedSchool> newSchools, Locale locale) throws Exception;
	List<ExtendedSchool> getSchools(String ordering, int init, int end, Locale locale) throws Exception;
	long countSchools() throws Exception;
	List<ExtendedSchool> getSchoolsByTown(Long townId, String ordering, Locale locale) throws Exception;
	
	long countSchoolsByTerritorial(Long provinceId, Long regionId, Long townId) throws Exception;
	List<ExtendedSchool> getSchoolsByTerritorial(Long provinceId, Long regionId, Long townId, String ordering, int init, int end, Locale locale) throws Exception;
}