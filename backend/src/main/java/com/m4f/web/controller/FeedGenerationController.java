package com.m4f.web.controller;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.m4f.business.domain.CronTaskReport;
import com.m4f.business.domain.MediationService;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.extended.ExtendedCourse;
import com.m4f.business.domain.extended.ExtendedSchool;
import com.m4f.business.domain.extended.FeedCourses;
import com.m4f.business.domain.extended.FeedSchools;
import com.m4f.utils.StackTraceUtil;

@Controller
@RequestMapping("/_feeds")
public class FeedGenerationController extends BaseController  {
	private static final Logger LOGGER = Logger.getLogger(FeedGenerationController.class.getName());
	
	/*
	 * This task generates internal feeds for a mediationService
	 */
	@RequestMapping(value="/mediation/create", method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseStatus(HttpStatus.OK)
	public void generateInternalFeedsByMediationId(@RequestParam(required=true) Long mediationId, 
			@RequestHeader("host") String host) throws Exception {
		final String FRONTEND_HOST = "http://localhost:8888";
		//LOGGER.info("referer: " + referer);
		// Create a new CronTaskReport
		CronTaskReport report = cronTaskReportService.create();
		report.setObject_id(mediationId);
		report.setDate(new Date());
		report.setType(CronTaskReport.TYPE.INTERNAL_FEED);
		try {
			// Start the process		
			Provider provider =  providerService.getProviderByMediationService(mediationId, null);		
			MediationService mediationService = mediatorService.getMediationService(mediationId, null);
			//Set report description
			report.setDescription(new StringBuffer("Servicio de mediación: ").append(mediationService.getName()).toString());
			
			if(!mediationService.getHasFeed()) { // All must be manual mediator, but it's another check.
				FeedSchools feedSchools = internalFeedService.createFeedSchools(FRONTEND_HOST, provider, mediationService);
				internalFeedService.saveFeedSchools(feedSchools);
				HashMap<Long, ExtendedSchool> schools = new HashMap<Long, ExtendedSchool>();
				Collection<ExtendedCourse> courses = 
					extendedCourseService.getCoursesByOwner(mediationService.getId(), null, null);
				for(ExtendedCourse course : courses) {
					ExtendedSchool school = extendedSchoolService.getSchool(course.getSchool(), Locale.getDefault());
					if(school != null) schools.put(school.getId(), school);
				}
				for(ExtendedSchool school : schools.values()) {
					FeedCourses feedCourse = internalFeedService.createFeedCourses(FRONTEND_HOST, 
							provider, mediationService, school, this.getAvailableLanguages()); 	
					internalFeedService.saveFeedCourses(feedCourse);
				}
				// Set result into report
				report.setResult("OK");
			}
			
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
			report.setResult(new StringBuffer("ERROR: ").append(e.getMessage()).toString());
			throw e;
		} finally {
			cronTaskReportService.save(report);
		}
	}
	/**
	 * END INTERNAL FEED GENERATION
	 */
}