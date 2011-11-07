package com.m4f.utils.feeds.parser.aspect;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.FieldError;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.School;
import com.m4f.business.domain.Course;
import com.m4f.utils.StackTraceUtil;
import com.m4f.utils.seo.SeoCatalogBuilder;

@Aspect
public class StoreHypervisor {
	
	private static final Logger LOGGER = Logger.getLogger(StoreHypervisor.class.getName());
	@Autowired
	private SeoCatalogBuilder catalogBuilder;
	
	
	@Pointcut("execution(* com.m4f.utils.feeds.parser.ifc.ISchoolStorage.store(..)) && args(objs,locale,provider)")
	private void schoolsStoreOperation(Collection<School> objs, Locale locale, Provider provider) {}
	
	@Pointcut("execution(* com.m4f.utils.feeds.parser.ifc.ICourseStorage.store(..)) && args(objs,locale,school, provider)")
	private void coursesStoreOperation(Collection<Course> objs, Locale locale, 
			School school, Provider provider) {}
	
	@AfterReturning(pointcut ="schoolsStoreOperation(objs,locale,provider)", 
			argNames="objs,locale,provider,retVal", returning= "retVal")
	public void createCatalogRequest(Collection<School> objs, Locale locale, 
			Provider provider, Map<School, List<FieldError>> retVal) {
		System.out.println("Registering SchoolStoreHypervisor");
	}
	
	@AfterReturning(pointcut ="coursesStoreOperation(objs,locale,school,provider)", 
			argNames="objs,locale,school,provider,retVal", returning= "retVal")
	public void createCatalogEntries(Collection<Course> objs, Locale locale, School school, 
			Provider provider, Map<Course, List<FieldError>> retVal) {
		System.out.println("Registering CourseStoreHypervisor");
		List<Course> validCourses = new ArrayList<Course>();
		List<Course> problematicCourses = new ArrayList<Course>();
		for(Course course : retVal.keySet()) {
			if(retVal.get(course).size()==0) {
				validCourses.add(course);
			} else {
				problematicCourses.add(course);
			}
		}
		try {
			validCourses(validCourses, school, provider, locale);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
		}
	}
	
	private void validCourses(Collection<Course> courses, School school, 
			Provider provider, Locale locale) throws Exception {
		this.catalogBuilder.buildSeo(courses, school, provider, locale);
	}
	
	private void problematicCourses(Collection<Course> courses, Locale locale) {
		
	}
	
}