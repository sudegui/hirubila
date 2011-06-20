package com.m4f.web.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.logging.Logger;

import javax.cache.CacheException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.appengine.api.taskqueue.TaskOptions.Method;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.m4f.business.domain.InternalUser;
import com.m4f.business.service.ifc.UserService;
import com.m4f.utils.StackTraceUtil;
import com.m4f.web.bind.form.RecoveryForm;

@Controller
@RequestMapping(value="/login")
public class LoginController extends BaseController {

	private static final Logger LOGGER = Logger.getLogger(LoginController.class.getName());
	
	public LoginController() throws CacheException {
		super();
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String getView(Principal currentUser, Model model,
			@RequestParam(required=false, defaultValue="") String login_error)
			throws IOException, ClassNotFoundException {
		return "login.login";
	}
	
	@RequestMapping(value="/recovery", method=RequestMethod.GET)
	public ModelAndView getRecovery() {
		return new ModelAndView("login.recovery", "recovery", new RecoveryForm());
	}
	
	@RequestMapping(value="/recovery", method=RequestMethod.POST)
	public String doRecovery(@ModelAttribute("recovery") @Valid RecoveryForm recovery, BindingResult result) {
		
		if(result.hasErrors()) {
			return "login.recovery";
		}
		
		try {
			InternalUser u = this.serviceLocator.getUserService().getUser(recovery.getEmail());
			if(u != null) {
				Queue queue = QueueFactory.getQueue(this.serviceLocator.getAppConfigurationService().getGlobalConfiguration().MAIL_QUEUE);
				queue.add(TaskOptions.Builder.withUrl("/task/recovery")
						.param("email", recovery.getEmail()).method(Method.POST));
			} else {
				LOGGER.finest("Trying to recover password from an unknown mail -> " + recovery.getEmail());
			}
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		
		return "login.login";
	}
	
}