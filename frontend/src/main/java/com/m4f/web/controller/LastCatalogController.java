package com.m4f.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.appengine.api.NamespaceManager;
import com.google.appengine.api.datastore.Category;
import com.google.appengine.api.memcache.InvalidValueException;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;
import com.m4f.business.domain.Course;
import com.m4f.business.domain.CourseCatalog;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.School;
import com.m4f.business.domain.extended.FeedCourses;
import com.m4f.business.domain.extended.FeedSchools;
import com.m4f.business.domain.extended.Province;
import com.m4f.business.domain.extended.Region;
import com.m4f.business.domain.extended.Town;
import com.m4f.business.service.exception.ContextNotActiveException;
import com.m4f.business.service.exception.ServiceNotFoundException;
import com.m4f.utils.PageManager;
import com.m4f.utils.StackTraceUtil;
import org.springframework.http.HttpStatus;
import com.m4f.web.controller.exception.GenericException;

@Controller
@RequestMapping("/catalog")
public class LastCatalogController extends BaseController {
	
	private static final Logger LOGGER = Logger.getLogger(LastCatalogController.class.getName());
	private static final String ORDERING_PROPERTY = "updated";
	
	
	@RequestMapping(method=RequestMethod.GET)
	public String getIndex() {
		return "catalog.index";
	}
	
	/**
	 * COURSES CATALOG
	 */
	/*@RequestMapping(value="/reglated/course/list", method=RequestMethod.GET)
	public String listReglated(Model model, Locale locale,
			@RequestParam(defaultValue="1", required=false) Integer page) throws GenericException {
		try {
			PageManager<CourseCatalog> paginator = new PageManager<CourseCatalog>();
			paginator.setOffset(this.getPageSize());
			paginator.setUrlBase("/" + locale.getLanguage()+ "/catalog/reglated/course/list");
			paginator.setStart((page-1)*paginator.getOffset());
			paginator.setSize(this.serviceLocator.getCatalogService().countCourseCatalog(true, locale));
			paginator.setCollection(this.serviceLocator.getCatalogService().getCoursesCatalog(true, 
					"-" + ORDERING_PROPERTY, locale, paginator.getStart(), paginator.getEnd()));
			model.addAttribute("paginator", paginator);
			model.addAttribute("type", "reglated");
		} catch(Exception e) {
			throw new GenericException(e);
		}
		return "catalog.course.list";
	}*/
	
	/*@RequestMapping(value="/non-reglated/course/list", method=RequestMethod.GET)
	public String listNonReglated(Model model, Locale locale,
			@RequestParam(defaultValue="1", required=false) Integer page) throws GenericException {
		try {
			PageManager<CourseCatalog> paginator = new PageManager<CourseCatalog>();
			paginator.setOffset(this.getPageSize());
			paginator.setUrlBase("/" + locale.getLanguage()+ "/catalog/non-reglated/course/list");
			paginator.setStart((page-1)*paginator.getOffset());
			paginator.setSize(this.serviceLocator.getCatalogService().countCourseCatalog(false, locale));
			paginator.setCollection(this.serviceLocator.getCatalogService().getCoursesCatalog(false, 
					"-" + ORDERING_PROPERTY, locale, paginator.getStart(), paginator.getEnd()));
			model.addAttribute("paginator", paginator);
			model.addAttribute("type", "non-reglated");
		} catch(Exception e) {
			throw new GenericException(e);
		}
		return "catalog.course.list";
	}*/
	@RequestMapping(value="/reglated/course/list", method=RequestMethod.GET)
	public String listReglated(Model model, Locale locale, HttpServletResponse response,
			@RequestParam(defaultValue="1", required=false) Integer page) throws GenericException {
		try {
			PageManager<Course> paginator = new PageManager<Course>();
			paginator.setOffset(this.getPageSize());
			paginator.setUrlBase("/" + locale.getLanguage()+ "/catalog/reglated/course/list");
			
			Calendar calendar = this.getCalendar();
						
			LOGGER.severe("Date: " + calendar.getTime());
			
			paginator.setSize(this.serviceLocator.getCourseService().countUpdatedCourses(calendar.getTime(), true));
			if((page-1)*paginator.getOffset() > paginator.getSize()) {
				throw new GenericException("Paginator Out of Range!!! Size: " + paginator.getSize() + " start: " + (page-1)*paginator.getOffset()); 
			}
			paginator.setStart((page-1)*paginator.getOffset());
			paginator.setCollection(this.serviceLocator.getCourseService().getUpdatedCourses(calendar.getTime(), true, 
					"-" + ORDERING_PROPERTY, locale, paginator.getStart(), paginator.getEnd()));
			/*paginator.setCollection(this.serviceLocator.getCourseService().getCourses(true, 
					"-" + ORDERING_PROPERTY, locale, paginator.getStart(), paginator.getEnd())); */
			model.addAttribute("paginator", paginator);
			model.addAttribute("type", "reglated");
			
			response.addDateHeader("Last-Modified", calendar.getTimeInMillis());
		} catch(Exception e) {
			throw new GenericException(e);
		}
		return "catalog.course.list";
	}
	
