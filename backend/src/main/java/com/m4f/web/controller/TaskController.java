package com.m4f.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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
import javax.xml.parsers.ParserConfigurationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
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
import com.m4f.utils.PageManager;
import com.m4f.utils.StackTraceUtil;
import com.m4f.utils.feeds.events.model.Dump;
import org.springframework.http.HttpStatus;

@Controller
@RequestMapping("/task")
public class TaskController extends BaseController  {
	
	private static final Logger LOGGER = Logger.getLogger(TaskController.class.getName());
	private static final String EMAIL_DOMAIN_SUFFIX = "@hirubila.appspotmail.com";
	
	
	@RequestMapping(value="/updatecourses", method=RequestMethod.POST)
	public String updateCourses(@RequestParam(required=true) Long schoolId,
			@RequestParam(required=true) Long dumpId) throws Exception {
		LOGGER.log(Level.INFO, "Starting the update courses from school's queue...");
		School school = null;
		try {
			school = schoolService.getSchool(schoolId, null);
			if(school == null) {
				LOGGER.severe("School with id " + schoolId + " doesn't exist.");
				return "common.error";
			}
			Dump dump = null;
			dump = dumpService.getDump(dumpId);
			if(dump == null) {
				LOGGER.severe("Dump with id " + dumpId + " doesn's exist.");
				return "common.error";
			}
			Map<String, List<Course>> parsedCourses = coursesParser.getCourses(dump, school);
			this.storeCourses(dump, school, parsedCourses);
		} catch (ParserConfigurationException e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		} catch (SAXException e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		} catch (IOException e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		} catch (Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			throw e;
			//return "common.error";
		}
		return "task.launched";
	}
	
	
	
	/*
	 * This task creates/updates schools and courses information from one provider feed
	 */
	@RequestMapping(value="/loadproviderfeed", method=RequestMethod.POST)
	public String loadProviderFeed(@RequestParam Long providerId, 
			@RequestParam Long dumpId) throws Exception {
		LOGGER.log(Level.SEVERE, "----- Starting the update schools from provider");
		// Create a new CronTaskReport
		CronTaskReport report = cronTaskReportService.create();
		report.setObject_id(providerId);
		report.setDate(new Date());
		report.setType(CronTaskReport.TYPE.PROVIDER_FEED);
		Provider provider = null;
		Dump dump = null;
		try {
			provider = providerService.getProviderById(providerId, null);
			LOGGER.log(Level.SEVERE, "----- name: " + provider.getName());
			//Set report description
			report.setDescription(new StringBuffer("Proveedor de feeds: ").append(provider.getName()).toString());
			dump = dumpService.getDump(dumpId);
			if(provider == null) {
				LOGGER.severe("Provider with id " + providerId + " doesn's exist.");
				return "common.error";
			}			
			if(dump == null) {
				LOGGER.severe("Dump with id " + dumpId + " doesn's exist."); 
				return "common.error";
			}
			/**
			 * Proceso que realiza el parseo del feed del proveedor, el cual contiene
			 * un conjunto de centros (schools). Existe un aspecto creado para registrar
			 * posibles excepciones producidas dentro del proceso de parseo.
			 * Este registro de errores se encuentra localizado en:
			 * com.m4f.utils.feeds.aop.ParserHypervisor#registerProviderError
			 * VALIDACI�N DE ESTRUCTURA DEL XML.
			 */
			List<School> schools = schoolsParser.getSchools(provider);
			/**
			 * Proceso que realiza el volcado de los centros parseados al modelo de 
			 * persistencia. Existe un aspecto creado para registrar posibles problemas
			 * de validación en el contenido de los centros a almacenar.
			 * Este registro de problemas se encuentra localizado en:
			 * com.m4f.utils.feeds.aop.DumperHypervisor#registerSchoolValidationError
			 * 
			 * VALIDACI�N DEL CONTENIDO DEL XML. 
			 */
			for(Locale locale : this.getAvailableLanguages()) {
				this.storeSchools(dump, provider, schools, locale);
			}
			/**
			 * Creación de una tarea por cada centro parseado para pasarla a ejecución
			 * posteriormente.
			 * TODO access to datastore to get all schools and generate the next task.
			 */
			List<School> storedSchools = 
				schoolService.getSchoolsByProvider(provider.getId(), null, null);
			for(School school : storedSchools) {
				if((school.getFeed()!=null) && (!"".equals(school.getFeed()))) {
					Map<String, String> params = new HashMap<String, String>();
					params.put("schoolId", school.getId().toString());
					params.put("dumpId", "" + dump.getId());
					workerFactory.createWorker().addWork(
							configurationService.getGlobalConfiguration().SCHOOL_QUEUE, 
							"/task/updatecourses", params);
					
				}
			}
			
			// Set result into report
			report.setResult("OK");
		} catch (ParserConfigurationException e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			report.setResult(new StringBuffer("ERROR: ").append(e.getMessage()).toString());
			cronTaskReportService.save(report);
			return "common.error";
		} catch (SAXException e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			report.setResult(new StringBuffer("ERROR: ").append(e.getMessage()).toString());
			cronTaskReportService.save(report);
			return "common.error";
		} catch (IOException e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			report.setResult(new StringBuffer("ERROR: ").append(e.getMessage()).toString());
			cronTaskReportService.save(report);
			return "common.error";
		} catch (Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			report.setResult(new StringBuffer("ERROR: ").append(e.getMessage()).toString());
			cronTaskReportService.save(report);
			throw e;
		}
		LOGGER.info("--- Ending the update schools from provider's (" + 
				providerId + ") queue...");
		cronTaskReportService.save(report);
		return "task.launched";
	}
		
