package com.m4f.web.controller.model;

import java.security.Principal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.m4f.business.domain.ContactInfo;
import com.m4f.business.domain.Course;
import com.m4f.business.domain.InternalUser;
import com.m4f.business.domain.MediationService;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.School;
import com.m4f.utils.PageManager;
import com.m4f.utils.StackTraceUtil;
import com.m4f.utils.feeds.events.model.Dump;
import com.m4f.web.controller.BaseController;


@Controller
@RequestMapping("/school")
public class SchoolController extends BaseModelController {
	
	private static final Logger LOGGER = Logger.getLogger(SchoolController.class.getName());
		
	public SchoolController() {
		super();
	}
	
	@Secured("ROLE_AUTOMATIC_MEDIATOR")
	@RequestMapping(method=RequestMethod.GET)
	public String getForm(Model model, Principal principal,
			@RequestHeader("referer") String referer, HttpSession session) {
		try {
			model.addAttribute("school", 
					this.serviceLocator.getSchoolService().createSchool());
			session.setAttribute(this.REFERER_PARAM, referer);
		} catch(Exception e) {
			return this.viewHelper.errorManagement(e);
		}
		
		return "school.form";
	}
	
	@Secured("ROLE_AUTOMATIC_MEDIATOR")
	@RequestMapping(method=RequestMethod.POST)
	public String save(@ModelAttribute("school") @Valid School school, 
			BindingResult result, Principal currentUser, Locale locale, Model model,
			@RequestHeader("Host") String host, HttpSession session) {
		if (result.hasErrors()) {
			return "school.form";
		}
		try {
			if(currentUser != null) {
				MediationService mediator = 
					this.serviceLocator.getMediatorService().getMediationServiceByUser(
							this.serviceLocator.getUserService().getUser(currentUser.getName()).getId(), 
							locale);
				if(mediator == null) {
					Exception e = new Exception("The user " + currentUser.getName() + 
							" doesn't have a mediator assigned.");
					return this.viewHelper.errorManagement(e);
				}
				Provider provider = this.serviceLocator.getProviderService().findProviderByMediator(mediator, locale);
				if(provider == null) {
					Exception e = new Exception("Mediantor with id " + 
							mediator.getId() + " hasn't related provider.");
					return this.viewHelper.errorManagement(e);
				}
				school.setProvider(provider.getId());
			}
			school.setUpdated(Calendar.getInstance(new Locale("es")).getTime());
			this.serviceLocator.getSchoolService().save(school, locale);
		} catch(Exception e) {
			return this.viewHelper.errorManagement(e);
		}
		String sourceReferer = (String) session.getAttribute(this.REFERER_PARAM);
		String returnURL = this.buildReturnURL(host, sourceReferer, locale);
		return "redirect:" + returnURL;
	}
	
	@Secured("ROLE_AUTOMATIC_MEDIATOR")
	@RequestMapping(value="/edit/{schoolId}", method=RequestMethod.GET)
	public String getView(@PathVariable Long schoolId, Model model, @RequestHeader("referer") String referer, 
			HttpSession session, Locale locale) {
		try {
			School school = this.serviceLocator.getSchoolService().getSchool(schoolId, locale);
			model.addAttribute("school", school);
			session.setAttribute(this.REFERER_PARAM, referer);
		} catch (Exception e) {
			return this.viewHelper.errorManagement(e);
		}
		return "school.form";
	}
	
	@Secured({"ROLE_AUTOMATIC_MEDIATOR","ROLE_ADMIN"})
	@RequestMapping(value="/detail/{schoolId}", method=RequestMethod.GET)
	public String getDetail(@PathVariable Long schoolId, 
			Model model, Locale locale) {
		try {
			School school = this.serviceLocator.getSchoolService().getSchool(schoolId, locale);
			model.addAttribute("school", school);
		} catch (Exception e) {
			return this.viewHelper.errorManagement(e);
		}
		return "school.detail";
	}
	
	@Secured("ROLE_AUTOMATIC_MEDIATOR")
	@RequestMapping(value="/delete/{schoolId}", method=RequestMethod.GET)
	public String delete(@PathVariable Long schoolId, Model model, Locale locale,
			@RequestHeader("referer") String referer, @RequestHeader("Host") String host) {
		try {
			School school = this.serviceLocator.getSchoolService().getSchool(schoolId, locale);
			this.serviceLocator.getSchoolService().delete(school, locale);
		} catch (Exception e) {
			return this.viewHelper.errorManagement(e);
		}
		String returnURL = this.buildReturnURL(host, referer, locale);
		return "redirect:" + returnURL;
	}
	
	@Secured({"ROLE_AUTOMATIC_MEDIATOR","ROLE_ADMIN"})
	@RequestMapping(value="/{schoolId}/courses", method=RequestMethod.GET) 
	public String getCourses(@PathVariable Long schoolId,  Model model, Locale locale,
			@RequestParam(defaultValue="1", required=false) Integer page) {
		try {
			School school = this.serviceLocator.getSchoolService().getSchool(schoolId, locale);
			model.addAttribute("school", school);
			PageManager<Course> paginator = new PageManager<Course>();
			paginator.setOffset(this.getPageSize());
			paginator.setUrlBase("/" + locale.getLanguage()+ "/course/list");
			paginator.setStart((page-1)*paginator.getOffset());
			paginator.setSize(this.serviceLocator.getCourseService().countCoursesBySchool(school));
			paginator.setCollection(this.serviceLocator.getCourseService().getCoursesBySchool(school, 
					"title", locale, paginator.getStart(), paginator.getEnd()));
			model.addAttribute("paginator", paginator);
		} catch(Exception e) {
			return this.viewHelper.errorManagement(e);
		}
		return "course.list";
	}
		
	@Secured({"ROLE_AUTOMATIC_MEDIATOR","ROLE_ADMIN"})
	@RequestMapping(value="/{schoolId}/dumps", method=RequestMethod.GET)
	public String getDumps(@PathVariable Long schoolId,  Model model, Locale locale,
			@RequestParam(defaultValue="1", required=false) Integer page) {
		try {
			School school = this.serviceLocator.getSchoolService().getSchool(schoolId, locale);
			if(school == null) {
				Exception e = new Exception("School with id " + schoolId + " doesn't exist.");
				return this.viewHelper.errorManagement(e);
			}
			model.addAttribute("school", school);
			PageManager<Dump> paginator = new PageManager<Dump>();
			paginator.setOffset(this.getPageSize());
			paginator.setUrlBase("/" + locale.getLanguage() + "/school/" + 
					schoolId + "/dumps");
			paginator.setStart((page-1)*paginator.getOffset());
			paginator.setSize(this.serviceLocator.getDumpService().countDumpsByOwner(schoolId));
			paginator.setCollection(this.serviceLocator.getDumpService().
					getDumpsByOwner(schoolId, paginator.getStart(), paginator.getEnd(), null));
			model.addAttribute("paginator", paginator);
		} catch(Exception e) {
			return this.viewHelper.errorManagement(e);
		}
		return "dump.main";
	}
	

}