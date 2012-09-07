package com.m4f.web.controller;

import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.m4f.business.domain.Course;
import com.m4f.business.domain.Inbox;
import com.m4f.business.domain.ResultSearchEmail;
import com.m4f.utils.StackTraceUtil;
import org.springframework.http.HttpStatus;

@Controller
@RequestMapping("/task")
public class TaskController extends BaseController  {
	
	private static final Logger LOGGER = Logger.getLogger(TaskController.class.getName());
	private static final String EMAIL_DOMAIN_SUFFIX = "@hirubila.appspotmail.com";
	
	/*@RequestMapping(value="/course/noUpdatedGet", method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void noUpdateCoursesGet(@RequestParam(required=true) String eId, HttpServletResponse response) throws Exception {
		StringBuffer sb = new StringBuffer();
		Collection<Course> courses = this.serviceLocator.getCourseService().getCoursesNoUPDATED(0, 2000);
		for(Course course : courses) {
			if(course.getUpdated() == null) {
				course.setUpdated(new Date());
				this.serviceLocator.getCourseService().save(course, null);
				sb.append(course.getId()).append(";");
			}
		}
		response.getWriter().write(sb.toString());
	}
	*/
	@RequestMapping(value="/recovery", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void recovery(@RequestParam(required=true) String email) {
		//TODO send an email to the user
		LOGGER.info("SENDING EMAIL WITH PASSWORD TO -> " + email);
	}
	
	/**
	 * RESULT SEARCH EMAIL
	 */
	@RequestMapping(value="/sendSearchResult", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void sendSearchResultEmail(
			@RequestParam(required=true) Long resultSearchEmailId) {
		try {
			ResultSearchEmail email = 
					this.serviceLocator.getInboxService().getResultSearchEmail(
							resultSearchEmailId, Locale.getDefault()); 
			Properties props = new Properties();
			Session session = Session.getDefaultInstance(props, null);		
			try {
				Message msg = new MimeMessage(session);
				msg.setFrom(new InternetAddress(new StringBuffer("info").append(EMAIL_DOMAIN_SUFFIX).toString(), 
						this.getMessage("search.result.send.from")));
				msg.addRecipient(Message.RecipientType.TO,
                         new InternetAddress(email.getTo(), email.getToName()));
		        msg.setSubject(this.getMessage("search.result.send.subject", email.getFromName()));
		        msg.setText(this.getMessage("search.result.send.body", email.getResultLink()).toString());
		        Transport.send(msg);
		        email.setSent(Boolean.TRUE);
		        this.serviceLocator.getInboxService().save(email, Locale.getDefault());
			} catch (AddressException e) {
				LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
	        } catch (MessagingException e) {
	        	LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
	        }
	        
			LOGGER.info("SENDING SEARCH RESULT TO EMAIL -> " + email.getTo());
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
		}
	}
	
	/**
	 * INBOX RESPONSE EMAIL
	 */
	@RequestMapping(value="/sendInboxResponse", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void sendInboxResponseEmail(
			@RequestParam(required=true) Long inboxId) {
		try {
			Locale locale = this.getAvailableLanguages().get(0);
			Inbox messageRes = 
					this.serviceLocator.getInboxService().getInbox(inboxId, locale);
			Inbox messageReq = null;
			if(messageRes != null) {
				messageReq = 
						this.serviceLocator.getInboxService().getInbox(messageRes.getRelatedId(), locale);
			}
			Properties props = new Properties();
			Session session = Session.getDefaultInstance(props, null);		
			try {
				Message msg = new MimeMessage(session);
				msg.setFrom(new InternetAddress(new StringBuffer("admin").append(EMAIL_DOMAIN_SUFFIX).toString(), 
						this.getMessage("suggestion.problem.response.from")));
				msg.addRecipient(Message.RecipientType.TO,
                         new InternetAddress(messageReq.getFrom(), messageReq.getName()));
				msg.setSubject(this.getMessage("suggestion.problem.response.subject", messageReq.getId()));
				msg.setText(new StringBuffer(messageRes.getContent()).append("\n\n\n").append(
						this.getMessage("suggestion.problem.response.advice")).toString());
		        Transport.send(msg);
			} catch (AddressException e) {
				LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
	        } catch (MessagingException e) {
	        	LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
	        }
	        
			LOGGER.info("SENDING SEARCH RESULT TO EMAIL -> " + messageReq.getFrom());
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
		}
	}
	
	
}