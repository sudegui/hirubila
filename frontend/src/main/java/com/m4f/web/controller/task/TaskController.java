package com.m4f.web.controller.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xml.sax.SAXException;
import com.m4f.business.domain.Course;
import com.m4f.business.domain.CourseCatalog;
import com.m4f.business.domain.CronTaskReport;
import com.m4f.business.domain.Inbox;
import com.m4f.business.domain.MediationService;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.ResultSearchEmail;
import com.m4f.business.domain.School;
import com.m4f.business.domain.extended.ExtendedCourse;
import com.m4f.business.domain.extended.ExtendedSchool;
import com.m4f.business.domain.extended.FeedCourses;
import com.m4f.business.domain.extended.FeedSchools;
import com.m4f.business.service.exception.ContextNotActiveException;
import com.m4f.business.service.exception.ServiceNotFoundException;
import com.m4f.business.service.extended.impl.InternalFeedServiceImpl;
import com.m4f.utils.PageManager;
import com.m4f.utils.StackTraceUtil;
import com.m4f.utils.feeds.events.model.Dump;
import com.m4f.web.controller.BaseController;

@Controller
@RequestMapping("/task")
public class TaskController extends BaseController  {
	
	private static final Logger LOGGER = Logger.getLogger(TaskController.class.getName());
	private static final String EMAIL_DOMAIN_SUFFIX = "@hirubila.appspotmail.com";
	
