package com.m4f.web.controller.cron;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;
import com.m4f.business.domain.Course;
import com.m4f.business.domain.CronTaskReport;
import com.m4f.business.domain.MediationService;
import com.m4f.business.domain.School;
import com.m4f.utils.PageManager;
import com.m4f.utils.StackTraceUtil;
import com.m4f.web.controller.BaseController;

@Controller
@RequestMapping("/loader")
public class LoaderController extends BaseController {
	
	private static final Logger LOGGER = Logger.getLogger(LoaderController.class.getName());
	
	@RequestMapping(value="/clean/feedSchools", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void cleanFeedSchools(@RequestParam(required=true) Long schoolId) throws Exception {
		// Invoke the task with the id obtained
		Map<String, String> params = new HashMap<String, String>();

		this.serviceLocator.getWorkerFactory().createWorker().addWork(
				this.serviceLocator.getAppConfigurationService().getGlobalConfiguration().INTERNAL_FEED_QUEUE, 
				"/task/clean/feedSchools", params);
	}	
	
	@RequestMapping(value="/clean/feedCourses", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void cleanFeedCourses(@RequestParam(required=true) Long schoolId) throws Exception {
		// Invoke the task with the id obtained
		Map<String, String> params = new HashMap<String, String>();
		
		this.serviceLocator.getWorkerFactory().createWorker().addWork(
				this.serviceLocator.getAppConfigurationService().getGlobalConfiguration().INTERNAL_FEED_QUEUE, 
				"/task/clean/feedCourses", params);
	}	
	
	@RequestMapping(value="/update/school", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void testSchool(@RequestParam(required=true) Long schoolId) throws Exception {
		// Invoke the task with the id obtained
		Map<String, String> params = new HashMap<String, String>();
		params.put("schoolId", String.valueOf(schoolId));
		this.serviceLocator.getWorkerFactory().createWorker().addWork(
				this.serviceLocator.getAppConfigurationService().getGlobalConfiguration().INTERNAL_FEED_QUEUE, 
				"/task/school/feed", params);
	}	
	
	
	/*
	 * Cron task to generate all internal feeds with the last information. Invoke for each manual mediation service a
	 * backend task to do it.
	 */
	@RequestMapping(value="/manual/feed", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void generateManualFeeds() throws Exception {
		// Get all manual mediation services
		List<MediationService> mediations = this.serviceLocator.getMediatorService().getAllMediationService(null);
		if(mediations != null) {
			// For each one create a backend task.
			for(MediationService mediation : mediations) {
				if(!mediation.getHasFeed()) { // If its a manual mediation service
					LOGGER.info("Manual mediation service: " + mediation.getName());
					// Invoke the task with the id obtained
					Map<String, String> params = new HashMap<String, String>();
					params.put("mediationId", String.valueOf(mediation.getId()));
					this.serviceLocator.getWorkerFactory().createWorker().addWork(
							this.serviceLocator.getAppConfigurationService().getGlobalConfiguration().INTERNAL_FEED_QUEUE, 
							"/task/_feed/mediation/create", params);
				}
			}
		}
	}
	
	/* TEST METHODS! */
	@RequestMapping(value="/school/information", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void infoSchool(@RequestParam(required=true) Long schoolId, HttpServletResponse response) throws Exception {
		// Invoke the task with the id obtained
		
		Collection<Course> coursesEs = this.serviceLocator.getCourseService().getCoursesBySchool(schoolId, "id", new Locale("es"));
		Collection<Course> coursesEu = this.serviceLocator.getCourseService().getCoursesBySchool(schoolId, "id", new Locale("eu"));
		StringBuffer sbEs = new StringBuffer();
		StringBuffer sbEu = new StringBuffer();
		
		for(Course c: coursesEs) {
			sbEs.append("Id: ").append(c.getId()).
				append(" eId: ").append(c.getExternalId()).
				append(" title: ").append(c.getTitle()).
				append("\n");
		}
		
		for(Course c: coursesEu) {
			sbEu.append("Id: ").append(c.getId()).
				append(" eId: ").append(c.getExternalId()).
				append(" title: ").append(c.getTitle()).
				append("\n");
		}
		
		response.getWriter().write(sbEs.toString() + "\n" + sbEu.toString());
		
	}
	
	@RequestMapping(value="/course/information", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void infoSchool(@RequestParam(required=true) String eId, HttpServletResponse response) throws Exception {
		// Invoke the task with the id obtained
		Collection<Course> cEs = this.serviceLocator.getCourseService().getCoursesByExternalId(eId, new Locale("es"));
		Collection<Course> cEu = this.serviceLocator.getCourseService().getCoursesByExternalId(eId, new Locale("eu"));
		StringBuffer sb = new StringBuffer();
		sb.append("Numero de cursos ES: ").append(cEs.size()).append(" EU:").append(cEu.size());
		
		for(Course c: cEs) {
			sb.append("Id: ").append(c.getId()).
				append(" eId: ").append(c.getExternalId()).
				append(" title: ").append(c.getTitle()).
				append("\n");
		}
		
		for(Course c: cEu) {
			sb.append("Id: ").append(c.getId()).
				append(" eId: ").append(c.getExternalId()).
				append(" title: ").append(c.getTitle()).
				append("\n");
		}
		
		response.getWriter().write(sb.toString());
		
	}
	
	/*@RequestMapping(value="/course/noUpdated", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void infoUpdatedCourses(@RequestParam(required=true) String eId, HttpServletResponse response) throws Exception {
		StringBuffer sb = new StringBuffer();
		long count = this.serviceLocator.getCourseService().countTESTnoUPDATED();
		
		sb.append("Numero de cursos sin updated es: ").append(count);
		
		response.getWriter().write(sb.toString());
	}
	
	@RequestMapping(value="/course/noUpdatedGet", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void noUpdateCoursesGet(@RequestParam(required=true) String eId, HttpServletResponse response) throws Exception {
		Queue queue = QueueFactory.getDefaultQueue();
		TaskOptions options = TaskOptions.Builder.withUrl("/task/course/noUpdatedGet");
		options.method(Method.GET);
		options.param("eId", "78989795646");
	    queue.add(options);
	}*/
	
	
	/*@RequestMapping(value="/update/providers", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void updateProvidersInformation() throws Exception {
		this.serviceLocator.getWorkerFactory().createWorker().addWork(
				this.serviceLocator.getAppConfigurationService().getGlobalConfiguration().PROVIDER_QUEUE, 
				"/task/update/providers", new HashMap());
	}*/
	
	/*
	@RequestMapping(value="/update/schools", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void updateSchoolsInformation() throws Exception {
		this.serviceLocator.getWorkerFactory().createWorker().addWork(
				this.serviceLocator.getAppConfigurationService().getGlobalConfiguration().SCHOOL_QUEUE, 
				"/task/update/schools", new HashMap());
	}*/
	
	/*
	 * Cron task to update a provider's information, using a round-robin method. It invokes a backend task to do it.
	 */
	/*@RequestMapping(value="/update/provider", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void updateProviderInformation(@RequestParam(required=false) Long providerId) throws Exception {
		CronTaskReport report = null;
		try {
			report = this.serviceLocator.getCronTaskReportService().getLastCronTaskReport(CronTaskReport.TYPE.PROVIDER_FEED);
		} catch(Exception e) {
			LOGGER.severe(new StringBuffer("No se ha podido recuperara el " +
					"ultimo CronTaskReport").append(StackTraceUtil.getStackTrace(e)).toString());			
		}
		try {
			Long id = null;
		
			if(providerId == null) {
				List<Long> ids = this.serviceLocator.getProviderService().getAllProviderIds();
				if(ids != null && ids.size() > 0) {
					id = this.getNextIdCronTaskReport(report != null ? report.getObject_id() : null, ids);
				}
			} else {
				id = providerId;
			}
			
			if(id != null) {
				LOGGER.info("Invoking backend task to update provider with ID:" + id);
				// Invoke the task with the id obtained
				Map<String, String> params = new HashMap<String, String>();
				params.put("providerId", String.valueOf(id));
				this.serviceLocator.getWorkerFactory().createWorker().addWork(
						this.serviceLocator.getAppConfigurationService().getGlobalConfiguration().PROVIDER_QUEUE, 
						"/task/provider/feed", params);
			} else {
				LOGGER.severe("No provider ID for invokin task in the backend! Update provider's information method");
			}
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			throw e;
		}
	}*/
	
	/*
	 * Cron task to update provider's information that has external Feed, using a round-robin method. It invokes a backend task to do it.
	 */
	@RequestMapping(value="/update/provider", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void updateManualProviderInformation(@RequestParam(required=false) Long providerId) throws Exception {
		CronTaskReport report = null;
		boolean register = true;
		
		try {
			report = this.serviceLocator.getCronTaskReportService().getLastCronTaskReport(CronTaskReport.TYPE.PROVIDER_FEED);
		} catch(Exception e) {
			LOGGER.severe(new StringBuffer("No se ha podido recuperara el " +
					"ultimo CronTaskReport").append(StackTraceUtil.getStackTrace(e)).toString());			
		}
		try {
			Long id = null;
		
			if(providerId == null) {
				List<Long> manualMediation = this.serviceLocator.getMediatorService().getAllMediationServiceIds(Boolean.TRUE);
				List<Long> ids = this.serviceLocator.getProviderService().getProviderIdsByMediations(manualMediation);
				StringBuffer idsSb = new StringBuffer();
				
				for(Long idTest : manualMediation) {
					idsSb.append("[").append(idTest).append("] ");
				}
				LOGGER.info("Los ids de los mediadores son: " + idsSb.toString());
				
				idsSb = new StringBuffer();
				for(Long idTest : ids) {
					idsSb.append("[").append(idTest).append("] ");
				}
				LOGGER.info("Los ids de los providers son: " + idsSb.toString());
				
				if(ids != null && ids.size() > 0) {
					id = this.getNextIdCronTaskReport(report != null ? report.getObject_id() : null, ids);
				}
			} else {
				register = false;
				id = providerId;
			}
			
			if(id != null) {
				LOGGER.info("Invoking backend task to update provider with ID:" + id);
				// Invoke the task with the id obtained
				Map<String, String> params = new HashMap<String, String>();
				params.put("providerId", String.valueOf(id));
				if(!register) {
					params.put("register", "false");
				}
				this.serviceLocator.getWorkerFactory().createWorker().addWork(
						this.serviceLocator.getAppConfigurationService().getGlobalConfiguration().PROVIDER_QUEUE, 
						"/task/provider/feed", params);
			} else {
				LOGGER.severe("No provider ID for invokin task in the backend! Update provider's information method");
			}
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			throw e;
		}
	}
	
	/*
	 * Cron task to update provider's information that has external Feed, using a round-robin method. It invokes a backend task to do it.
	 */
	@RequestMapping(value="/update/school", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void updateManualSchoolInformation(@RequestParam(required=false) Long schoolId, Locale locale) throws Exception {
		try {
			School school = this.serviceLocator.getSchoolService().getSchool(schoolId, locale);
			if(school != null) {
				Map<String, String> params = new HashMap<String, String>();
				params.put("schoolId", String.valueOf(school.getId()));
				
				this.serviceLocator.getWorkerFactory().createWorker().addWork(
						this.serviceLocator.getAppConfigurationService().getGlobalConfiguration().SCHOOL_QUEUE, 
						"/task/school/feed", params);
			} else {
				LOGGER.severe("No school ID for invokin task in the backend! Update school's information method");
			}
			
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			throw e;
		}
		
	}
	/*
	@RequestMapping(value="/update/provider/schools", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void loadProviderSchools(@RequestParam(required=false) Long providerId) throws Exception {
		CronTaskReport report = null;
		try {
			report = this.serviceLocator.getCronTaskReportService().getLastCronTaskReport(CronTaskReport.TYPE.PROVIDER_SCHOOLS);
		} catch(Exception e) {
			LOGGER.severe(new StringBuffer("No se ha podido recuperara el " +
					"ultimo CronTaskReport").append(StackTraceUtil.getStackTrace(e)).toString());			
		}
		try {
			// Get a list with all manual mediation service ids.
			Long id = null;
			if(providerId != null) {
				id = providerId;
			}
			else {
				List<Long> ids = this.serviceLocator.getProviderService().getAllProviderIds();
				
				if(ids != null && ids.size() > 0) {
					id = this.getNextIdCronTaskReport(report != null ? report.getObject_id() : null, ids);
				}
			}
			if(id != null) {
				// Invoke the task with the id obtained
				Map<String, String> params = new HashMap<String, String>();
				params.put("providerId", String.valueOf(id));
				this.serviceLocator.getWorkerFactory().createWorker().addWork(
						this.serviceLocator.getAppConfigurationService().getGlobalConfiguration().SCHOOL_QUEUE, 
						"/task/provider/schools", params);
			}
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			throw e;
		}
	}*/
	
	/*
	 * Cron task to update a school's information, using a round-robin method. It invokes a backend task to do it.
	 */
	/*@RequestMapping(value="/update/school", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void updateSchoolInformation() throws Exception {
		CronTaskReport report = null;
		try {
			report = this.serviceLocator.getCronTaskReportService().getLastCronTaskReport(CronTaskReport.TYPE.PROVIDER_SCHOOLS);
		} catch(Exception e) {
			LOGGER.severe(new StringBuffer("No se ha podido recuperara el " +
					"ultimo CronTaskReport").append(StackTraceUtil.getStackTrace(e)).toString());			
		}
		try {
			Long id = null;
			List<Long> ids = this.serviceLocator.getSchoolService().getAllSchoolIds();
			
			if(ids != null && ids.size() > 0) {
				id = this.getNextIdCronTaskReport(report != null ? report.getObject_id() : null, ids);
			}
			if(id != null) {
				LOGGER.info("Invoking backend task to update school with ID:" + id);
				// Invoke the task with the id obtained
				Map<String, String> params = new HashMap<String, String>();
				params.put("schoolId", String.valueOf(id));
				this.serviceLocator.getWorkerFactory().createWorker().addWork(
						this.serviceLocator.getAppConfigurationService().getGlobalConfiguration().SCHOOL_QUEUE, 
						"/task/school/feed", params);
			} else {
				LOGGER.severe("No schol ID for invokin task in the backend! Update school's information method");
			}
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			throw e;
		}
	}*/
	
	private Long getNextIdCronTaskReport(Long lastId, List<Long> ids) {
		Long id = null;
		if(ids != null && ids.size() > 0) {
			if(lastId != null) {
				for(int i=0; i< ids.size(); i++) {
					if(lastId.equals(ids.get(i))) {
						// Extract next element if it possible, if not extract the first element
						if(i+1 < ids.size()) id = ids.get(i+1);
						else id = ids.get(0);
						break; // Id found. Break the loop.
					}
				}
			} else {
				// There is not reports yet. First id in the list
				id = ids.get(0);
			}
		}		
		return id;
	}
}