package com.m4f.web.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.m4f.business.domain.InternalUser;
import com.m4f.business.domain.MediationService;
import com.m4f.business.domain.Provider;
import com.m4f.utils.StackTraceUtil;
import com.m4f.web.bind.form.MediatorForm;
import com.m4f.business.service.ifc.I18nCourseService;
import java.util.Map;

@Controller
@RequestMapping("/")
public class HomeController extends BaseController {

	private static final Logger LOGGER = Logger.getLogger(HomeController.class.getName());
	
		
	public HomeController() {
		super();
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String getView(Principal currentUser, Model model, 
			Locale locale)
			throws IOException, ClassNotFoundException {		
		if(currentUser ==null) {
			return "home";
		}
		String customHome = "";
		try {
			InternalUser user = this.serviceLocator.getUserService().getUser(currentUser.getName());
			//The root user isn't into datastore.
			if(user==null) {
				customHome = "redirect:/" + locale.getLanguage() + "/dashboard/admin/";
			} else {
				if(user.isAdmin()) {
					customHome = "redirect:/" + locale.getLanguage() + "/dashboard/admin/";
				} else {
					customHome = "redirect:/" + locale.getLanguage() + "/dashboard/mediator/";
				}
			}	
		}catch(Exception e) {
			this.viewHelper.errorManagement(e);
		}
		return customHome;
	}
	
	@RequestMapping(value="/contact", method=RequestMethod.GET)
	public String getContact(Principal currentUser, Model model, 
			@RequestParam(required=false, defaultValue="en") String lang)
			throws IOException, ClassNotFoundException {
		return "contact";
	}
	
	@RequestMapping(value="/home", method=RequestMethod.GET)
	public String getHome(Principal currentUser, Model model, 
			Locale locale)
			throws IOException, ClassNotFoundException {
		return "redirect:/" + locale.getLanguage() + "/";
	}
	
	@RequestMapping(value="/postlogout", method=RequestMethod.GET)
	public String getPostLogout(Principal currentUser, Model model, 
			Locale locale)
			throws IOException, ClassNotFoundException {
		return "redirect:/" + locale.getLanguage() + "/";
	}
	
	@RequestMapping(value="/postlogin", method=RequestMethod.GET)
	public String getPostLogin(Principal currentUser, Model model, 
			Locale locale)
			throws IOException, ClassNotFoundException {
		return "redirect:/" + locale.getLanguage() + "/";
	}
	
	@RequestMapping(value="/ping", method=RequestMethod.GET)
	public String doPing() throws IOException, ClassNotFoundException {
		return "common.ping";
	}
	
}