package com.m4f.web.controller.cron;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.m4f.business.domain.CronTaskReport;
import com.m4f.business.domain.Provider;
import com.m4f.utils.StackTraceUtil;
import com.m4f.utils.feeds.events.model.Dump;
import com.m4f.web.controller.BaseController;

@Controller
@RequestMapping("/launcher")
public class LauncherController extends BaseController {
	
	private static final Logger LOGGER = Logger.getLogger(LauncherController.class.getName());
		
	/*
	 * CRON TO EXECUTE IN ORDER AND IN ROUND-ROBIN METHOD THE SCHOOLS AND COURSES FROM THEIR PROVIDER
	 */
	@RequestMapping(value="/loadprovider", method=RequestMethod.GET)
	public String loadProviderFeed() throws Exception {
		CronTaskReport report = null;
		try {
			report = this.serviceLocator.getCronTaskReportService().getLastCronTaskReport(CronTaskReport.TYPE.PROVIDER_FEED);
		} catch(Exception e) {
			LOGGER.severe(new StringBuffer("No se ha podido recuperara el " +
					"ultimo CronTaskReport").append(StackTraceUtil.getStackTrace(e)).toString());			
		}
		try {
			// Get a list with all manual mediation service ids.
			List<Long> ids = this.serviceLocator.getProviderService().getAllProviderIds();
			Long id = null;
			if(ids != null && ids.size() > 0) {
				id = this.getNextIdCronTaskReport(report != null ? report.getObject_id() : null, ids);
			}
			if(id != null) {
				// Dump file
				Dump dump = this.serviceLocator.getDumpService().createDump();
				String message = "Proceso de importación del proveedor (" + 
					id + "-" + " " + ")";
				dump.setDescription(message);
				dump.setLaunched(Calendar.getInstance(new Locale("es")).getTime());
				dump.setOwner(id);
				dump.setOwnerClass(Provider.class.getName());
				this.serviceLocator.getDumpService().save(dump);
				// Invoke the task with the id obtained
				Map<String, String> params = new HashMap<String, String>();
				params.put("providerId", String.valueOf(id));
				params.put("dumpId", String.valueOf(dump.getId()));
				this.serviceLocator.getWorkerFactory().createWorker().addWork(
						this.serviceLocator.getAppConfigurationService().getGlobalConfiguration().PROVIDER_QUEUE, 
						"/task/loadproviderfeed", params);
			}
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			throw e;
		}
		return "cron.launched";
	}
	
	
	/*
	 * CRON TO EXECUTE IN ORDER AND IN ROUND-ROBIN METHOD THE INTERNAL FEED GENERATION
	 */
	@RequestMapping(value="/internalfeedgeneration", method=RequestMethod.GET)
	public String internalFeedGeneration() throws Exception {
		CronTaskReport report = null;
		try {
			report = this.serviceLocator.getCronTaskReportService().getLastCronTaskReport(CronTaskReport.TYPE.INTERNAL_FEED);
		} catch(Exception e) {
			LOGGER.severe(new StringBuffer("No se ha podido recuperara el ultimo CronTaskReport").append(StackTraceUtil.getStackTrace(e)).toString());			
		}
		try {
			// Get a list with all manual mediation service ids.
			Long id = null;
			List<Long> ids = this.serviceLocator.getMediatorService().getAllMediationServiceManualIds();
			if(ids != null && ids.size() > 0) {
				id = this.getNextIdCronTaskReport(report != null ? report.getObject_id() : null, ids);
				if(id != null) {
					// Invoke the task with the id obtained
					Map<String, String> params = new HashMap<String, String>();
					params.put("mediationId", String.valueOf(id));
					this.serviceLocator.getWorkerFactory().createWorker().addWork(
							this.serviceLocator.getAppConfigurationService().getGlobalConfiguration().INTERNAL_FEED_QUEUE, 
							"/task/internalFeeds/mediation", params);
				}
			} else {
				LOGGER.severe("No se encontro ningun mediation service!!! NO SE HACE NADA!!");
			}
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			throw e;
		}
		return "cron.launched";
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