package com.m4f.web.controller.users;

import java.io.IOException;
import java.security.Principal;
import java.util.Locale;
import java.util.logging.Logger;

import javax.validation.Valid;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;
import com.m4f.business.domain.Course;
import com.m4f.business.domain.GlobalConfiguration;
import com.m4f.business.domain.Inbox;
import com.m4f.business.domain.InternalUser;
import com.m4f.business.domain.MediationService;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.School;
import com.m4f.business.domain.Inbox.ORIGIN;
import com.m4f.business.domain.extended.ExtendedCourse;
import com.m4f.business.domain.extended.ExtendedSchool;
import com.m4f.utils.PageManager;
import com.m4f.utils.StackTraceUtil;
import com.m4f.web.bind.form.FilterForm;
import com.m4f.web.bind.form.InboxFilterForm;
import com.m4f.web.controller.BaseController;

@Controller
@Secured("ROLE_ADMIN")
@RequestMapping("/dashboard/admin")
public class AdminController extends BaseController {

	private static final Logger LOGGER = Logger.getLogger(AdminController.class.getName());
	
	public AdminController() {
		super();
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String getView(Principal currentUser, Model model, 
			@RequestParam(required=false, defaultValue="en") String lang)
			throws IOException, ClassNotFoundException {
		return "admin.home";
	}
	
	
	@RequestMapping(value="/catalog", method=RequestMethod.GET)
	public String getIndex(Principal currentUser) {
		return "admin.catalog.index";
	}
	
	@RequestMapping(value="/catalog/schools", method=RequestMethod.GET)
	public String getSchools(Principal currentUser, Model model, Locale locale, 
			@RequestParam(defaultValue="1", required=false) Integer page, @RequestParam(defaultValue="", required=false) String order) {
		try {
			String ordering = order != null && !("").equals(order) ? order : "name";
			PageManager<School> paginator = new PageManager<School>();
			paginator.setOffset(this.getPageSize());
			paginator.setUrlBase("/" + locale.getLanguage() + "/dashboard/admin/catalog/schools");
			paginator.setStart((page-1)*paginator.getOffset());
			paginator.setSize(this.serviceLocator.getSchoolService().countSchools());
			paginator.setCollection(this.serviceLocator.getSchoolService().getSchools(ordering, locale, 
					paginator.getStart(), paginator.getEnd()));
			model.addAttribute("paginator", paginator);
			model.addAttribute("order", ordering);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			model.addAttribute("paginator", new PageManager<School>());
			//return "common.error";
		}
		return "admin.catalog.schools";
	}
	
	@RequestMapping(value="/catalog/courses", method=RequestMethod.GET)
	public String getCourses(Principal currentUser, Model model, Locale locale, 
			@RequestParam(defaultValue="1", required=false) Integer page, @RequestParam(defaultValue="", required=false) String order) {
		try {
			String ordering = order != null && !("").equals(order) ? order : "title";
			PageManager<Course> paginator = new PageManager<Course>();
			paginator.setOffset(this.getPageSize());
			paginator.setUrlBase("/" + locale.getLanguage() + "/dashboard/admin/catalog/courses");
			paginator.setStart((page-1)*paginator.getOffset());
			paginator.setSize(this.serviceLocator.getCourseService().count());
			paginator.setCollection(this.serviceLocator.getCourseService().getCourses(ordering, locale, 
					paginator.getStart(), paginator.getEnd()));
			model.addAttribute("paginator", paginator);
			model.addAttribute("order", ordering);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			model.addAttribute("paginator", new PageManager<Course>());
			//return "common.error";
		}
		return "admin.catalog.courses";
	}
	
	@RequestMapping(value="/catalog/extended/schools", method=RequestMethod.GET)
	public String getExtendedSchools(Principal currentUser, Model model, Locale locale, 
			@RequestParam(defaultValue="1", required=false) Integer page, @RequestParam(defaultValue="", required=false) String order,
			@RequestParam(required=false) Long provinceId, @RequestParam(required=false) Long regionId, @RequestParam(required=false) Long townId) {
		if(provinceId != null || regionId != null || townId != null) {
			FilterForm filterForm = new FilterForm();
			filterForm.setProvinceId(provinceId);
			filterForm.setRegionId(regionId);
			filterForm.setTownId(townId);
			this.listFilter(filterForm, model, page, order, locale);
		}
		try {
			model.addAttribute("provinces", this.serviceLocator.getTerritorialService().getAllProvinces(locale));
			String ordering = order != null && !("").equals(order) ? order : "name";
			PageManager<ExtendedSchool> paginator = new PageManager<ExtendedSchool>();
			paginator.setOffset(this.getPageSize());
			paginator.setUrlBase("/" + locale.getLanguage() + 
					"/dashboard/admin/catalog/extended/schools");
			paginator.setStart((page-1)*paginator.getOffset());
			paginator.setSize(this.serviceLocator.getExtendedSchoolService().countSchools());
			paginator.setCollection(this.serviceLocator.getExtendedSchoolService().
					getSchools(ordering, paginator.getStart(), paginator.getEnd(), locale));
			model.addAttribute("paginator", paginator);
			model.addAttribute("order", ordering);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			model.addAttribute("paginator", new PageManager<ExtendedSchool>());
			//return "common.error";
		}	
		return "admin.catalog.extended.schools";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/catalog/extended/schools", method=RequestMethod.POST)
	public String listFilter(@ModelAttribute("filterForm") FilterForm filterForm,
			Model model, @RequestParam(defaultValue="1", required=false) Integer page, 
			@RequestParam(defaultValue="", required=false) String order, Locale locale) {
		try {
			String ordering = order != null && !("").equals(order) ? order : "name";
					
			model.addAttribute("filterForm", filterForm);
			model.addAttribute("provinces", this.serviceLocator.getTerritorialService().getAllProvinces(locale));
			PageManager<ExtendedSchool> paginator = new PageManager<ExtendedSchool>();
			paginator.setOffset(this.getPageSize());
			paginator.setUrlBase("/" + locale.getLanguage() + 
					"/dashboard/admin/catalog/extended/schools");
			paginator.setStart((page-1)*paginator.getOffset());
			paginator.setStart((page-1)*paginator.getOffset());
			paginator.setSize(this.serviceLocator.getExtendedSchoolService().countSchoolsByTerritorial(filterForm.getProvinceId(),
					filterForm.getRegionId(), filterForm.getTownId()));
			paginator.setCollection(this.serviceLocator.getExtendedSchoolService().getSchoolsByTerritorial(filterForm.getProvinceId(),
					filterForm.getRegionId(), filterForm.getTownId(), ordering, paginator.getStart(), paginator.getEnd(), locale));
			model.addAttribute("paginator", paginator);
			model.addAttribute("order", ordering);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			model.addAttribute("paginator", new PageManager<ExtendedSchool>());
			return "extended.school.list";
		}	
		return "extended.school.list";
	}
	
	@RequestMapping(value="/catalog/extended/courses", method=RequestMethod.GET)
	public String list(Principal currentUser, Model model, Locale locale,
			@RequestParam(defaultValue="1", required=false) Integer page, @RequestParam(defaultValue="", required=false) String order) {
		try {
			String ordering = order != null && !("").equals(order) ? order : "title";
			PageManager<ExtendedCourse> paginator = new PageManager<ExtendedCourse>();
			paginator.setOffset(this.getPageSize());
			paginator.setUrlBase("/" + locale.getLanguage() + 
					"/dashboard/admin/catalog/extended/courses");
			paginator.setStart((page-1)*paginator.getOffset());
			paginator.setSize(this.serviceLocator.getExtendedCourseService().countCourses());
			paginator.setCollection(this.serviceLocator.getExtendedCourseService().
					getCourses(ordering, locale, paginator.getStart(), paginator.getEnd()));
			model.addAttribute("paginator", paginator);
			model.addAttribute("order", ordering);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			model.addAttribute("paginator", new PageManager<ExtendedCourse>());
			//return "common.error";
		}
		return "admin.catalog.extended.courses";
	}
	
	@RequestMapping(value="/catalog/users", method=RequestMethod.GET)
	public String getUsers(Principal currentUser, Model model, Locale locale, 
			@RequestParam(defaultValue="1", required=false) Integer page, @RequestParam(defaultValue="", required=false) String order) {
		try {
			String ordering = order != null && !("").equals(order) ? order : "";
			PageManager<InternalUser> paginator = new PageManager<InternalUser>();
			paginator.setOffset(this.getPageSize());
			paginator.setUrlBase("/" + locale.getLanguage() + 
					"/dashboard/admin/catalog/users");
			paginator.setStart((page-1)*paginator.getOffset());
			paginator.setSize(this.serviceLocator.getUserService().countUsers());
			paginator.setCollection(this.serviceLocator.getUserService().
					getUsersByRange(ordering, paginator.getStart(), paginator.getEnd()));
			model.addAttribute("paginator", paginator);
			model.addAttribute("order", ordering);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			model.addAttribute("paginator", new PageManager<InternalUser>());
			//return "common.error";
		}
		return "admin.catalog.users";
	}
	
	@RequestMapping(value="/catalog/providers", method=RequestMethod.GET)
	public String getProviders(Principal currentUser, Model model, Locale locale, 
			@RequestParam(defaultValue="1", required=false) Integer page, @RequestParam(defaultValue="", required=false) String order) {
		try {
			String ordering = order != null && !("").equals(order) ? order : "";
			PageManager<Provider> paginator = new PageManager<Provider>();
			paginator.setOffset(this.getPageSize());
			paginator.setUrlBase("/" + locale.getLanguage() + 
					"/dashboard/admin/catalog/providers");
			paginator.setStart((page-1)*paginator.getOffset());
			paginator.setSize(this.serviceLocator.getProviderService().count());
			paginator.setCollection(this.serviceLocator.getProviderService().getProviders(ordering, 
					paginator.getStart(), paginator.getEnd(), locale));
			model.addAttribute("paginator", paginator);
			model.addAttribute("order", ordering);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			model.addAttribute("paginator", new PageManager<Provider>());
			//return "common.error";
		}
		return "admin.catalog.providers";
	}
	
	@RequestMapping(value="/catalog/mediations", method=RequestMethod.GET)
	public String getMediationServices(Principal currentUser, Model model, Locale locale, 
			@RequestParam(defaultValue="1", required=false) Integer page, @RequestParam(defaultValue="", required=false) String order) {
		try {
			String ordering = order != null && !("").equals(order) ? order : "";
			PageManager<MediationService> paginator = new PageManager<MediationService>();
			paginator.setOffset(this.getPageSize());
			paginator.setUrlBase("/" + locale.getLanguage() + 
					"/dashboard/admin/catalog/mediations");
			paginator.setStart((page-1)*paginator.getOffset());
			paginator.setSize(this.serviceLocator.getMediatorService().count());
			paginator.setCollection(this.serviceLocator.getMediatorService().getMediationServices(order, 
					paginator.getStart(), paginator.getEnd(),locale));
			model.addAttribute("paginator", paginator);
			model.addAttribute("order", ordering);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			model.addAttribute("paginator", new PageManager<MediationService>());
			//return "common.error";
		}
		return "admin.catalog.mediations";
	}
	
	@RequestMapping(value="/territorial", method=RequestMethod.GET)
	public String getTerritorialHome() {
		return "admin.territorial.index";
	}
		
	
	@RequestMapping(value="/suggestions")
	public String getSuggestions(@ModelAttribute("filter") InboxFilterForm filter, Principal currentUser, Model model, Locale locale,
			@RequestParam(defaultValue="1", required=false) Integer page, @RequestParam(defaultValue="", required=false) String order,
			@RequestParam(defaultValue="", required=false) String readedFilter,
			@RequestParam(defaultValue="", required=false) String userFilter) {
		try {
			String ordering = order != null && !("").equals(order) ? order : "-created";
			PageManager<Inbox> paginator = new PageManager<Inbox>();
			paginator.setOffset(this.getPageSize());
			paginator.setUrlBase("/" + locale.getLanguage() + 
					"/dashboard/admin/suggestions");
			paginator.setStart((page-1)*paginator.getOffset());
			
			if(filter.getUser() == null && userFilter != null && !("").equals(userFilter)) {
				for(Inbox.USER user : Inbox.USER.values()) {
					if(user.getValue().equals(userFilter)) {
						filter.setUser(user);
					}
				}
			}
			
			paginator.setSize(this.serviceLocator.getInboxService().count(true, 
					filter.getReaded() != null && filter.getReaded() ? !filter.getReaded() : null , null, ORIGIN.REQUEST, filter.getUser()));
			paginator.setCollection(this.serviceLocator.getInboxService().getAllInbox(true, 
					filter.getReaded() != null && filter.getReaded() ? !filter.getReaded() : null , null, ORIGIN.REQUEST, filter.getUser(),
					locale, paginator.getStart(), paginator.getEnd(), ordering));
			
			model.addAttribute("paginator", paginator);
			model.addAttribute("order", ordering);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "admin.suggestions.index";
	}
	
	@RequestMapping(value="/configuration", method=RequestMethod.GET)
	public String getConfiguration(Principal currentUser, Model model, Locale locale) {
		try {
			model.addAttribute("configuration", 
					this.serviceLocator.getAppConfigurationService().getGlobalConfiguration());
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "admin.configuration.index";
	}
	
	@RequestMapping(value="/configuration", method=RequestMethod.POST)
	public String setConfiguration(@ModelAttribute("configuration") @Valid GlobalConfiguration configuration, 
			BindingResult result, Principal currentUser, Model model, Locale locale) {
		if(result.hasErrors()) {
			return "admin.configuration.index";
		}
		try {
			this.serviceLocator.getAppConfigurationService().deleteGlobalConfiguration();
			this.serviceLocator.getAppConfigurationService().saveGlobalConfiguration(configuration);
			model.addAttribute("configuration", this.serviceLocator.getAppConfigurationService().getGlobalConfiguration());
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "admin.configuration.index";
	}
	
	@RequestMapping(value="/configuration/reset", method=RequestMethod.GET)
	public String resetConfiguration(Principal currentUser, Model model, Locale locale) {
		try {
			this.serviceLocator.getAppConfigurationService().deleteGlobalConfiguration();
			model.addAttribute("configuration", this.serviceLocator.getAppConfigurationService().getGlobalConfiguration());
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "redirect:/" + locale.getLanguage() + "/dashboard/admin/configuration/";
	}
	
	@ModelAttribute("filterForm")
	public FilterForm getFilterForm() {
		return new FilterForm();
	}
	
	/*
	 * MANAGEMENT
	 */
	@RequestMapping(value="/management/delete/provider/{providerId}", method=RequestMethod.GET)
	public String deleteProvider(@PathVariable Long providerId, Locale locale) {
		try {
			Provider provider = this.serviceLocator.getProviderService().getProviderById(providerId, locale);
			if(provider != null) {
				this.serviceLocator.getProviderService().deleteProvider(provider, locale);
			}
			
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		Queue queue = QueueFactory.getQueue(this.PROVIDER_QUEUE);
		TaskOptions options = TaskOptions.Builder.withUrl("/task/management/delete/provider");
		options.param("providerId", String.valueOf(providerId));
		options.method(Method.POST);
		queue.add(options);	
		
		return new StringBuffer("redirect:/").append(locale.getLanguage()).append("/dashboard/admin/catalog/providers").toString();
	}
	
	
	/*
	 * TEST
	 */
	@RequestMapping(value="/test", method=RequestMethod.GET)
	public String doTest(@RequestParam String search, Model model, Locale locale) {
		try {
			String searchParam = search != null && !("").equals(search) ? search : "anboto";
			String ordering = "name";
			PageManager<School> paginator = new PageManager<School>();
			paginator.setOffset(this.getPageSize());
			paginator.setUrlBase("/");
			paginator.setStart(0);
			paginator.setSize(10);
			paginator.setCollection(this.serviceLocator.getSchoolService().findByName(searchParam, locale));
			model.addAttribute("paginator", paginator);
			model.addAttribute("order", ordering);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "admin.catalog.schools";
	}
}