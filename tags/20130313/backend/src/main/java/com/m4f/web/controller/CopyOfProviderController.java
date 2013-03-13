package com.m4f.web.controller;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.xml.sax.SAXException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.m4f.business.domain.CronTaskReport;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.School;
import com.m4f.utils.PageManager;
import com.m4f.utils.StackTraceUtil;
import com.m4f.utils.feeds.importer.Importer;
import com.m4f.utils.feeds.importer.ProviderImporter;
import com.m4f.utils.feeds.importer.SchoolImporter;
import com.m4f.utils.worker.impl.AppEngineBackendWorker;


@Controller
@RequestMapping("/copyprovider")
public class CopyOfProviderController extends BaseController {
	private static final Logger LOGGER = Logger.getLogger(CopyOfProviderController.class.getName());
	
	@Autowired
	ProviderImporter providerImporter;
	@Autowired
	SchoolImporter schoolImporter;
	@Autowired
	AppEngineBackendWorker worker;
	
	@RequestMapping(value = "/ids", method = RequestMethod.GET)
	@ResponseBody
	public Collection<Long> getIds() throws Exception {
		return this.providerService.getAllProviderIds();
	}
	
	@RequestMapping(value = "/feed", method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseStatus(HttpStatus.OK)
	public void loadFeed(@RequestParam Long providerId) 
			throws ParserConfigurationException, SAXException, IOException, Exception {
		CronTaskReport report = cronTaskReportService.create();
		report.setObject_id(providerId);
		report.setDate(new Date());
		report.setType(CronTaskReport.TYPE.PROVIDER_FEED);
		try {
			Provider provider = this.providerService.getProviderById(providerId, null);
			report.setDescription("Importing schools from " + provider.getName() + " provider.");	
			providerImporter.importSchools(provider, null);
			
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("providerId", String.valueOf(provider.getId()));
			worker.addWork("school", "/provider/schools", params);
			/*------------------------------------*/
			/*PageManager<School> paginator = new PageManager<School>();
			long total = schoolService.countSchoolsByProvider(provider.getId());
			paginator.setOffset(Importer.RANGE);
			paginator.setStart(0);
			paginator.setSize(total);
			for (Integer page : paginator.getTotalPagesIterator()) {
				int start = (page - 1) * (Importer.RANGE);
				int end = (page) * (Importer.RANGE);
				Collection<School> schools = schoolService.getSchoolsByProvider(provider.getId(), 
						"updated", null, start, end);
				for(School school : schools) {
					// Create tasks for the backend with the aim of getting courses for each school.
					Map<String, String> params = new HashMap<String, String>();
					params.put("schoolId", String.valueOf(school.getId()));
					
					worker.addWork("school", "/provider/shools", params);
				}
			}*/
			/*------------------------------------*/
			
			report.setResult("OK");
		} catch(Exception e) {
			report.setResult(new StringBuffer("ERROR: ").append(e.getMessage()).toString());
			throw e;
		} finally {
			cronTaskReportService.save(report);
		}
	}
	
	/*@RequestMapping(value = "/schools", method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseStatus(HttpStatus.OK)
	public void loadSchools(@RequestParam(required=false) Long providerId) 
			throws ParserConfigurationException, SAXException, IOException, Exception {
		Provider provider = null;
		
		try {
			provider = this.providerService.getProviderById(providerId, null);
			PageManager<School> paginator = new PageManager<School>();
			long total = schoolService.countSchoolsByProvider(provider.getId());
			paginator.setOffset(Importer.RANGE);
			paginator.setStart(0);
			paginator.setSize(total);
			for (Integer page : paginator.getTotalPagesIterator()) {
				int start = (page - 1) * (Importer.RANGE);
				int end = (page) * (Importer.RANGE);
				Collection<School> schools = schoolService.getSchoolsByProvider(provider.getId(), 
						"updated", null, start, end);
				for(School school : schools) {
					// Create tasks for the backend with the aim of getting courses for each school.
					Map<String, String> params = new HashMap<String, String>();
					params.put("providerId", String.valueOf(provider.getId()));
					params.put("schoolId", String.valueOf(school.getId()));
					
					worker.addWork("school", "/provider/school", params);
				}
			}
		} catch(Exception e) {
			
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			
			throw e;
		}
	}*/
	
	/*@RequestMapping(value = "/school", method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseStatus(HttpStatus.OK)
	public void loadSchool(@RequestParam(required=false) Long providerId, Long schoolId) 
			throws ParserConfigurationException, SAXException, IOException, Exception {
		Provider provider;
		School school;
		CronTaskReport report = cronTaskReportService.create();
		report.setObject_id(providerId);
		report.setDate(new Date());
		report.setType(CronTaskReport.TYPE.PROVIDER_SCHOOLS);
		try {
			provider = this.providerService.getProviderById(providerId, null);
			school = this.schoolService.getSchool(schoolId, null);
			report.setDescription("Importing courses from " + provider.getName() + " provider.");
			schoolImporter.importCourses(provider, school);

			report.setResult("OK");
		} catch(Exception e) {
			report.setResult(new StringBuffer("ERROR: ").append(e.getMessage()).toString());
			throw e;
		} finally {
			cronTaskReportService.save(report);
		}
		
	}*/
	
	@RequestMapping(value = "/schools", method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseStatus(HttpStatus.OK)
	public void loadSchools(@RequestParam(required=false) Long providerId) 
			throws ParserConfigurationException, SAXException, IOException, Exception {
		CronTaskReport report = cronTaskReportService.create();
		report.setObject_id(providerId);
		report.setDate(new Date());
		report.setType(CronTaskReport.TYPE.PROVIDER_SCHOOLS);
		try {
			Provider provider = this.providerService.getProviderById(providerId, null);
			report.setDescription("Importing courses from " + provider.getName() + " provider.");
			providerImporter.importCourses(provider, null);
			report.setResult("OK");
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("providerId", String.valueOf(provider.getId()));
			worker.addWork("catalog", "/provider/catalog", params);
		} catch(Exception e) {
			report.setResult(new StringBuffer("ERROR: ").append(e.getMessage()).toString());
			throw e;
		} finally {
			cronTaskReportService.save(report);
		}
		
	}
	
	@RequestMapping(value = "/catalog", method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseStatus(HttpStatus.OK)
	public void createCatalog(@RequestParam(required=false) Long providerId) 
			throws ParserConfigurationException, SAXException, IOException, Exception {
		CronTaskReport report = cronTaskReportService.create();
		report.setObject_id(providerId);
		report.setDate(new Date());
		report.setType(CronTaskReport.TYPE.PROVIDER_CATALOG);
		try {
			Provider provider = this.providerService.getProviderById(providerId, null);
			report.setDescription("Creating catalog from " + provider.getName() + " provider.");
			providerImporter.importCourses(provider, null);
			report.setResult("OK");
		} catch(Exception e) {
			report.setResult(new StringBuffer("ERROR: ").append(e.getMessage()).toString());
			throw e;
		} finally {
			cronTaskReportService.save(report);
		}
		
	}
	
	/*@RequestMapping(value="/import/courses", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public String importCourses(@RequestParam(required=true) Long schoolId) throws Exception {
		
		Provider provider = null;
		School school = null;
		CronTaskReport report = cronTaskReportService.create();
		try {
			school = this.schoolService.getSchool(schoolId, null);
			provider = this.providerService.getProviderById(school.getProvider(), null);
			
			report.setObject_id(provider.getId());
			report.setDate(new Date());
			report.setType(CronTaskReport.TYPE.PROVIDER_SCHOOLS);
			
			schoolImporter.importCourses(provider, school);
		} catch(Exception e) {
			report.setResult(new StringBuffer("ERROR: ").append(e.getMessage()).toString());
			throw e;
			//LOGGER.finest("-------------------------------------------");
			//if(provider != null && school != null) {
			//	LOGGER.finest("Importing courses of provider " + provider.getId() + " - " + provider.getName() + " school: " + school.getId() + " - " + school.getName());
			//}
			//LOGGER.severe(StackTraceUtil.getStackTrace(e));
			//LOGGER.finest("-------------------------------------------"); 
		} finally {
			cronTaskReportService.save(report);
		}
	
		return "task.launched";
	}*/
}