	/*
	 * This task generates internal feeds for a mediationService
	 */
	@RequestMapping(value="/internalFeeds/mediation", method=RequestMethod.POST)
	public String generateInternalFeedsByMediationId(@RequestParam(required=true) Long mediationId, 
			@RequestHeader("host") String host, HttpServletRequest request) throws Exception {
		// Create a new CronTaskReport
		CronTaskReport report = this.serviceLocator.getCronTaskReportService().create();
		report.setObject_id(mediationId);
		report.setDate(new Date());
		report.setType(CronTaskReport.TYPE.INTERNAL_FEED);
		try {
			// Start the process
			
			
			Provider provider = 
				this.serviceLocator.getProviderService().getProviderByMediationService(mediationId, null);
			
			MediationService mediationService = this.serviceLocator.getMediatorService().getMediationService(mediationId, null);
			//Set report description
			report.setDescription(new StringBuffer("Servicio de mediación: ").append(mediationService.getName()).toString());
			
			if(!mediationService.getHasFeed()) { // All must be manual mediator, but it's another check.
				FeedSchools feedSchools = this.serviceLocator.getInternalFeedService().createFeedSchools(host, provider, mediationService);
				this.serviceLocator.getInternalFeedService().saveFeedSchools(feedSchools);
				HashMap<Long, ExtendedSchool> schools = new HashMap<Long, ExtendedSchool>();
				Collection<ExtendedCourse> courses = 
					this.serviceLocator.getExtendedCourseService().getCoursesByOwner(mediationService.getId(), null, null);
				for(ExtendedCourse course : courses) {
					ExtendedSchool school = this.serviceLocator.getExtendedSchoolService().getSchool(course.getSchool(), Locale.getDefault());
					if(school != null) schools.put(school.getId(), school);
				}
				for(ExtendedSchool school : schools.values()) {
					FeedCourses feedCourse = this.serviceLocator.getInternalFeedService().
						createFeedCourses(host, provider, mediationService, school, this.getAvailableLanguages()); 	
					this.serviceLocator.getInternalFeedService().saveFeedCourses(feedCourse);
				}
				// Set result into report
				report.setResult("OK");
			}
			
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
			report.setResult(new StringBuffer("ERROR: ").append(e.getMessage()).toString());
			throw e;
		} finally {
			this.serviceLocator.getCronTaskReportService().save(report);
		}
		return "task.launched";
	}
	/**
	 * END INTERNAL FEED GENERATION
	 */
	
	
	/***************************************************************************************
	 * 
	 * 								SEO CATALOG OPERATIONS
	 * 
	 ***************************************************************************************/
	
	
	@RequestMapping(value="/school/catalog/generate", method=RequestMethod.POST)
	public String generateSchoolCatalog(@RequestParam(required=true) Long schoolId) {
		final int RANGE = 200;
		try {	
			for (Course course : this.serviceLocator.getCourseService().getCoursesBySchool(schoolId, null, null)) {
				for(Locale locale : this.getAvailableLanguages()) {
					
				}
			}
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		
		return "task.launched";
	}
	
	
	@RequestMapping(value="/provider/catalog/create", method=RequestMethod.POST)
	public String createCatalogByProvider(@RequestParam(required=true) Long providerId) 
		throws Exception {
		LOGGER.severe("#### Start - Generation catalog for provider " + providerId);
		for(Locale locale : this.getAvailableLanguages()) {
			Collection<Course> courses = 
				this.serviceLocator.getCourseService().getCoursesByProvider(providerId, null, locale);
			LOGGER.severe("#### Total courses " + courses.size());
			for (Iterator<Course> it = courses.iterator(); it.hasNext(); ) {
				Course course = it.next();
				this.catalogBuilder.buildSeoEntity(course, locale);
			}
		}
		LOGGER.severe("#### End - Generation catalog for provider " + providerId);
		return "task.launched";
	}

	
	@RequestMapping(value="/catalog/regenerate", method=RequestMethod.GET)
	public String generateCatalog(Locale locale) {
		final int RANGE = 200;
		try {
			LOGGER.severe("#### Regenerating catalog for all Courses.............");
			PageManager<Course> paginator = new PageManager<Course>();
			paginator.setOffset(RANGE);
			paginator.setStart(0);
			long total = this.serviceLocator.getCourseService().count();
			paginator.setSize(total);
			LOGGER.severe("#### Total Courses............. " + total);
			LOGGER.severe("#### End page.................." + paginator.getPagesMax());
			for(Integer page : paginator.getTotalPagesIterator()) {
				Map<String, String> params = new HashMap<String, String>();
				int start = (page-1)*RANGE;
				params.put("start", "" + start);
				int end = (page)*RANGE;
				params.put("finish", "" + end);
				this.serviceLocator.getWorkerFactory().createWorker().addWork(
						this.serviceLocator.getAppConfigurationService().getGlobalConfiguration().CATALOG_QUEUE, 
						"/task/catalog/createpaginated", params);
				LOGGER.severe("#### Batch: " + start + "-" + end);
			}
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "task.launched";
	}
	
	
	/**
	 * CATALOG CREATION TASKS
	 */
	@RequestMapping(value="/catalog/createpaginated", method=RequestMethod.POST)
	public String createCatalogPaginatedNew(@RequestHeader("host") String host, 
			@RequestParam(required=true) Integer start, 
			@RequestParam(required=true) Integer finish) throws Exception {
		try {
			Collection<Course> courses = 
				this.serviceLocator.getCourseService().getCourses("title", null, start, finish);
			LOGGER.severe("+++ Paginated total courses: " + courses.size());
			for(Course course : courses) {
				for(Locale locale : this.getAvailableLanguages()) {
					CourseCatalog catalogCourse = 
						this.serviceLocator.getCatalogService().getCourseCatalogByCourseId(course.getId(), locale);
					if(catalogCourse == null) {
						LOGGER.severe("+++ Creating catalog entry for course " + 
								course.getTitle() + " (" + course.getId() + ")");
						Map<String, String> params = new HashMap<String, String>();
						params.put("courseId", course.getId().toString());
						params.put("language", locale.getLanguage());
						this.serviceLocator.getWorkerFactory().createWorker().addWork(
								this.serviceLocator.getAppConfigurationService().getGlobalConfiguration().CATALOG_QUEUE, 
								"/task/catalog/create", params);
					}
				}
			}
		} catch(Exception e) {
			this.viewHelper.errorManagement(e);
			throw e;
		}
		return "task.launched";
	}
	
	
	
	@RequestMapping(value="/catalog/create", method=RequestMethod.POST)
	public String createCourseCatalog(@RequestHeader("host") String host, 
			@RequestParam(required=true) Long courseId, 
			@RequestParam(required=false) String language) throws Exception {
		try {		
			List<Locale> locales = new ArrayList<Locale>();
			if(language != null) {
				locales.add(new Locale(language));
			} else {
				locales.addAll(this.getAvailableLanguages());
			}
			this.catalogBuilder.buildSeoEntity(courseId, locales);	
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			throw e;
		}
		return "task.launched";
	}
	
	/**
	 * CATALOG DELETION TASKS
	 */
	@RequestMapping(value="/catalog/deletepaginated", method=RequestMethod.POST)
	public String deleteCatalogPaginatedNew(@RequestHeader("host") String host, 
			@RequestParam(required=true) Integer start, 
			@RequestParam(required=true) Integer finish) throws Exception{
		try {
			Collection<CourseCatalog> courses = 
				this.serviceLocator.getCatalogService().getCoursesCatalog(null, 
					null, start, finish);
			for(CourseCatalog course : courses) {
				Map<String, String> params = new HashMap<String, String>();
				params.put("courseId", course.getId().toString());
				this.serviceLocator.getWorkerFactory().createWorker().addWork(
						this.serviceLocator.getAppConfigurationService().getGlobalConfiguration().CATALOG_QUEUE, 
						"/task/catalog/delete", params);
			}			
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			throw e;
		}
		return "task.launched";
	}
	
	@RequestMapping(value="/catalog/delete", method=RequestMethod.POST)
	public String deleteCourseCatalog(@RequestHeader("host") String host, 
			@RequestParam(required=true) Long courseId) throws Exception {
		try {
			CourseCatalog course = this.serviceLocator.getCatalogService().getCourseCatalogById(courseId);
			this.serviceLocator.getCatalogService().delete(course);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			throw e;
		}
		return "task.launched";
	}
	
	
	@RequestMapping(value="/recovery", method=RequestMethod.POST)
	public @ResponseBody String recovery(@RequestParam(required=true) String email) {
		//TODO send an email to the user
		LOGGER.info("SENDING EMAIL WITH PASSWORD TO -> " + email);
		return "task.launched";
	}
	
	/**
	 * RESULT SEARCH EMAIL
	 */
	@RequestMapping(value="/sendSearchResult", method=RequestMethod.POST)
	public @ResponseBody String sendSearchResultEmail(
			@RequestParam(required=true) Long resultSearchEmailId) {
		try {
			ResultSearchEmail email = this.serviceLocator.getInboxService().getResultSearchEmail(resultSearchEmailId, Locale.getDefault());
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
			LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "task.launched";
	}
	
	/**
	 * INBOX RESPONSE EMAIL
	 */
	@RequestMapping(value="/sendInboxResponse", method=RequestMethod.POST)
	public @ResponseBody String sendInboxResponseEmail(
			@RequestParam(required=true) Long inboxId) {
		try {
			Locale locale = this.getAvailableLanguages().get(0);
			Inbox messageRes = this.serviceLocator.getInboxService().getInbox(inboxId, locale);
			Inbox messageReq = null;
			if(messageRes != null) {
				messageReq = this.serviceLocator.getInboxService().getInbox(messageRes.getRelatedId(), locale);
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
				msg.setText(new StringBuffer(messageRes.getContent()).append("\n\n\n").append(this.getMessage("suggestion.problem.response.advice")).toString());
		        Transport.send(msg);
			} catch (AddressException e) {
				LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
	        } catch (MessagingException e) {
	        	LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
	        }
	        
			LOGGER.info("SENDING SEARCH RESULT TO EMAIL -> " + messageReq.getFrom());
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "task.launched";
	}
	
	/*
	 * TEMPORARY TASK TO ADD A PROVIDER TO ALL MEDIATION SERVICE MANUAL
	 */
	@RequestMapping(value="/createprovidersmanual", method=RequestMethod.POST)
	public String createProvidersManual(@RequestHeader("host") String host, @RequestParam Long mediationId) {
		try {
			for(Locale locale : this.getAvailableLanguages()) {
				MediationService mediation = this.serviceLocator.getMediatorService().getMediationService(mediationId, locale);
				Provider provider = this.serviceLocator.getProviderService().getProviderByMediationService(mediation.getId(), locale);
				if(provider == null) {
					provider = new Provider();
					provider.setMediationService(mediation.getId());
					provider.setRegulated(Boolean.FALSE);
					provider.setName(mediation.getName());
					this.serviceLocator.getProviderService().save(provider, locale);
					provider.setFeed(new StringBuffer("http://").append(host).append("/").
							append(locale).append(InternalFeedServiceImpl.SCHOOL_DETAIL_URL).append(provider.getId()).toString());
					this.serviceLocator.getProviderService().save(provider, locale);
				} else if(provider != null && !provider.isTranslated()) {
					provider.setName(mediation.getName());
					this.serviceLocator.getProviderService().save(provider, locale);
				}
				
			}
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "task.launched";
	}
	
	
	
	
}