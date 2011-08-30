package com.m4f.web.controller.cron;

import java.io.StringWriter;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.logging.Logger;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;
import com.m4f.business.domain.Course;
import com.m4f.business.domain.CronTaskReport;
import com.m4f.business.domain.CustomChannelMessage;
import com.m4f.business.domain.MediationService;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.School;
import com.m4f.business.service.exception.ContextNotActiveException;
import com.m4f.business.service.exception.ServiceNotFoundException;
import com.m4f.utils.PageManager;
import com.m4f.utils.StackTraceUtil;
import com.m4f.utils.feeds.events.model.Dump;
import com.m4f.web.controller.BaseController;

@Controller
@RequestMapping("/launcher")
public class LauncherController extends BaseController {
	
	private static final Logger LOGGER = Logger.getLogger(LauncherController.class.getName());
	
	
	/**
	 * Load schools from one registered provider's feed.
	 * 
	 * @param providerId unique identifier of provider.
	 * @param model spring model object.
	 * @return tiles's registered view.
	 */
	@RequestMapping(value="/updateschools/{providerId}", method=RequestMethod.GET)
	public String updateProviderCourses(@PathVariable Long providerId, Model model) {		
		Provider provider = null;
		try {
			provider = this.serviceLocator.getProviderService().getProviderById(providerId, new Locale("es"));
			if(provider == null) {
				String message = "Provider with id " + providerId + 
					" doesn´t exist.";
				model.addAttribute("message", message);
				return "common.error";
			}
			Dump dump = this.serviceLocator.getDumpService().createDump();
			String message = "Proceso de importación del proveedor (" + 
				provider.getId() + "-" + provider.getName() + ")";
			dump.setDescription(message);
			dump.setLaunched(Calendar.getInstance(new Locale("es")).getTime());
			dump.setOwner(provider.getId());
			dump.setOwnerClass(Provider.class.getName());
			this.serviceLocator.getDumpService().save(dump);
			Queue queue = QueueFactory.getQueue(this.serviceLocator.getAppConfigurationService().getGlobalConfiguration().PROVIDER_QUEUE);
			TaskOptions options = TaskOptions.Builder.withUrl("/task/loadproviderfeed");
			options.param("providerId", provider.getId().toString());
			options.param("dumpId", "" + dump.getId());
			options.method(Method.POST);
			queue.add(options);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			model.addAttribute("message", "Error updating schools from " + 
					provider.getName());
			return "common.error";
		}
		return "cron.launched";
	}
	
	
	
	@RequestMapping(value="/updatecourses/{schoolId}", method=RequestMethod.GET)
	public String updateSchoolCourses(@PathVariable Long schoolId, Model model) {
		School school = null;
		try {
			school = this.serviceLocator.getSchoolService().getSchool(schoolId, new Locale("es"));
			if(school == null) {
				String message = "School with id " + schoolId + 
				" doesn´t exist.";
				model.addAttribute("message", message);
				return "common.error";
			}
			if(!"".equals(school.getFeed())) {
				Dump dump = this.serviceLocator.getDumpService().createDump();
				String message = "Launched dump for school (" + 
					school.getId() + "-" + school.getName() + ")";
				dump.setDescription(message);
				dump.setLaunched(Calendar.getInstance(new Locale("es")).getTime());
				dump.setOwner(school.getId());
				dump.setOwnerClass(School.class.getName());
				this.serviceLocator.getDumpService().save(dump);
				Queue queue = QueueFactory.getQueue(this.serviceLocator.getAppConfigurationService().getGlobalConfiguration().SCHOOL_QUEUE);
				TaskOptions options = TaskOptions.Builder.withUrl("/task/updatecourses");
				options.param("schoolId", school.getId().toString());
				options.param("dumpId", "" + dump.getId());
				options.method(Method.POST);
				queue.add(options);
			}
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			model.addAttribute("message", "Error actualizando cursos del centro " + 
					school.getName());
			return "common.error";
		}
		return "cron.launched";
	}
	
	
	/*
	 * START CREATE CATALOG NEW WAY
	 */
	
