package com.m4f.web.controller;


import java.security.Principal;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.m4f.business.domain.Provider;
import com.m4f.business.service.exception.ContextNotActiveException;
import com.m4f.business.service.exception.ServiceNotFoundException;

@Controller
@RequestMapping("/")
public class HomeController extends BaseController {

	private static final Logger LOGGER = Logger.getLogger(HomeController.class.getName());
		
	public HomeController() {
		super();
	}
	
	
	@RequestMapping(method=RequestMethod.GET)
	public String getHome(Principal currentUser, Model model, 
			Locale locale)
			throws ServiceNotFoundException, ContextNotActiveException, Exception {
		List<Provider> providers = this.serviceLocator.getProviderService().getAllProviders(locale);
		LOGGER.severe("Number of providers: " + providers.size());
		return "home";
	}
	
}