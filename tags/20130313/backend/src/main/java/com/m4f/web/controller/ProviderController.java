package com.m4f.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.xml.sax.SAXException;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.m4f.business.domain.Course;
import com.m4f.business.domain.CourseCatalog;
import com.m4f.business.domain.CronTaskReport;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.School;
import com.m4f.utils.PageManager;
import com.m4f.utils.StackTraceUtil;
import com.m4f.utils.feeds.events.model.Dump;
import com.m4f.utils.feeds.importer.ProviderImporter;
import com.m4f.utils.feeds.importer.SchoolImporter;
import com.m4f.utils.worker.impl.AppEngineBackendWorker;


@Controller
@RequestMapping("/provider")
public class ProviderController extends BaseController {
	private static final Logger LOGGER = Logger.getLogger(ProviderController.class.getName());
	
	@Autowired
	ProviderImporter providerImporter;
	@Autowired
	SchoolImporter schoolImporter;
	@Autowired
	AppEngineBackendWorker worker;
	
	public static final int RANGE = 100;
	
	@RequestMapping(value = "/feed", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseStatus(HttpStatus.OK)
    public void loadFeed(@RequestParam Long providerId) 
                    throws ParserConfigurationException, SAXException, IOException, Exception { 
		Provider provider = null;
		CronTaskReport report = null;
		Dump dump = null;
		
        try {
        	provider = this.providerService.getProviderById(providerId, null);
            
            // CRON REPORT
            report = cronTaskReportService.create();
            report.setObject_id(providerId);
            report.setDate(new Date());
            report.setType(CronTaskReport.TYPE.PROVIDER_FEED);
			report.setDescription(new StringBuffer("Proveedor: ").append(provider.getName()).toString());
			
			// DUMP
			dump = this.dumpService.createDump();
        	String message = "Proceso de importacion del proveedor: " + provider.getName() + " (" + 
        			provider.getId() + "-" + " " + ")";
			dump.setDescription(message);
			dump.setLaunched(Calendar.getInstance(new Locale("es")).getTime());
			dump.setOwner(provider.getId());
			dump.setOwnerClass(Provider.class.getName());
			this.dumpService.save(dump);
        	
			// IMPORT PROVIDER'S SCHOOLS FROM FEED
			providerImporter.importSchools(provider, dump);
			
			// IMPORT PROVIDER'S COURSES FROM EACH SCHOOL
            PageManager<School> paginator = new PageManager<School>();
	        long total = schoolService.countSchoolsByProvider(providerId);
	        paginator.setOffset(RANGE);
	        paginator.setStart(0);
	        paginator.setSize(total);
	        ArrayList<School> fails = new ArrayList<School>();
	        for (Integer page : paginator.getTotalPagesIterator()) {
                int start = (page - 1) * RANGE;
                int end = (page) * RANGE;
                Collection<School> schools = schoolService.getSchoolsByProvider(providerId, 
                                "updated", null, start, end);
                for(School school : schools) {
                	/*Map<String, String> params = new HashMap<String, String>();
                	
					params.put("providerId", String.valueOf(providerId));
					params.put("schoolId", String.valueOf(school.getId()));
					params.put("dumpId", String.valueOf(dump.getId())); 
					
                	worker.addWork("school", "/provider/school", params); */
                	try {
                		providerImporter.createLoadTask(provider, school, dump);
                	} catch(Exception e) {
                		fails.add(school);
                	}
                }
	        }
	        
	        // Retries
	        for(School school : fails) {
	        	providerImporter.createLoadTask(provider, school, dump);
	        }
	        
	        // Set result into report
			report.setResult("OK");
        } catch(Exception e) {
                report.setResult(new StringBuffer("ERROR: ").append(e.getMessage()).toString());
                throw e;
        } finally {
        	cronTaskReportService.save(report);
        	dumpService.save(dump);
        	/*String[] cacheNames =  {"courses", "schools", "coursesCatalog"};
        	for(String cacheName : cacheNames) {
        		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService(cacheName);
        		syncCache.clearAll();
        	}
        	MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
    		syncCache.clearAll();*/    
        }
    }
	
 /* TEST para probar que modificando a uno el titulo luego se actualiza el curso.
  * @RequestMapping(value = "/change", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseStatus(HttpStatus.OK)
    public void change(@RequestParam(required=false) Long providerId) 
                    throws ParserConfigurationException, SAXException, IOException, Exception {
	 Collection<Course> courses = courseService.getCoursesByProvider(new Long(77), null, new Locale("es"));
	 for(Course course : courses) {
		 course.setTitle("Te cambio el titulo");
		 courseService.save(courses, new Locale("es"));
		 break;
	 }
 }*/
    
    @RequestMapping(value = "/schools", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseStatus(HttpStatus.OK)
    public void loadSchools(@RequestParam(required=false) Long providerId) 
                    throws ParserConfigurationException, SAXException, IOException, Exception {
	    	PageManager<School> paginator = new PageManager<School>();
	        long total = schoolService.countSchoolsByProvider(providerId);
	        paginator.setOffset(RANGE);
	        paginator.setStart(0);
	        paginator.setSize(total);
	        for (Integer page : paginator.getTotalPagesIterator()) {
	                int start = (page - 1) * RANGE;
	                int end = (page) * RANGE;
	                Collection<School> schools = schoolService.getSchoolsByProvider(providerId, 
	                                "updated", null, start, end);
	                for(School school : schools) {
	                	Map<String, String> params = new HashMap<String, String>();
	                	
						params.put("providerId", String.valueOf(providerId));
						params.put("schoolId", String.valueOf(school.getId()));
						
						worker.addWork("school", "/provider/school", params);
	                }
	        }
            
          
    }
    
    @RequestMapping(value = "/school", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseStatus(HttpStatus.OK)
    public void importSchool(@RequestParam(required=true) Long providerId, @RequestParam(required=true) Long schoolId,
    		@RequestParam(required=true) Long dumpId) 
                    throws ParserConfigurationException, SAXException, IOException, Exception {
    	Provider provider = null;
    	School school = null;
		Dump dump = null;
		CronTaskReport report = null;
		

		try {
			provider = providerService.getProviderById(providerId, null);
			school = schoolService.getSchool(schoolId, null);
			dump = dumpService.getDump(dumpId);
			
			report = cronTaskReportService.create();
            report.setObject_id(providerId);
            report.setDate(new Date());
            report.setType(CronTaskReport.TYPE.PROVIDER_SCHOOLS);
			report.setDescription(new StringBuffer("School: ").append(provider.getName()).toString());
			int retries = 5;
			while(retries > 0) {
		    	try {
				   providerImporter.createLoadTask(provider, school, dump);
			   	} catch(Exception e) {
			   		LOGGER.info("" + retries + " Fail importing courses: "+school.getName() );
			   		retries--;
			   		if(!(retries > 0)) {
			   			throw e;
			   		}
			   	}
			}    	
			// Set result into report
			
			report.setResult("OK");
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			report.setResult(new StringBuffer("ERROR: ").append(e.getMessage()).toString());
			throw e;
		}
        
          
    } 
    
    @RequestMapping(value = "/prueba", method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseStatus(HttpStatus.OK)
    public void prueba(@RequestParam(required=false) Long providerId) 
                    throws ParserConfigurationException, SAXException, IOException, Exception {
    	/*PageManager<Course> paginator = new PageManager<Course>();
        long total = this.courseService.countCoursesByProvider(providerId);
        paginator.setOffset(RANGE);
        paginator.setStart(0);
        paginator.setSize(total);
        LOGGER.finest("-------------------------------------------------------------------");
        for (Integer page : paginator.getTotalPagesIterator()) {
                int start = (page - 1) * RANGE;
                int end = (page) * RANGE;
                Collection<Course> courses = this.courseService.getCoursesByProvider(providerId, null, null, start, end);
                for(Course course : courses) {
                	CourseCatalog c_es = this.catalogService.getCourseCatalogByCourseId(course.getId(), new Locale("es"));
                	CourseCatalog c_eu = this.catalogService.getCourseCatalogByCourseId(course.getId(), new Locale("eu"));
                	if(c_es == null) {
                		LOGGER.finest("Id: " + course.getId());
                		LOGGER.finest("SchoolId: " + course.getSchool());
                	}
                }
        }
        LOGGER.finest("-------------------------------------------------------------------");*/
    	PageManager<Course> paginator = new PageManager<Course>();
    	School school = this.schoolService.getSchool(new Long(954), null);
        long total = this.courseService.countCoursesBySchool(school);
        paginator.setOffset(RANGE);
        paginator.setStart(0);
        paginator.setSize(total);
        LOGGER.finest("-------------------------------------------------------------------");
        for (Integer page : paginator.getTotalPagesIterator()) {
                int start = (page - 1) * RANGE;
                int end = (page) * RANGE;
	    	 Collection<Course> courses = this.courseService.getCoursesBySchool(school.getId(), null, null, start, end);
	    	 for(Course course : courses) {
	         	CourseCatalog c_es = this.catalogService.getCourseCatalogByCourseId(course.getId(), new Locale("es"));
	         	CourseCatalog c_eu = this.catalogService.getCourseCatalogByCourseId(course.getId(), new Locale("eu"));
	         	if(c_es == null) {
	         		LOGGER.finest("Id: " + course.getId());
	         		LOGGER.finest("SchoolId: " + course.getSchool());
	         	}
	         }
        }
    }  
}