	@RequestMapping(value="/non-reglated/course/list", method=RequestMethod.GET)
	public String listNonReglated(Model model, Locale locale, HttpServletResponse response,
			@RequestParam(defaultValue="1", required=false) Integer page) throws GenericException {
		try {
			
			PageManager<Course> paginator = new PageManager<Course>();
			paginator.setOffset(this.getPageSize());
			paginator.setUrlBase("/" + locale.getLanguage()+ "/catalog/non-reglated/course/list");
			
			Calendar calendar = this.getCalendar();
									
			LOGGER.severe("Date: " + calendar.getTime());
			
			paginator.setSize(this.serviceLocator.getCourseService().countUpdatedCourses(calendar.getTime(), false));
			if((page-1)*paginator.getOffset() > paginator.getSize()) {
				throw new GenericException("Paginator Out of Range!!! Size: " + paginator.getSize() + " start: " + (page-1)*paginator.getOffset()); 
			}
			paginator.setStart((page-1)*paginator.getOffset());
			paginator.setCollection(this.serviceLocator.getCourseService().getUpdatedCourses(calendar.getTime(), false, 
					"-" + ORDERING_PROPERTY, locale, paginator.getStart(), paginator.getEnd()));
			
			model.addAttribute("paginator", paginator);
			model.addAttribute("type", "non-reglated");
			
			response.addDateHeader("Last-Modified", calendar.getTimeInMillis());
		} catch(Exception e) {
			throw new GenericException(e);
		}
		return "catalog.course.list";
	} 
/*	
	 @RequestMapping(value="/non-reglated/course/list", method=RequestMethod.GET)
		public String listNonReglated(Model model, Locale locale, HttpServletResponse response,
				@RequestParam(defaultValue="1", required=false) Integer page) throws GenericException {
			try {
				
				PageManager<Course> paginator = new PageManager<Course>();
				paginator.setOffset(this.getPageSize());
				paginator.setUrlBase("/" + locale.getLanguage()+ "/catalog/non-reglated/course/list");
				
				Calendar calendar = this.getCalendar();
											
				LOGGER.severe("Date: " + calendar.getTime());
				
				paginator.setSize(this.serviceLocator.getCourseService().countCoursesByProvider(new Long(715431)));
				if((page-1)*paginator.getOffset() > paginator.getSize()) {
					throw new GenericException("Paginator Out of Range!!! Size: " + paginator.getSize() + " start: " + (page-1)*paginator.getOffset()); 
				}
				paginator.setStart((page-1)*paginator.getOffset());
				paginator.setCollection(this.serviceLocator.getCourseService().getCoursesByProvider(new Long(715431), "id", locale, paginator.getStart(), paginator.getEnd()));
				
				model.addAttribute("paginator", paginator);
				model.addAttribute("type", "non-reglated");
				
				response.addDateHeader("Last-Modified", calendar.getTimeInMillis());
			} catch(Exception e) {
				throw new GenericException(e);
			}
			return "catalog.course.list";
		}
*/	
	@RequestMapping(value="/reglated/course/detail/{courseId}", method=RequestMethod.GET)
	public String reglatedDetail(@PathVariable Long courseId, Model model, Locale locale, 
			HttpServletResponse response) throws GenericException {
		return this.redirectToCourseDetail(response, courseId, model, locale);
	}
	
