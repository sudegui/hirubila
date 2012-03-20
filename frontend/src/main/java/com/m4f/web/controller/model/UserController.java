package com.m4f.web.controller.model;

import java.util.Locale;
import java.util.logging.Logger;

import javax.cache.CacheException;
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

import com.m4f.business.domain.InternalUser;
import com.m4f.business.domain.MediationService;
import com.m4f.utils.StackTraceUtil;
import com.m4f.web.bind.form.UserForm;

@Controller
@Secured({"ROLE_ADMIN"})
@RequestMapping("/user")
public class UserController extends BaseModelController {
	private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());
	
	public UserController() throws CacheException {
		super();
	}
	
/*	@RequestMapping(value="/convertAll", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void convertAll(Locale locale) {
		try {
			Collection<InternalUser> mediators = this.serviceLocator.getUserService().getAllUser();
			for(InternalUser mediator : mediators) {
				mediator.setDeleted(Boolean.FALSE);
				this.serviceLocator.getUserService().save(mediator);
			}
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			//return "common.error";
		}
		//return "mediation.form";
	}
*/	
	@RequestMapping(method=RequestMethod.GET)
	public String getForm(Model model, Locale locale,
			@RequestHeader(required=false, value="referer") String referer, 
			HttpSession session) {
		try {
			UserForm form = new UserForm();
			form.setUser(this.serviceLocator.getUserService().createUser());
			model.addAttribute("form", form);
			model.addAttribute("mediations", 
					this.serviceLocator.getMediatorService().getAllMediationService(locale));
			session.setAttribute(this.REFERER_PARAM, referer);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "user.form";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String save(@ModelAttribute("form") @Valid UserForm form, 
			BindingResult result, Model model, Locale locale,
			@RequestHeader("Host") String host, HttpSession session) {
		// Check if exist an user with this mail
		try {
			InternalUser oldUser = this.serviceLocator.getUserService().getUser(form.getUser().getEmail());
			oldUser.setDeleted(Boolean.FALSE);
			this.serviceLocator.getUserService().save(oldUser);
			if(result.hasErrors() || (oldUser != null && form.getUser().getId() == null)) {
				return "user.form";
			}
			if(form.getUser().getId() != null) { // Editing mode. Remove user from his mediationservice
				MediationService mediationService = 
					this.serviceLocator.getMediatorService().getMediationServiceByUser(form.getUser().getId(), locale);
				if(mediationService != null) {
					mediationService.removeUser(form.getUser());
					this.serviceLocator.getMediatorService().save(mediationService, locale);
				}
			}
			form.getUser().setDeleted(Boolean.FALSE);
			this.serviceLocator.getUserService().save(form.getUser());
			MediationService mediationService = 
				this.serviceLocator.getMediatorService().getMediationService(form.getMediationService(), locale);
			mediationService.addUser(form.getUser());
			this.serviceLocator.getMediatorService().save(mediationService, locale);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		String returnURL = this.buildReturnURL(host, 
				(String) session.getAttribute(this.REFERER_PARAM), locale);
		return "redirect:" + returnURL;
	}
	
	@RequestMapping(value="/edit/{userId}", method=RequestMethod.GET)
	public String edit(@PathVariable Long userId, Model model, Locale locale,
			@RequestHeader(required=false, value="referer") String referer, 
			HttpSession session) {
		try {
			InternalUser user = this.serviceLocator.getUserService().getUser(userId);
			UserForm form = new UserForm();
			form.setUser(user);
			model.addAttribute("form", form);
			model.addAttribute("mediations", 
					this.serviceLocator.getMediatorService().getAllMediationService(locale));
			session.setAttribute(this.REFERER_PARAM, referer);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "user.form";
	}
	
	@RequestMapping(value="/delete/{userId}", method=RequestMethod.GET)
	public String delete(@PathVariable Long userId, Locale locale, 
			@RequestHeader(required=false,value="referer") String referer, 
			@RequestHeader("Host") String host) {
		try {
			InternalUser user = this.serviceLocator.getUserService().getUser(userId);
			if(user != null) {
				this.serviceLocator.getUserService().delete(user);
			}
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		String returnURL = this.buildReturnURL(host, referer, locale);
		return "redirect:" + returnURL;
	}
	
	@RequestMapping(value="/detail/{userId}", method=RequestMethod.GET)
	public String detail(@PathVariable Long userId, Model model, Locale locale) {
		try {
			InternalUser user = this.serviceLocator.getUserService().getUser(userId);
			model.addAttribute("user", user);
			model.addAttribute("mediationService", 
					this.serviceLocator.getMediatorService().getMediationServiceByUser(userId, locale));
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "user.detail";
	}

}