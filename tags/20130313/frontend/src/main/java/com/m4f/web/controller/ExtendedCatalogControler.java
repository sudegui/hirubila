package com.m4f.web.controller;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.m4f.business.domain.extended.ExtendedCourse;
import com.m4f.business.domain.extended.ExtendedSchool;
import com.m4f.business.domain.extended.FeedCourses;
import com.m4f.business.domain.extended.FeedSchools;
import com.m4f.business.domain.extended.Town;
import com.m4f.utils.StackTraceUtil;


@Controller
@RequestMapping("/extended")
public class ExtendedCatalogControler extends BaseController {
	private static final Logger LOGGER = Logger.getLogger(ExtendedCatalogControler.class.getName());
	
	/*
	 * Public FEEDs methods
	 */
	@RequestMapping(value="/public/school/feed/{providerId}", method=RequestMethod.GET)
	public void getSchoolFeed(@PathVariable Long providerId, 
			HttpServletResponse response, Locale locale, Model model) {
		try {
			FeedSchools feed = this.serviceLocator.getInternalFeedService().getLastFeedSchools(providerId);
			byte[] content = feed != null &&  feed.getContent() != null ? feed.getContent().getBytes() : null;
			response.setContentType("text/xml");
			response.setCharacterEncoding("UTF-8");
			if(content != null) {
				response.setContentLength(content.length);
				response.getOutputStream().write(content);
			}
			
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
		}
		
	}
	
	@RequestMapping(value="/public/course/feed/{providerId}/{schoolId}", method=RequestMethod.GET)
	public void getCoursesFeed(@PathVariable Long providerId, 
			@PathVariable Long schoolId, HttpServletResponse response, 
			Locale locale, Model model) {
		try {
			FeedCourses feed = this.serviceLocator.getInternalFeedService().getLastFeedCourses(providerId, schoolId);
			byte[] content = feed != null && feed.getContent() != null? feed.getContent().getBytes() : null;
			response.setContentType("text/xml");
			response.setCharacterEncoding("UTF-8");
			if(content != null) {
				response.setContentLength(content.length);
				response.getOutputStream().write(content);
			}
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
		}
	}
	
	/*
	 * Public detail methods
	 */
	@RequestMapping(value="/public/school/{schoolId}", method=RequestMethod.GET)
	public String showSchoolDetail(@PathVariable Long schoolId, Locale locale, Model model) {
		try {
			ExtendedSchool school = this.serviceLocator.getExtendedSchoolService().getSchool(schoolId, locale);
			model.addAttribute("school", school);
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		
		return "extended.public.school";
	}
	
	@RequestMapping(value="/public/course/{courseId}", method=RequestMethod.GET)
	public String showCourseDetail(@PathVariable Long courseId, Locale locale, Model model) {
		try {
			ExtendedCourse course = this.serviceLocator.getExtendedCourseService().getCourse(courseId, locale);
			ExtendedSchool school = this.serviceLocator.getExtendedSchoolService().getSchool(course.getSchool(), locale);
			Town town = this.serviceLocator.getTerritorialService().getTown(school.getTown(), locale);
			model.addAttribute("course", course);
			model.addAttribute("school", school);
			model.addAttribute("town", town);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}	
		return "extended.public.course";
	}
}
