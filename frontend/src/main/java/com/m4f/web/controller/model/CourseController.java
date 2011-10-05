package com.m4f.web.controller.model;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
import com.google.appengine.api.datastore.Category;
import com.m4f.business.domain.Course;
import com.m4f.business.domain.InternalUser;
import com.m4f.business.domain.MediationService;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.School;
import com.m4f.utils.StackTraceUtil;

@Controller
@RequestMapping("/course")
public class CourseController extends BaseModelController {
	private static final Logger LOGGER = Logger.getLogger(CourseController.class.getName());
	
	public CourseController() {
		super();
	}
	
	@Secured("ROLE_AUTOMATIC_MEDIATOR")
	@RequestMapping(method=RequestMethod.GET)
	public String getForm(Principal currentUser, Model model, Locale locale,
			@RequestHeader(required=false,value="referer") String referer, 
			HttpSession session) {
		try {
			Provider provider = this.getProvider(currentUser, locale);
			Course course = this.serviceLocator.getCourseService().createCourse();
			course.setProvider(provider.getId());
			model.addAttribute("course", course);
			session.setAttribute(this.REFERER_PARAM, referer);
		} catch(Exception e) {
			return this.viewHelper.errorManagement(e);
		}
		return "course.form";
	}
	
	@Secured("ROLE_AUTOMATIC_MEDIATOR")
	@RequestMapping(value="/edit/{courseId}", method=RequestMethod.GET)
	public String edit(@PathVariable Long courseId, Model model, Locale locale,
			@RequestHeader(required=false,value="referer") String referer, HttpSession session) {
		try {
			Course course =  this.serviceLocator.getCourseService().getCourse(courseId, locale);
			model.addAttribute("course", course);
			model.addAttribute("school", this.serviceLocator.getSchoolService().getSchool(course.getSchool(), locale));
			session.setAttribute(this.REFERER_PARAM, referer);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "course.form";
	}
	
	@Secured("ROLE_AUTOMATIC_MEDIATOR")
	@RequestMapping(method=RequestMethod.POST)
	public String save(@ModelAttribute("course") @Valid Course course, 
			BindingResult result, Principal principal, Locale locale, Model model,
			@RequestHeader("Host") String host, HttpSession session) {
		try {
			if(result.hasErrors()) {
				return "course.form";
			}
			this.serviceLocator.getCourseService().save(course, locale);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
		}
		String returnURL = this.buildReturnURL(host, 
				(String) session.getAttribute(this.REFERER_PARAM), locale);
		if(!returnURL.endsWith("/")) returnURL += "/";
		return "redirect:" + returnURL;
	}
	
	
	
	@Secured("ROLE_AUTOMATIC_MEDIATOR")
	@RequestMapping(value="/delete/{courseId}", method=RequestMethod.GET)
	public String delete(@PathVariable Long courseId, Locale locale, 
			@RequestHeader("referer") String referer, @RequestHeader("Host") String host) {
		try {
			Course course = this.serviceLocator.getCourseService().getCourse(courseId, locale);
			this.serviceLocator.getCourseService().deleteLogic(course, locale);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "redirect:" + this.buildReturnURL(host, referer, locale);
	}
	
	@Secured({"ROLE_AUTOMATIC_MEDIATOR","ROLE_ADMIN"})
	@RequestMapping(value="/detail/{courseId}", method=RequestMethod.GET)
	public String detail(@PathVariable Long courseId, Model model, Locale locale) {
		try {
			Course course = this.serviceLocator.getCourseService().getCourse(courseId, locale);
			School school = this.serviceLocator.getSchoolService().getSchool(course.getSchool(), locale);
			model.addAttribute("course", course);
			model.addAttribute("school", school);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "course.detail";
	}
	
	/*
	 * AJAX
	 */
	@Secured("ROLE_AUTOMATIC_MEDIATOR")
	@RequestMapping(value="/ajax/schools", method=RequestMethod.GET)
	public @ResponseBody List<School> getSchools(Principal currentUser, 
			@RequestParam(required=true, defaultValue="") String letter, Locale locale) {
		try {
			Provider provider = this.getProvider(currentUser, locale);
			return this.serviceLocator.getSchoolService().getSchoolsByProvider(provider.getId(), letter, "name", locale);
		} catch (Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return new ArrayList<School>();
		}
	}
	
	/*
	 * AJAX
	 */
	@Secured("ROLE_AUTOMATIC_MEDIATOR")
	@RequestMapping(value="/ajax/tags", method=RequestMethod.GET)
	public @ResponseBody List<Category> getCoursesTags(Locale locale) {
		try {
			Map<String, Category> mapa = new HashMap<String, Category>();
			List<Category> categories = new ArrayList<Category>();
			categories.addAll(this.serviceLocator.getExtendedCourseService().getCoursesTags(locale));
			categories.addAll(this.serviceLocator.getCourseService().getCoursesTags(locale));
			Iterator<Category> it = categories.iterator();
			while(it.hasNext()) {
				Category category = it.next();
				mapa.put(category.getCategory(), category);
			}
			return new ArrayList<Category>(mapa.values());
		} catch (Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return new ArrayList<Category>();
		}
	}
	
	
	/*
	 * Private methods
	 */
	private Provider getProvider(Principal user, Locale locale) throws Exception {
		InternalUser intUser = this.serviceLocator.getUserService().getUser(user.getName());
		MediationService mediationService = this.serviceLocator.getMediatorService().
			getMediationServiceByUser(intUser.getId(), locale);
		return this.serviceLocator.getProviderService().
			getProviderByMediationService(mediationService.getId(), locale);
	}
}