	@RequestMapping(value="/non-reglated/course/detail/{courseId}", method=RequestMethod.GET)
	public String nonReglatedDetail(@PathVariable Long courseId, Model model, Locale locale, 
			HttpServletResponse response) throws GenericException {
		return this.redirectToCourseDetail(response, courseId, model, locale);
	}
	
	@RequestMapping(value="/course/detail/{courseId}", method=RequestMethod.GET)
	public String detail(@PathVariable Long courseId, Model model, 
			Locale locale, HttpServletResponse response) throws GenericException {
		return this.redirectToCourseDetail(response, courseId, model, locale);
	}
	
	/*@RequestMapping(value="/cleanCache", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void cleanCache(Locale locale) throws GenericException {
		String[] cacheNames =  {"courses", "schools", "coursesCatalog"};
    	for(String cacheName : cacheNames) {
    		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService(cacheName);
    		syncCache.clearAll();
    	}
    	MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.clearAll();    
	}*/
	
	/*private String redirectToCourseDetail(HttpServletResponse response, 
			Long courseId, Model model, Locale locale) throws GenericException {
		try {
			CourseCatalog courseCatalog = 
				this.serviceLocator.getCatalogService().getCourseCatalogByCourseId(courseId, locale);
			model.addAttribute("course", courseCatalog);
			response.addDateHeader("Last-Modified", courseCatalog.getStart().getTime());
		} catch(Exception e) {
			throw new GenericException(e);
		}
		return "search.result.detail";
	}*/
	
	private String redirectToCourseDetail(HttpServletResponse response, 
		Long courseId, Model model, Locale locale) throws GenericException {
		try {
			HashMap<String, Object> courseData = this.getDataFromCache(courseId, locale);
			Course course = (Course) courseData.get("course");
			
			model.addAttribute("provider", courseData.get("provider"));
			model.addAttribute("school", courseData.get("school"));
			model.addAttribute("course", course);
			model.addAttribute("province", courseData.get("province"));
			model.addAttribute("region", courseData.get("region"));
			model.addAttribute("town", courseData.get("town"));
			model.addAttribute("tags", courseData.get("tags"));
			
			response.addDateHeader("Last-Modified", course.getStart() != null ? course.getStart().getTime() : course.getUpdated().getTime());
		} catch(Exception e) {
			throw new GenericException(e);
		}
		return "search.result.detail";
	}
	
	@ExceptionHandler(GenericException.class)
	public String handleCatalogException(Exception ex, 
			HttpServletResponse response)  throws IOException {
		LOGGER.severe(StackTraceUtil.getStackTrace(ex));
		response.sendError(HttpStatus.NOT_FOUND.value());
		return "common.error";
	}
	
