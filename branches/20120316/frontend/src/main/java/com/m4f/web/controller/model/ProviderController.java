package com.m4f.web.controller.model;

import java.net.MalformedURLException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;
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
import com.m4f.business.domain.InternalUser;
import com.m4f.business.domain.MediationService;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.School;
import com.m4f.utils.PageManager;
import com.m4f.utils.StackTraceUtil;
import com.m4f.utils.feeds.events.model.Dump;
import com.m4f.web.controller.BaseController;

@Controller
@Secured({"ROLE_ADMIN"})
@RequestMapping("/provider")
public class ProviderController extends BaseModelController {
	
	private static final Logger LOGGER = Logger.getLogger(ProviderController.class.getName());
		
	@RequestMapping(method=RequestMethod.GET)
	public String getForm(@RequestHeader(required=false,value="referer") String referer, 
			HttpSession session, Model model) {
		try {
			model.addAttribute("provider", 
					this.serviceLocator.getProviderService().createProvider());
			session.setAttribute(this.REFERER_PARAM, referer);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "provider.form";
	}
	
	@RequestMapping(value="/edit/{providerId}", method=RequestMethod.GET)
	public String getView(@PathVariable Long providerId, Model model,
			Locale locale, @RequestHeader(required=false,value="referer") String referer, 
			HttpSession session) {
		try {
			Provider provider = this.serviceLocator.getProviderService().getProviderById(providerId, locale);
			model.addAttribute("provider", provider);
			session.setAttribute(this.REFERER_PARAM, referer);
		} catch (Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "provider.form";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String save(@ModelAttribute("provider") @Valid Provider provider, 
			BindingResult result, Locale locale, Model model,
			@RequestHeader("Host") String host, HttpSession session) {
		if (result.hasErrors()) {
			return "provider.form";
		}
		try {
			this.serviceLocator.getProviderService().save(provider, locale);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		String returnURL = this.buildReturnURL(host, 
				(String) session.getAttribute(this.REFERER_PARAM), locale);
		return "redirect:" + returnURL;
	}
	
	@RequestMapping(value="/delete/{providerId}", method=RequestMethod.GET)
	public String delete(@PathVariable Long providerId, Model model, Locale locale, 
			@RequestHeader("referer") String referer, @RequestHeader("Host") String host) {
		try {
			Provider provider = this.serviceLocator.getProviderService().getProviderById(providerId, locale);
			if(provider!=null) {
				this.serviceLocator.getProviderService().deleteProvider(provider, locale);
			}
		} catch (Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "redirect:" + this.buildReturnURL(host, referer, locale);
	}
	
	@RequestMapping(value="/detail/{providerId}", method=RequestMethod.GET)
	public String getDetail(@PathVariable Long providerId, 
			Model model, Locale locale) {
		try {
			Provider provider = this.serviceLocator.getProviderService().getProviderById(providerId, locale);
			model.addAttribute("provider", provider);
		} catch (Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "provider.detail";
	}
	
	
	@RequestMapping(value="/courses/", method=RequestMethod.GET)
	public String getCourses(Principal currentUser,Model model, Locale locale, 
			@RequestParam(defaultValue="1", required=false) Integer page) {
		/*
		 * TODO extract only the currentUser's courses but is necessary modify business logic
		 * to annotate into course the provider's id.
		 */
		try {
			InternalUser user = this.serviceLocator.getUserService().getUser(currentUser.getName());
			// get mediator because, they are the unique users with a provider associated and internaluser
			MediationService mediator = this.serviceLocator.getMediatorService().getMediationServiceByUser(user.getId(), locale);
			PageManager<Course> paginator = new PageManager<Course>();
			paginator.setUrlBase("/" + locale.getLanguage()+ "/provider/courses/");
			paginator.setStart((page-1)*paginator.getOffset());
			paginator.setSize(this.serviceLocator.getCourseService().countCoursesByProvider(mediator.getId()));
			paginator.setCollection(this.serviceLocator.getCourseService().getCoursesByProvider(mediator.getId(), "title", locale, 
					paginator.getStart(), paginator.getEnd()));
			model.addAttribute("paginator", paginator);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "course.list";
	}
	
	@RequestMapping(value="/{providerId}/schools", method=RequestMethod.GET)
	public String getSchools(@PathVariable Long providerId, Model model, Locale locale, 
			@RequestParam(defaultValue="1", required=false) Integer page) {
		try {
			Provider provider = this.serviceLocator.getProviderService().getProviderById(providerId, locale);
			if(provider == null) {
				String message = "Provider with id " + providerId + " doesn't exist.";
				model.addAttribute("message", message);
				return "common.error";
			}
			model.addAttribute("provider", provider);
			PageManager<School> paginator = new PageManager<School>();
			paginator.setUrlBase("/" + locale.getLanguage() + "/provider/" + provider.getId() + "/schools");
			paginator.setStart((page-1)*paginator.getOffset());
			paginator.setSize(this.serviceLocator.getSchoolService().countSchoolsByProvider(provider.getId()));
			paginator.setCollection(this.serviceLocator.getSchoolService().getSchoolsByProvider(provider.getId(), "name", locale, 
					paginator.getStart(), paginator.getEnd()));
			model.addAttribute("paginator", paginator);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "school.list";
	}
	
	@RequestMapping(value="/{providerId}/courses", method=RequestMethod.GET)
	public String getCourses(@PathVariable Long providerId, Model model, Locale locale, 
			@RequestParam(defaultValue="1", required=false) Integer page) {
		try {
			Provider provider = this.serviceLocator.getProviderService().getProviderById(providerId, locale);
			if(provider == null) {
				String message = "Provider with id " + providerId + " doesn't exist.";
				model.addAttribute("message", message);
				return "common.error";
			}
			model.addAttribute("provider", provider);
			PageManager<Course> paginator = new PageManager<Course>();
			paginator.setUrlBase("/" + locale.getLanguage() + "/provider/" + provider.getId() + "/courses");
			paginator.setStart((page-1)*paginator.getOffset());
			paginator.setSize(this.serviceLocator.getCourseService().countCoursesByProvider(provider.getId()));
			paginator.setCollection(this.serviceLocator.getCourseService().getCoursesByProvider(providerId, 
					null, locale, paginator.getStart(), paginator.getEnd()));
			model.addAttribute("paginator", paginator);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "course.list";
	}
	
	@RequestMapping(value="/{providerId}/catalog/create", method=RequestMethod.GET)
	public @ResponseBody String createCatalog(@PathVariable Long providerId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("providerId", "" + providerId);
		try {
			this.serviceLocator.getWorkerFactory().createWorker().addWork(
					this.serviceLocator.getAppConfigurationService().getGlobalConfiguration().CATALOG_QUEUE, 
					"/catalog/provider/create", params);
		} catch(Exception e) {
			return "nook";
		}
		return "ok";
	}
	
	@RequestMapping(value="/{providerId}/dumps", method=RequestMethod.GET)
	public String getDumps(@PathVariable Long providerId, Model model, Locale locale, 
			@RequestParam(defaultValue="1", required=false) Integer page) {
		try {
			Provider provider = this.serviceLocator.getProviderService().getProviderById(providerId, locale);
			if(provider == null) {
				String message = "Provider with id " + 
					providerId + " doesn't exist.";
				model.addAttribute("message", message);
				return "common.error";
			}
			model.addAttribute("provider", provider);
			PageManager<Dump> paginator = new PageManager<Dump>();
			paginator.setUrlBase("/" + locale.getLanguage() + 
					"/provider/" + provider.getId() + "/dumps");
			paginator.setStart((page-1)*paginator.getOffset());
			paginator.setSize(this.serviceLocator.getDumpService().countDumpsByOwner(providerId));
			paginator.setCollection(this.serviceLocator.getDumpService().getDumpsByOwner(providerId, 
					paginator.getStart(), paginator.getEnd(), null));
			model.addAttribute("paginator", paginator);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "dump.main";
	}
	
	@RequestMapping(value="/{providerId}/summary", method=RequestMethod.GET)
	public String getDumps(@PathVariable Long providerId, Model model, Locale locale) {
		try {
			Provider provider = this.serviceLocator.getProviderService().getProviderById(providerId, locale);
			Dump dump = this.serviceLocator.getDumpService().getLastDumpByOwner(provider.getId());
			if(dump != null) {
				model.addAttribute("dumpId", dump.getId());
			}
			model.addAttribute("providerId", provider.getId());
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "summary.admin.today";
	}
	
	@RequestMapping(value="/ajax/{providerId}", method=RequestMethod.GET)
	@ResponseBody
	public Provider getProvider(@PathVariable Long providerId,
			Locale locale) {
		Provider provider = null;
		try {
			provider = this.serviceLocator.getProviderService().getProviderById(providerId, locale);
			return provider;
		} catch (Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
		}
		return provider;
	}
	
}