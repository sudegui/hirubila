package com.m4f.web.controller.cron;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.m4f.business.domain.CronTaskReport;
import com.m4f.utils.StackTraceUtil;
import com.m4f.web.controller.BaseController;

@Controller
@RequestMapping("/loader")
public class LoaderController extends BaseController {
	
	private static final Logger LOGGER = Logger.getLogger(LoaderController.class.getName());
	
	/*
	 * CRON TO EXECUTE IN ORDER AND IN ROUND-ROBIN METHOD THE SCHOOLS AND COURSES FROM THEIR PROVIDER
	 */
	@RequestMapping(value="/provider/feed", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void loadProviderFeed(@RequestParam(required=false) Long providerId) throws Exception {
		CronTaskReport report = null;
		try {
			report = this.serviceLocator.getCronTaskReportService().getLastCronTaskReport(CronTaskReport.TYPE.PROVIDER_FEED);
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
						this.serviceLocator.getAppConfigurationService().getGlobalConfiguration().PROVIDER_QUEUE, 
						"/es/provider/feed", params);
			}
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			throw e;
		}
	}
	
	@RequestMapping(value="/provider/schools", method=RequestMethod.GET)
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
						this.serviceLocator.getAppConfigurationService().getGlobalConfiguration().PROVIDER_QUEUE, 
						"/es/provider/schools", params);
			}
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			throw e;
		}
	}
	
	
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