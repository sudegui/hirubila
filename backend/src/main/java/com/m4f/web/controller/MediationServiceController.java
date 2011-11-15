package com.m4f.web.controller;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
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
@RequestMapping("/mediation")
public class MediationServiceController extends BaseController {
	
	private static final Logger LOGGER = Logger.getLogger(MediationServiceController.class.getName());
	private static String host = "hirubila.appspot.com";
	
	/*
	 * This task generates internal feeds for a mediationService
	 */
	@RequestMapping(value="/feeds", method={RequestMethod.POST,RequestMethod.GET})
	@ResponseStatus(HttpStatus.OK)
	public void generateCoursesFeed(@RequestParam(required=true) Long mediationId, 
			HttpServletRequest request) throws Exception {
		// Create a new CronTaskReport
		CronTaskReport report = cronTaskReportService.create();
		report.setObject_id(mediationId);
		report.setDate(new Date());
		report.setType(CronTaskReport.TYPE.INTERNAL_FEED);
		MediationService mediationService = mediatorService.getMediationService(mediationId, null);
		report.setDescription(new StringBuffer("Creating feed for MediationService ").append(mediationService.getName()).toString());
		if(mediationService.getHasFeed()) {
			report.setResult("NOOK because has feed.");
			cronTaskReportService.save(report);
			return;
		}
		try {
			LOGGER.info("Creating feeds for " + mediationService.getName() + " service");		
			Provider provider =  providerService.getProviderByMediationService(mediationId, null);		
			FeedSchools feedSchools = internalFeedService.createFeedSchools(host, provider, mediationService);
			internalFeedService.saveFeedSchools(feedSchools);
			HashMap<Long, ExtendedSchool> schools = new HashMap<Long, ExtendedSchool>();
			Collection<ExtendedCourse> courses = 
					extendedCourseService.getCoursesByOwner(mediationService.getId(), null, null);
			for(ExtendedCourse course : courses) {
				ExtendedSchool school = extendedSchoolService.getSchool(course.getSchool(), Locale.getDefault());
				if(school != null) schools.put(school.getId(), school);
			}
			for(ExtendedSchool school : schools.values()) {
				FeedCourses feedCourse = internalFeedService.createFeedCourses(host, 
						provider, mediationService, school, this.getAvailableLanguages()); 	
				internalFeedService.saveFeedCourses(feedCourse);
			}
			report.setResult("OK");
		} catch (Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			report.setResult(new StringBuffer("ERROR: ").append(e.getMessage()).toString());
			throw e;
		} finally {
			cronTaskReportService.save(report);
		}
	}
	
}