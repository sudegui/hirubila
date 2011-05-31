package com.m4f.web.controller.model;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import com.google.appengine.api.datastore.Category;
import com.m4f.business.domain.InternalUser;
import com.m4f.business.domain.MediationService;
import com.m4f.business.domain.extended.ExtendedCourse;
import com.m4f.business.domain.extended.ExtendedSchool;
import com.m4f.business.domain.extended.Province;
import com.m4f.business.domain.extended.Town;
import com.m4f.utils.PageManager;
import com.m4f.utils.StackTraceUtil;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/extended/course/")
public class ExtendedCourseController extends BaseModelController {
private static final Logger LOGGER = Logger.getLogger(ExtendedCourseController.class.getName());
	
	public ExtendedCourseController() {
		super();
	}
	
	@Secured("ROLE_MANUAL_MEDIATOR")
	@RequestMapping( method=RequestMethod.GET)
	public String getForm(Principal currentUser, Model model, Locale locale, 
			@RequestHeader(required=false,value="referer") String referer, 
			HttpSession session) {
		try {
			MediationService mediationService = this.getMediationService(currentUser);
			ExtendedCourse course = this.serviceLocator.getExtendedCourseService().createCourse();
			course.setMediationService(mediationService.getId());
			model.addAttribute("course", course);
			session.setAttribute(this.REFERER_PARAM, referer);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "extended.course.form";
	}
	
	@Secured("ROLE_MANUAL_MEDIATOR")
	@RequestMapping(value="/edit/{courseId}", method=RequestMethod.GET)
	public String edit(@PathVariable Long courseId, Model model, Locale locale, 
			@RequestHeader(required=false,value="referer") String referer, HttpSession session) {
		try {
			ExtendedCourse course = this.serviceLocator.getExtendedCourseService().getCourse(courseId, locale);
			model.addAttribute("course", course);
			model.addAttribute("school", this.serviceLocator.getExtendedSchoolService().getSchool(course.getSchool(), locale));
			session.setAttribute(this.REFERER_PARAM, referer);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "extended.course.form";
	}
	
	@Secured("ROLE_MANUAL_MEDIATOR")
	@RequestMapping(method=RequestMethod.POST)
	public String save(@ModelAttribute("course") @Valid ExtendedCourse course, 
			BindingResult result, Principal principal, Locale locale, Model model, 
			@RequestHeader("Host") String host, HttpSession session) {				
		try {
			if(result.hasErrors()) {
				model.addAttribute("schools", 
						this.serviceLocator.getExtendedSchoolService().getAllSchools(null, locale));
				return "extended.course.form";
			}
			if(principal != null) {
				InternalUser user = this.serviceLocator.getUserService().getUser(principal.getName());
				MediationService mediationService = 
					this.serviceLocator.getMediatorService().getMediationServiceByUser(user.getId(), locale);
				course.setMediationService(mediationService.getId());
				this.serviceLocator.getExtendedCourseService().save(course, locale);
			} else {
				return "common.error";
			}
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		String returnURL = this.buildReturnURL(host, 
				(String) session.getAttribute(this.REFERER_PARAM), locale);
		return "redirect:" + returnURL;
	}
	
	
	
	@Secured("ROLE_MANUAL_MEDIATOR")
	@RequestMapping(value="/delete/{courseId}", method=RequestMethod.GET)
	public String delete(@PathVariable Long courseId, Locale locale, 
			@RequestHeader("referer") String referer, @RequestHeader("Host") String host) {
		try {
			ExtendedCourse course = this.serviceLocator.getExtendedCourseService().getCourse(courseId, locale);
			this.serviceLocator.getExtendedCourseService().delete(course, locale);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		String returnURL = this.buildReturnURL(host, referer, locale);
		return "redirect:" + returnURL;
	}
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public String list(Model model, Locale locale,
			@RequestParam(defaultValue="1", required=false) Integer page, @RequestParam(defaultValue="", required=false) String order) {
		try {
			String ordering = order != null && !("").equals(order) ? order : "title";
			PageManager<ExtendedCourse> paginator = new PageManager<ExtendedCourse>();
			paginator.setOffset(this.getPageSize());
			paginator.setUrlBase("/" + locale.getLanguage()+ "/extended/course/list");
			paginator.setStart((page-1)*paginator.getOffset());
			paginator.setSize(this.serviceLocator.getExtendedCourseService().countCourses());
			paginator.setCollection(this.serviceLocator.getExtendedCourseService().getCourses(ordering, locale, 
					paginator.getStart(), paginator.getEnd()));
			model.addAttribute("paginator", paginator);
			model.addAttribute("order", ordering);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			model.addAttribute("paginator", new PageManager<ExtendedCourse>());
			//return "common.error";
		}
		
		return "extended.course.list";
	}
	
	@Secured({"ROLE_MANUAL_MEDIATOR","ROLE_ADMIN"})
	@RequestMapping(value="/detail/{courseId}", method=RequestMethod.GET)
	public String detail(@PathVariable Long courseId, Model model, Locale locale) {
		try {
			ExtendedCourse course = this.serviceLocator.getExtendedCourseService().getCourse(courseId, locale);
			ExtendedSchool school = this.serviceLocator.getExtendedSchoolService().getSchool(course.getSchool(), locale);
			model.addAttribute("course", course);
			model.addAttribute("school", school);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}	
		return "extended.course.detail";
	}
	
	
	
	
	/**********************************************************************
	 * 
	 * 							ADMIN ACCESS ONLY
	 * 
	 **********************************************************************/
	
	/**
	 * 
	 * @param locale
	 * @param referer
	 * @param session
	 * @return
	 */
	
	@Secured("ADMIN")
	@RequestMapping(value="/erasure", method=RequestMethod.GET)
	public String erasure(Locale locale, 
			@RequestHeader("referer") String referer, HttpSession session) {
		try {
			this.serviceLocator.getExtendedSchoolService().erasure();
		} catch (Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "redirect:/" + locale.getLanguage() + "/extended/school/list";
	}
	
	/*@ModelAttribute("course")p
	public ExtendedCourse getCourse() {
		return this.serviceLocator.getExtendedCourseService().createCourse();
	}*/
	
	/*
	 * AJAX METHODS
	 */
	@Secured({"ROLE_MANUAL_MEDIATOR","ADMIN"})
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
	
	@Secured("ROLE_MANUAL_MEDIATOR")
	@RequestMapping(value="/ajax/provinces", method=RequestMethod.GET)
	public @ResponseBody List<Province> getProvinces(Locale locale) {
		try {
			return this.serviceLocator.getTerritorialService().getAllProvinces(locale);
		} catch (Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return new ArrayList<Province>();
		}
	}
	
	@Secured("ROLE_MANUAL_MEDIATOR")
	@RequestMapping(value="/ajax/towns", method=RequestMethod.GET)
	public @ResponseBody List<Town> getTowns(@RequestParam(required=true, defaultValue="") 
			Long provinceId, Locale locale) {
		List<Town> towns = new ArrayList<Town>();
		try {
			towns.addAll(this.serviceLocator.getTerritorialService().getTownsByProvince(provinceId, locale));
		} catch (Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
		}
		return towns;
	}
	
	@Secured("ROLE_MANUAL_MEDIATOR")
	@RequestMapping(value="/ajax/schools", method=RequestMethod.GET)
	public @ResponseBody List<ExtendedSchool> getSchools(Principal currentUser, 
			@RequestParam(required=true, defaultValue="") Long townId, Locale locale) {
		try {
			return this.serviceLocator.getExtendedSchoolService().getSchoolsByTown(townId, "name", locale);
		} catch (Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return new ArrayList<ExtendedSchool>();
		}
	}
	
}