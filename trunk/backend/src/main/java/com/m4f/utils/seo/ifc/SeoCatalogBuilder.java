package com.m4f.utils.seo.ifc;

import java.util.List;
import java.util.Collection;
import java.util.Locale;
import com.m4f.business.domain.Course;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.School;

public interface SeoCatalogBuilder {
	void buildSeoEntity(Long courseId, List<Locale> locales) throws  Exception;
	void buildSeoEntity(Course course, Locale locale) throws  Exception;
	void buildSeo(Collection<Course> courses, School school, Provider provider, Locale locale) throws  Exception;
}