	private HashMap<String, Object> getDataFromCache(Long courseId, Locale locale) throws Exception {
		HashMap<Long, HashMap<String, Object>> courses = null;
		HashMap<String, Object> courseData = null;
		MemcacheService syncCache = null;
		boolean cached = false;
		
		try {
			syncCache = MemcacheServiceFactory.getMemcacheService("test-"+locale.getLanguage());
		} catch(Exception e) {
			LOGGER.severe("Error getting cache for towns by name map: ");
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
		}
		
		try {
            courses = (HashMap<Long, HashMap<String, Object>>)syncCache.get("coursesData");
	    } catch(InvalidValueException e) {
	           LOGGER.severe(StackTraceUtil.getStackTrace(e));
	    }
		
		if(courses == null) {
			courses = new HashMap<Long, HashMap<String, Object>>();
		}
		
		courseData = courses.get(courseId);
		if(courseData == null) {
			courseData = new HashMap<String, Object>();
			Course course = this.serviceLocator.getCourseService().getCourse(courseId, locale);
			School school = this.serviceLocator.getSchoolService().getSchool(course.getSchool(), locale);
			Provider provider = this.serviceLocator.getProviderService().getProviderById(school.getProvider(), locale);
			
			String townName = school.getContactInfo() != null && 
					school.getContactInfo().getCity() != null ? 
					school.getContactInfo().getCity() : "";
			

			//Town town = this.getTownByName(townName, locale);

			Town town = this.serviceLocator.getTerritorialService().getTownsMap(locale).get(townName);
			
			Province province = new Province();
			Region region = new Region();
			
			if(town != null && town.getId() != null) {
				region = this.serviceLocator.getTerritorialService().getRegionsMap(locale).get(town.getRegion());
				province = this.serviceLocator.getTerritorialService().getProvincesMap(locale).get(town.getProvince());
			} else {
				town = new Town();
			}
			
			
			// Metadata
			StringBuffer keyWords = new StringBuffer();
			for(Category tag : course.getTags()) {
				keyWords.append(tag.getCategory()).append(",");
			}
			
			// Create courseData
			courseData.put("provider", provider);
			courseData.put("school", school);
			courseData.put("course", course);
			courseData.put("province", province);
			courseData.put("region", region);
			courseData.put("town", town);
			courseData.put("tags", keyWords.toString());
			
			courses.put(courseId, courseData);
			
			//Store in cache
			syncCache.put("coursesData", courses);
			
		} else {
			LOGGER.log(Level.INFO, "CourseData from cache!!!!!!!");
		}
		
		return courseData;
	}
	
	private Calendar getCalendar() {
		final long referenceTime = 15 /* days */ * 24 /* hours/day */ * 60 /* minutes/day */ * 60 /* seconds/minute */ * 1000 /* miliseconds/second */;
		Calendar calendar = Calendar.getInstance();
		
		// Reset calendar to a known time 00:00:00:000
		calendar.set(Calendar.HOUR_OF_DAY, 0); // 0 hours in the day
		calendar.set(Calendar.MINUTE, 0); // 0 minutes
		calendar.set(Calendar.SECOND, 0); // 0 seconds
		calendar.set(Calendar.MILLISECOND, 0); // O miliseconds		
		
		calendar.setTimeInMillis(calendar.getTimeInMillis() - referenceTime);
		
		return calendar;
	}
	
	/*
	@RequestMapping(value="/test", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void test(){
		Queue queue = QueueFactory.getDefaultQueue();
		TaskOptions options = TaskOptions.Builder.withUrl("/es/catalog/dotest");
		options.method(Method.GET);
	    queue.add(options);
	}
	
	@RequestMapping(value="/dotest", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void doTest(HttpServletResponse response) throws Exception {
		
		long total = this.serviceLocator.getCourseService().countCoursesByProvider(new Long(127297));
		Collection<Course> courses = this.serviceLocator.getCourseService().getCoursesByProvider(new Long(127297), "", null);
		
		StringBuffer sb = new StringBuffer("Total: ").append(total).append("\n");
		for(Course c : courses) {
			
			sb.append("http://hirubila.appspot.com/es/catalog/non-reglated/course/detail/"+c.getId());
			sb.append("\n");
			sb.append(c.toString() + " -> active: "+ c.getActive() + " Regulated: -> " + c.getRegulated());
			sb.append("\n");
		}
		
		LOGGER.log(Level.SEVERE, sb.toString());
	}*/

}