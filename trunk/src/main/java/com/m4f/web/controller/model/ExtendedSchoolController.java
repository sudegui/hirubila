package com.m4f.web.controller.model;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.m4f.business.domain.Course;
import com.m4f.business.domain.extended.ExtendedCourse;
import com.m4f.business.domain.extended.ExtendedSchool;
import com.m4f.business.domain.extended.Town;
import com.m4f.utils.PageManager;
import com.m4f.utils.StackTraceUtil;

@Controller
@RequestMapping("/extended/school")
public class ExtendedSchoolController extends BaseModelController {

	private static final Logger LOGGER = Logger.getLogger(ExtendedSchoolController.class.getName());
	
	public ExtendedSchoolController() {
		super();
	}
	
	@Secured("ROLE_MANUAL_MEDIATOR")
	@RequestMapping(method=RequestMethod.GET)
	public String getForm(Principal user, Model model, 
			@RequestHeader("referer") String referer, HttpSession session) {
		try {
			ExtendedSchool school = this.serviceLocator.getExtendedSchoolService().createSchool();
			school.setCreated(Calendar.getInstance(new Locale("es")).getTime());
			model.addAttribute("school", school);
			session.setAttribute(this.REFERER_PARAM, referer);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "extended.school.form";
	}
	
	@Secured("ROLE_MANUAL_MEDIATOR")
	@RequestMapping(method=RequestMethod.POST)
	public String save(@ModelAttribute("school") @Valid ExtendedSchool school, 
			BindingResult result, Principal currentUser, Locale locale, Model model,
			@RequestHeader("Host") String host, HttpSession session) {
		if (result.hasErrors()) {
			return "extended.school.form";
		}
		try {
			school.setUpdated(Calendar.getInstance(new Locale("es")).getTime());
			this.serviceLocator.getExtendedSchoolService().save(school, locale);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		String sourceReferer = (String) session.getAttribute(this.REFERER_PARAM);
		String returnURL = this.buildReturnURL(host, sourceReferer, locale);
		return "redirect:" + returnURL;
	}
	
	@Secured("ROLE_MANUAL_MEDIATOR")
	@RequestMapping(value="/edit/{schoolId}", method=RequestMethod.GET)
	public String getView(@PathVariable Long schoolId, Model model,
			Locale locale, @RequestHeader("referer") String referer, 
			HttpSession session) {
		try {
			ExtendedSchool school = this.serviceLocator.getExtendedSchoolService().getSchool(schoolId, locale);
			model.addAttribute("school", school);
			session.setAttribute(this.REFERER_PARAM, referer);
		} catch (Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "extended.school.form";
	}
	
	@Secured({"ROLE_MANUAL_MEDIATOR","ROLE_ADMIN"})
	@RequestMapping(value="/detail/{schoolId}", method=RequestMethod.GET)
	public String getDetail(@PathVariable Long schoolId, 
			Model model, Locale locale) {
		try {
			ExtendedSchool school = this.serviceLocator.getExtendedSchoolService().getSchool(schoolId, locale);
			model.addAttribute("school", school);
		} catch (Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "extended.school.detail";
	}
	
	@Secured("ROLE_MANUAL_MEDIATOR")
	@RequestMapping(value="/delete/{schoolId}", method=RequestMethod.GET)
	public String delete(@PathVariable Long schoolId, Model model, Locale locale, 
			@RequestHeader("referer") String referer, @RequestHeader("Host") String host) {
		try {
			ExtendedSchool school = this.serviceLocator.getExtendedSchoolService().getSchool(schoolId, locale);
			this.serviceLocator.getExtendedSchoolService().delete(school, locale);
		} catch (Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		String returnURL = this.buildReturnURL(host, referer, locale);
		return "redirect:" + returnURL;
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/delete/all", method=RequestMethod.GET)
	public String deleteAll(Locale locale, @RequestHeader("referer") String referer,
			@RequestHeader("Host") String host) {
		try {
			this.serviceLocator.getExtendedSchoolService().deleteAll(locale);
		} catch (Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "redirect:/" + locale.getLanguage() + "/extended/school/list";
	}
	
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public String list(Model model, Locale locale, 
			@RequestParam(defaultValue="1", required=false) Integer page, @RequestParam(defaultValue="", required=false) String order) {
		try {
			String ordering = order != null && !("").equals(order) ? order : "name";
			PageManager<ExtendedSchool> paginator = new PageManager<ExtendedSchool>();
			paginator.setOffset(this.getPageSize());
			paginator.setUrlBase("/" + locale.getLanguage()+ "/extended/school/list");
			paginator.setStart((page-1)*paginator.getOffset());
			paginator.setSize(this.serviceLocator.getExtendedSchoolService().countSchools());
			paginator.setCollection(this.serviceLocator.getExtendedSchoolService().getSchools(ordering, 
					paginator.getStart(), paginator.getEnd(), locale));
			model.addAttribute("paginator", paginator);
			model.addAttribute("order", ordering);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			model.addAttribute("paginator", new PageManager<ExtendedSchool>());
			return "extended.school.list";
		}	
		return "extended.school.list";
	}
	
	@Secured({"ROLE_MANUAL_MEDIATOR","ROLE_ADMIN"})
	@RequestMapping(value="/{schoolId}/courses", method=RequestMethod.GET)
	public String getCourses(@PathVariable Long schoolId,  Model model, Locale locale,
			@RequestParam(defaultValue="1", required=false) Integer page,@RequestParam(defaultValue="", required=false) String order) {
		try {
			String ordering = order != null && !("").equals(order) ? order : "title";
			ExtendedSchool school = this.serviceLocator.getExtendedSchoolService().getSchool(schoolId, locale);
			Collection<ExtendedCourse> courses = this.serviceLocator.getExtendedCourseService().getCoursesBySchool(school.getId(), "title", locale);		
			model.addAttribute("school", school);
			model.addAttribute("courses", courses);		
			PageManager<ExtendedCourse> paginator = new PageManager<ExtendedCourse>();
			paginator.setOffset(this.getPageSize());
			paginator.setUrlBase("/" + locale.getLanguage()+ "/extended/school/" + 
					schoolId + "/courses");
			paginator.setStart((page-1)*paginator.getOffset());
			paginator.setSize(this.serviceLocator.getExtendedCourseService().countCoursesBySchool(school));
			paginator.setCollection(this.serviceLocator.getExtendedCourseService().getCoursesBySchool(school, ordering, locale, 
					paginator.getStart(), paginator.getEnd()));
			model.addAttribute("paginator", paginator);
			model.addAttribute("order", ordering);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			model.addAttribute("paginator", new PageManager<ExtendedCourse>());
			//return "common.error";
		}
		//return "extended.course.list";
		return "admin.catalog.extended.courses";
	}
		
	
	/**
	 * AJAX
	 */
	@RequestMapping(value="/towns/json", method=RequestMethod.GET)
	public @ResponseBody List<Town> listJson(@RequestParam(required=true, defaultValue="") String query, Model model, Locale locale) {
		try {
			return this.serviceLocator.getTerritorialService().findTownsByName(query, locale);
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
			return new ArrayList<Town>();
		}
	}
	
}