package com.m4f.web.controller.model;

import java.security.Principal;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
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

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;
import com.m4f.business.domain.Inbox;
import com.m4f.business.domain.Inbox.ORIGIN;
import com.m4f.business.domain.Inbox.USER;
import com.m4f.utils.StackTraceUtil;
import com.m4f.web.bind.form.InboxForm;
import com.m4f.web.controller.BaseController;

@Controller
@RequestMapping("/suggestion")
public class InboxController extends BaseModelController {
private static final Logger LOGGER = Logger.getLogger(InboxController.class.getName());
	
	public InboxController() {
		super();
	}
	
	@Secured({"ROLE_AUTOMATIC_MEDIATOR","ROLE_MANUAL_MEDIATOR"})
	@RequestMapping(method=RequestMethod.GET)
	public String getForm(Model model, Principal principal,
			@RequestHeader("referer") String referer, HttpSession session) {
		try {
			InboxForm inbox = new InboxForm();
			inbox.setFrom(principal.getName());
			model.addAttribute("inbox", inbox);
			session.setAttribute(this.REFERER_PARAM, referer);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "suggestion.inbox.form";
	}
	
	@Secured({"ROLE_AUTOMATIC_MEDIATOR","ROLE_MANUAL_MEDIATOR"})
	@RequestMapping(value="/edit/{inboxId}", method=RequestMethod.GET)
	public String edit(@PathVariable Long inboxId, Model model, Locale locale,
			@RequestHeader("referer") String referer, HttpSession session) {
		try {
			model.addAttribute("inbox", this.serviceLocator.getInboxService().getInbox(inboxId, locale));
			session.setAttribute(this.REFERER_PARAM, referer);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "suggestion.inbox.form";
	}
	
	@Secured({"ROLE_AUTOMATIC_MEDIATOR","ROLE_MANUAL_MEDIATOR"})
	@RequestMapping(method=RequestMethod.POST)
	public String save(@ModelAttribute("inbox") @Valid InboxForm form, 
			BindingResult result, Principal principal, Model model, 
			Locale locale, @RequestHeader("Host") String host, HttpSession session, HttpServletRequest request) {
		if(result.hasErrors()) {
			return "suggestion.inbox.form";
		}
		try {
			String remoteHost = request.getRemoteAddr() != null ? request.getRemoteAddr() : "";
			Inbox inbox = this.serviceLocator.getInboxService().createInbox();
			inbox.setType(form.getType());
			inbox.setRemoteHost(remoteHost);
			inbox.setUser(Inbox.USER.INTERNAL);
			inbox.setContent(form.getContent());
			inbox.setFrom(principal.getName());
			inbox.setName(principal.getName());
			inbox.setCreated(new Date());
			this.serviceLocator.getInboxService().save(inbox, locale);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		String returnURL = this.buildReturnURL(host, 
				(String) session.getAttribute(this.REFERER_PARAM), locale);
		return "redirect:" + returnURL;
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/delete/{inboxId}", method=RequestMethod.GET)
	public String delete(@PathVariable Long inboxId, Locale locale,
			@RequestHeader("referer") String referer, @RequestHeader("Host") String host) {
		try {
			Inbox inbox = this.serviceLocator.getInboxService().getInbox(inboxId, locale);
			this.serviceLocator.getInboxService().delete(inbox, locale);			
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		String returnURL = this.buildReturnURL(host, referer, locale);
		return "redirect:" + returnURL;
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/detail/{inboxId}", method=RequestMethod.GET)
	public String detail(@PathVariable Long inboxId, Model model, Locale locale) {
		try {
			Inbox inbox = this.serviceLocator.getInboxService().getInbox(inboxId, locale);
			inbox.setReaded(Boolean.TRUE);
			this.serviceLocator.getInboxService().save(inbox, locale);
			model.addAttribute("inbox", inbox);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "suggestion.inbox.detail";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/response/{inboxId}", method=RequestMethod.GET)
	public String responseToInboxMessage(Principal principal, @PathVariable Long inboxId, Model model, Locale locale) {
		try {
			Inbox inboxReq = this.serviceLocator.getInboxService().getInbox(inboxId, locale);
			InboxForm inbox = new InboxForm();
			inbox.setFrom(principal.getName());
			model.addAttribute("inboxReq", inboxReq);
			model.addAttribute("inbox", inbox);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		
		return "suggestion.inbox.response";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/response/{inboxId}", method=RequestMethod.POST)
	public String sendResponseToInboxMessage(@ModelAttribute("inbox") @Valid Inbox inboxRes, BindingResult result, 
			@PathVariable Long inboxId, Model model, Locale locale, @RequestHeader("Host") String host) {
		
		try {
			Inbox inboxReq = this.serviceLocator.getInboxService().getInbox(inboxId, locale);
			if(inboxReq != null) {
				// Some hand setting.
				inboxRes.setRelatedId(inboxReq.getId());
				inboxRes.setFrom("admin@hirubila.m4f.es");
				inboxRes.setUser(USER.INTERNAL);
				inboxRes.setOrigin(ORIGIN.RESPONSE);
				// Save
				this.serviceLocator.getInboxService().save(inboxRes, locale);
				// Create task to send an email response
				Queue queue = QueueFactory.getQueue(this.serviceLocator.getAppConfigurationService().getGlobalConfiguration().MAIL_QUEUE);
				queue.add(TaskOptions.Builder.withUrl("/task/sendInboxResponse")
						.param("inboxId", inboxRes.getId().toString()).method(Method.POST));
			}
			
			
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		
		String returnURL = this.buildReturnURL(host, "/" + locale.getLanguage() + "/dashboard/admin/suggestions", locale);
		return new StringBuffer("redirect:").append(returnURL).toString();
	}
	
	
}