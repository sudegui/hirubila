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
import com.m4f.business.service.ifc.I18nCourseService;
import com.m4f.utils.PageManager;
import com.m4f.utils.StackTraceUtil;
import com.m4f.utils.seo.ifc.SeoCatalogBuilder;

@Aspect
public class StoreHypervisor {
	
	private static final Logger LOGGER = Logger.getLogger(StoreHypervisor.class.getName());
	@Autowired
	private SeoCatalogBuilder catalogBuilder;
	@Autowired
	protected I18nCourseService courseService;
	
	
	@Pointcut("execution(* com.m4f.utils.feeds.parser.ifc.ISchoolStorage.store(..)) && args(objs,locale,provider)")
	private void schoolsStoreOperation(Collection<School> objs, Locale locale, Provider provider) {}
	
	@Pointcut("execution(* com.m4f.utils.feeds.parser.ifc.ICourseStorage.store(..)) && args(objs,locale,school, provider)")
	private void coursesStoreOperation(Collection<Course> objs, Locale locale, 
			School school, Provider provider) {}
	
	@AfterReturning(pointcut ="schoolsStoreOperation(objs,locale,provider)", 
			argNames="objs,locale,provider,retVal", returning= "retVal")
	public void createCatalogRequest(Collection<School> objs, Locale locale, 
			Provider provider, Map<School, List<FieldError>> retVal) {
		List<School> validSchools = new ArrayList<School>();
		List<School> problematicSchools = new ArrayList<School>();
		for(School school : retVal.keySet()) {
			if(retVal!=null && retVal.get(school).size()==0) {
				validSchools.add(school);
			} else {
				problematicSchools.add(school);
			}
		}
		try {
			processValidSchools(validSchools,provider,locale);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
		}
	}
	
	
	
	@AfterReturning(pointcut ="coursesStoreOperation(objs,locale,school,provider)", 
			argNames="objs,locale,school,provider,retVal", returning= "retVal")
	public void createCatalogEntries(Collection<Course> objs, Locale locale, School school, 
			Provider provider, Map<Course, List<FieldError>> retVal) {
		LOGGER.info("Registering CourseStoreHypervisor");
		List<Course> validCourses = new ArrayList<Course>();
		List<Course> problematicCourses = new ArrayList<Course>();
		for(Course course : retVal.keySet()) {
			if(retVal!=null && retVal.get(course).size()==0) {
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
	
	private void processValidSchools(Collection<School> schools, 
			Provider provider, Locale locale) throws Exception {
		for(School school : schools) {
			/*LOGGER.severe("School: " + school);
			LOGGER.severe("CreatedDate: " + school.getCreated() + 
					"-UpdatedDate: " + school.getUpdated());*/
			if(!school.getCreated().equals(school.getUpdated())) {
				LOGGER.info("Generar todo el catálogo del centro " + school.getName());		
				generateSchoolCatalog(provider, school, locale);
			}
		}
	}
	
	private void generateSchoolCatalog(Provider provider, School school, 
			Locale locale) throws Exception {
		final int RANGE = 300;
		PageManager<Course> paginator = new PageManager<Course>();
		long total = courseService.countCoursesBySchool(school);
		paginator.setOffset(RANGE);
		paginator.setStart(0);
		paginator.setSize(total);
		for (Integer page : paginator.getTotalPagesIterator()) {
			int start = (page - 1) * RANGE;
			int end = (page) * RANGE;
			Collection<Course> courses = courseService.getCourses("id", locale,
					start, end);
			this.catalogBuilder.buildSeo(courses, school, provider, locale);
		}
	}
	
	private void validCourses(Collection<Course> courses, School school, 
			Provider provider, Locale locale) throws Exception {
		this.catalogBuilder.buildSeo(courses, school, provider, locale);
	}
	
	private void problematicCourses(Collection<Course> courses, Locale locale) {
		
	}
	
}