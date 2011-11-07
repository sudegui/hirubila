package com.m4f.utils.feeds.parser.ifc;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.validation.FieldError;
import com.m4f.business.domain.Course;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.School;

public interface ICourseStorage {
	
	Map<Course , List<FieldError>> store(Collection<Course> courses, Locale locale, School school, Provider provider) throws Exception;
}