	/*
	 * This task generates internal feeds for a mediationService
	 */
	@RequestMapping(value="/internalFeeds/mediation", method=RequestMethod.POST)
	public String generateInternalFeedsByMediationId(@RequestParam(required=true) Long mediationId, 
			@RequestHeader("host") String host, HttpServletRequest request) throws Exception {
		// Create a new CronTaskReport
		CronTaskReport report = cronTaskReportService.create();
		report.setObject_id(mediationId);
		report.setDate(new Date());
		report.setType(CronTaskReport.TYPE.INTERNAL_FEED);
		try {
			// Start the process		
			Provider provider =  providerService.getProviderByMediationService(mediationId, null);		
			MediationService mediationService = mediatorService.getMediationService(mediationId, null);
			//Set report description
			report.setDescription(new StringBuffer("Servicio de mediación: ").append(mediationService.getName()).toString());
			
			if(!mediationService.getHasFeed()) { // All must be manual mediator, but it's another check.
				FeedSchools feedSchools = internalFeedService.createFeedSchools(host, provider, mediationService);
				internalFeedService.saveFeedSchools(feedSchools);
				HashMap<Long, ExtendedSchool> schools = new HashMap<Long, ExtendedSchool>();
				Collection<ExtendedCourse> courses = 
					extendedCourseService.getCoursesByOwner(mediationService.getId(), null, null);
				for(ExtendedCourse course : courses) {
					ExtendedSchool school = extendedSchoolService.getSchool(course.getSchool(), Locale.getDefault());
					if(school != null) schools.put(school.getId(), school);
				}
				for(ExtendedSchool school : schools.values()) {
					FeedCourses feedCourse = internalFeedService.createFeedCourses(host, 
							provider, mediationService, school, this.getAvailableLanguages()); 	
					internalFeedService.saveFeedCourses(feedCourse);
				}
				// Set result into report
				report.setResult("OK");
			}
			
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
			report.setResult(new StringBuffer("ERROR: ").append(e.getMessage()).toString());
			throw e;
		} finally {
			cronTaskReportService.save(report);
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
			ResultSearchEmail email = inboxService.getResultSearchEmail(resultSearchEmailId, Locale.getDefault());
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
		        inboxService.save(email, Locale.getDefault());
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
			Inbox messageRes = inboxService.getInbox(inboxId, locale);
			Inbox messageReq = null;
			if(messageRes != null) {
				messageReq = inboxService.getInbox(messageRes.getRelatedId(), locale);
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
	
	
	
	/**
	 * PRIVATE METHODS
	 */
	
	private void storeSchools(Dump dump, Provider provider, 
			List<School> schools, Locale locale) throws Exception {
		for(School school : schools) {
			dumperManager.dumpSchool(dump, school, locale, provider);
		}
	}
	
	private void storeCourses(Dump dump, School school, 
			Map<String, List<Course>> courses) throws Exception {
		for(String lang : courses.keySet()) {
			Locale locale = new Locale(lang);
			ArrayList<Course> lista = new ArrayList<Course>();
			lista.addAll(courses.get(lang));
			/*
			 * Se consigue ahora la informacion de territorio, debido a que esta asociada a la escuela, 
			 * y esta no va a variar para la colección de cursos.
			 * De esta forma ahorramos procesamiento en las diferentes consultas, ya que sino habria que 
			 * hacerlo en el método dumpCourse, sacando la información por cada curso.
			 * 
			 */
			
			// Get provider data
			Provider provider = providerService.getProviderById(school.getProvider(), locale);
			for(Course course : lista) {
				dumperManager.dumpCourse(dump, course, locale, school, provider);
			}
		}
	}
}