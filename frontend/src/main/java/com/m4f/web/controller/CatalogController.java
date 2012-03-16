package com.m4f.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.m4f.business.domain.Course;
import com.m4f.business.domain.CourseCatalog;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.School;
import com.m4f.business.domain.extended.Province;
import com.m4f.business.domain.extended.Region;
import com.m4f.business.domain.extended.Town;
import com.m4f.utils.PageManager;
import com.m4f.utils.StackTraceUtil;
import org.springframework.http.HttpStatus;
import com.m4f.web.controller.exception.GenericException;

@Controller
@RequestMapping("/catalog")
public class CatalogController extends BaseController {
	
	private static final Logger LOGGER = Logger.getLogger(CatalogController.class.getName());
	private static final String ORDERING_PROPERTY = "id";
	
	
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
	public String listReglated(Model model, Locale locale,
			@RequestParam(defaultValue="1", required=false) Integer page) throws GenericException {
		try {
			PageManager<Course> paginator = new PageManager<Course>();
			paginator.setOffset(this.getPageSize());
			paginator.setUrlBase("/" + locale.getLanguage()+ "/catalog/reglated/course/list");
			paginator.setStart((page-1)*paginator.getOffset());
			paginator.setSize(this.serviceLocator.getCourseService().countCourses(true, locale));
			paginator.setCollection(this.serviceLocator.getCourseService().getCourses(true, 
					"-" + ORDERING_PROPERTY, locale, paginator.getStart(), paginator.getEnd()));
			model.addAttribute("paginator", paginator);
			model.addAttribute("type", "reglated");
		} catch(Exception e) {
			throw new GenericException(e);
		}
		return "catalog.course.list";
	}
	
	@RequestMapping(value="/non-reglated/course/list", method=RequestMethod.GET)
	public String listNonReglated(Model model, Locale locale,
			@RequestParam(defaultValue="1", required=false) Integer page) throws GenericException {
		try {
			PageManager<Course> paginator = new PageManager<Course>();
			paginator.setOffset(this.getPageSize());
			paginator.setUrlBase("/" + locale.getLanguage()+ "/catalog/non-reglated/course/list");
			paginator.setStart((page-1)*paginator.getOffset());
			paginator.setSize(this.serviceLocator.getCourseService().countCourses(false, locale));
			paginator.setCollection(this.serviceLocator.getCourseService().getCourses(false, 
					"-" + ORDERING_PROPERTY, locale, paginator.getStart(), paginator.getEnd()));
			model.addAttribute("paginator", paginator);
			model.addAttribute("type", "non-reglated");
		} catch(Exception e) {
			throw new GenericException(e);
		}
		return "catalog.course.list";
	}
	
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
	
	@RequestMapping(value="/cleanCache", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void cleanCache(Locale locale) throws GenericException {
		String[] cacheNames =  {"courses", "schools", "coursesCatalog"};
    	for(String cacheName : cacheNames) {
    		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService(cacheName);
    		syncCache.clearAll();
    	}
    	MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.clearAll();    
	}
	
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
			
			Course course = this.serviceLocator.getCourseService().getCourse(courseId, locale);
			School school = this.serviceLocator.getSchoolService().getSchool(course.getSchool(), locale);
			Provider provider = this.serviceLocator.getProviderService().getProviderById(school.getProvider(), locale);
			
			String townName = school.getContactInfo() != null && 
					school.getContactInfo().getCity() != null ? 
					school.getContactInfo().getCity() : "";
			
			
			Town town = this.getTownByName(townName, locale);
			Province province = new Province();
			Region region = new Region();
			
			if(town != null && town.getId() != null) {
				region = this.getRegionsMap().get(locale.getLanguage()).get(town.getRegion());
				province = this.getProvincesMap().get(locale.getLanguage()).get(town.getProvince());
			} else {
				town = new Town();
			}
			
			
			// Metadata
			StringBuffer keyWords = new StringBuffer();
			for(Category tag : course.getTags()) {
				keyWords.append(tag.getCategory()).append(",");
			}
			
			model.addAttribute("provider", provider);
			model.addAttribute("school", school);
			model.addAttribute("course", course);
			model.addAttribute("province", province);
			model.addAttribute("region", region);
			model.addAttribute("town", town);
			model.addAttribute("tags", keyWords.toString());
			
			response.addDateHeader("Last-Modified", course.getStart().getTime());
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
	
	/* 
	 * Methods for caching territorial data
	 */
	private Map<String, Map<Long, Province>> getProvincesMap() throws Exception {
		Map<String, Map<Long,Province>> provincesMap = new HashMap<String, Map<Long,Province>>();	
		for(Locale locale :  this.serviceLocator.getAppConfigurationService().getLocales()) {
			provincesMap.put(locale.getLanguage(), this.serviceLocator.getTerritorialService().getProvincesMap(locale));
		}
		return provincesMap;
	}
	
	private Map<String, Map<Long, Region>> getRegionsMap() throws Exception {
		Map<String, Map<Long,Region>> regionsMap = new HashMap<String, Map<Long,Region>>();
		for(Locale locale : this.serviceLocator.getAppConfigurationService().getLocales()) {
			regionsMap.put(locale.getLanguage(), this.serviceLocator.getTerritorialService().getRegionsMap(locale));
		}
		return regionsMap;
	}
	
	public Town getTownByName(String name, Locale locale) throws Exception {
		HashMap<Locale, HashMap<String, Town>> towns;
		MemcacheService syncCache = null;
		try {
			syncCache = MemcacheServiceFactory.getMemcacheService();
		} catch(Exception e) {
			LOGGER.severe("Error getting cache for towns by name map: ");
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
		} 
		
		boolean cached = false;
		
		towns = (HashMap<Locale, HashMap<String, Town>>) syncCache.get("townsByName");
		if(towns == null) {
			towns = new HashMap<Locale, HashMap<String, Town>>();
		}
		
		HashMap<String, Town> townsByName = towns.get(locale);
		if(townsByName == null) {
			townsByName = new HashMap<String, Town>();
		}
		
		Town town = townsByName.get(name);
		if(town == null) {
			List<Town> all = this.serviceLocator.getTerritorialService().findTownsByName(name, locale);
			if(towns != null && towns.size() > 0) {
				town = all.get(0);
				townsByName.put(name, town);
				towns.put(locale, townsByName);
			}
		} else {
			cached = true;
		}
		
		if(!cached) {
			syncCache.put("townsByName", towns);
		}
		
		return town;
	}
}