	/**
	 * Task for completed regeneration for SEO Catalog for courses.
	 * 
	 * 
	 * @return tiles's registered view.
	 */
	@RequestMapping(value="/catalog/regenerate", method=RequestMethod.GET)
	public String generateCatalog(Locale locale) throws ServiceNotFoundException, ContextNotActiveException {
		try {
			Queue queue = QueueFactory.getQueue(this.serviceLocator.getAppConfigurationService().getGlobalConfiguration().CATALOG_QUEUE);
			TaskOptions options = TaskOptions.Builder.withUrl("/task/catalog/regenerate");
			options.method(Method.GET);
			queue.add(options);		
		} catch (ServiceNotFoundException e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			throw e;
		} catch (ContextNotActiveException e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			throw e;					
		}		
		return "cron.launched";
	}
	
	/**
	 * Task for completed deletion for SEO Catalog for courses.
	 * 
	 * 
	 * @return tiles's registered view.
	 */
	@RequestMapping(value="/catalog/destroy", method=RequestMethod.GET)
	public String destroyCatalog(Locale locale) {
		final int RANGE = 200;
		try {
			LOGGER.severe("#### Deleting Catalog for all Courses.............");
			PageManager<Course> paginator = new PageManager<Course>();
			paginator.setOffset(RANGE);
			paginator.setStart(0);
			paginator.setSize(this.serviceLocator.getCatalogService().countCourseCatalog(locale));
			for(Integer page : paginator.getPagesIterator()) {
				Queue queue = QueueFactory.getQueue(this.serviceLocator.getAppConfigurationService().getGlobalConfiguration().CATALOG_QUEUE);
				TaskOptions options = TaskOptions.Builder.withUrl("/task/catalog/deletepaginated");
				options.param("start", "" +(page-1)*RANGE);
				options.param("finish", "" + (page)*RANGE);
				options.method(Method.POST);
				queue.add(options);		
			}
			
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		
		return "cron.launched";
	}
	/*
	 * END CREATE NEW CATALOG
	 */
	
	/*
	 * TEMPORARY TASK TO ADD A PROVIDER TO ALL MEDIATION SERVICE MANUAL
	 */
	@RequestMapping(value="/createprovidermanual", method=RequestMethod.GET)
	public String createProvidersManual() {
		try {
			Collection<MediationService> mediationServices = this.serviceLocator.getMediatorService().getMediationServices(false, Locale.getDefault());
			for(MediationService m : mediationServices) {
				Queue queue = QueueFactory.getQueue(this.serviceLocator.getAppConfigurationService().getGlobalConfiguration().PROVIDER_QUEUE);
				TaskOptions options = TaskOptions.Builder.withUrl("/task/createprovidersmanual");
				options.param("mediationId", String.valueOf(m.getId()));
				options.method(Method.POST);
				queue.add(options);
			}
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "cron.launched";
	}
	
	
	
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
				Queue queue = QueueFactory.getQueue(this.serviceLocator.getAppConfigurationService().getGlobalConfiguration().PROVIDER_QUEUE);
				TaskOptions options = TaskOptions.Builder.withUrl("/task/loadproviderfeed");
				options.param("providerId", String.valueOf(id));
				options.param("dumpId", String.valueOf(dump.getId()));
				options.method(Method.POST);
				queue.add(options);
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
					Queue queue = QueueFactory.getQueue(this.serviceLocator.getAppConfigurationService().getGlobalConfiguration().INTERNAL_FEED_QUEUE);
					TaskOptions options = TaskOptions.Builder.withUrl("/task/internalFeeds/mediation");
					options.param("mediationId", String.valueOf(id));
					options.method(Method.POST);
					queue.add(options);
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
	
	@RequestMapping(value="/websocket", method=RequestMethod.GET)
	public String websocket() throws Exception {
		Random generator = new Random();
		for(String clientId : this.connectedClients) {
			 ChannelService channelService = ChannelServiceFactory.getChannelService();
			 ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
			 StringWriter strWriter  = new StringWriter();
			 CustomChannelMessage message = new CustomChannelMessage("v-" + generator.nextInt());
			 mapper.writeValue(strWriter, message);
		     channelService.sendMessage(new ChannelMessage(clientId, strWriter.toString()));
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