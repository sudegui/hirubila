package com.m4f.web.controller;

import java.util.Locale;
import java.util.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/")
public class HomeController extends BaseController {

	private static final Logger LOGGER = Logger.getLogger(HomeController.class.getName());
		
	public HomeController() {
		super();
	}
	
	@RequestMapping(method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void getHome(Locale locale) throws Exception {
		for(long id : providerService.getAllProviderIds()) {
			LOGGER.severe("Id: " + id);
		}
	}
	
	
}