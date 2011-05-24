package com.m4f.utils.feeds.parser.ifc;

import java.util.List;
import java.util.Locale;
import org.springframework.validation.FieldError;
import com.m4f.business.domain.Course;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.School;
import com.m4f.utils.feeds.events.model.Dump;

public interface DumperCapable {
	
	List<FieldError> dumpSchool(Dump dump, Provider provider, School school, Locale locale) throws Exception;
	List<FieldError> dumpCourse(Dump dump, Course course, School school, Provider provider,String province, String region, String town, Locale locale) throws Exception;
	
}