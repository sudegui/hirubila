package com.m4f.web.controller.task;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.m4f.business.domain.Course;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.School;
import com.m4f.utils.StackTraceUtil;
import com.m4f.web.controller.BaseController;

@Controller
@RequestMapping("/task/management")
public class ManagementTask extends BaseController {
	private static final Logger LOGGER = Logger.getLogger(ManagementTask.class.getName());
	
	@RequestMapping(value="/delete/provider", method = RequestMethod.POST)
	public String taskDeleteSchoolsByProvider(@RequestParam(required=true) Long providerId, Locale locale) throws Exception {
		try {
			this.deleteSchoolsByProvider(providerId, locale);
			this.deleteCoursesByProvider(providerId, locale);
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
			throw e;
		}
		return "task.launched";
	}
		
	private void deleteSchoolsByProvider(Long providerId, Locale locale) throws Exception {
		try {
			List<School> schools = this.serviceLocator.getSchoolService().getSchoolsByProvider(providerId, null, locale);
			for(School school : schools ) {
				this.serviceLocator.getSchoolService().delete(school, locale);
			}
		} catch(Exception e) {
			throw e;
		}
	}
	
	private void deleteCoursesByProvider(Long providerId, Locale locale) throws Exception{
		try {
			Collection<Course> courses = this.serviceLocator.getCourseService().getCoursesByProvider(providerId, null, locale);
			for(Course course :courses) {
				this.deleteCoursesCatalogByCourse(course, locale);
				this.serviceLocator.getCourseService().delete(course, locale);
			}
		} catch(Exception e) {
			throw e;
		}
	}
	
	private void deleteCoursesCatalogByCourse(Course course, Locale locale) throws Exception {
		try {
			this.serviceLocator.getCourseHtmlService().deleteCourseCatalogByCourseId(course.getId());
		} catch(Exception e) {
			throw e;
		}
	}
}
