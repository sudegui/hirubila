package com.m4f.web.controller.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
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
import org.xml.sax.SAXException;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;
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
import com.m4f.business.domain.extended.Province;
import com.m4f.business.domain.extended.Region;
import com.m4f.business.domain.extended.Town;
import com.m4f.business.service.extended.impl.InternalFeedServiceImpl;
import com.m4f.utils.StackTraceUtil;
import com.m4f.utils.feeds.events.model.Dump;
import com.m4f.web.controller.BaseController;

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
			school = this.serviceLocator.getSchoolService().getSchool(schoolId, null);
			if(school == null) {
				LOGGER.severe("School with id " + schoolId + " doesn't exist.");
				return "common.error";
			}
			Dump dump = null;
			dump = this.serviceLocator.getDumpService().getDump(dumpId);
			if(dump == null) {
				LOGGER.severe("Dump with id " + dumpId + " doesn's exist.");
				return "common.error";
			}
			Map<String, List<Course>> parsedCourses = this.serviceLocator.getCoursesParser().getCourses(dump, school);
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
	
	@RequestMapping(value="/recovery", method=RequestMethod.POST)
	public @ResponseBody String recovery(@RequestParam(required=true) String email) {
		//TODO send an email to the user
		LOGGER.info("SENDING EMAIL WITH PASSWORD TO -> " + email);
		return "task.launched";
	}
	
	
	
	
	/*
	 * This task creates/updates schools and courses information from one provider feed
	 */
	@RequestMapping(value="/loadproviderfeed", method=RequestMethod.POST)
	public String updateDataFromFeed(@RequestParam(required=true) Long providerId, 
			@RequestParam(required=true) Long dumpId) throws Exception {
		LOGGER.log(Level.INFO, "----- Starting the update schools from provider's queue...");
		// Create a new CronTaskReport
		CronTaskReport report = this.serviceLocator.getCronTaskReportService().create();
		report.setObject_id(providerId);
		report.setDate(new Date());
		report.setType(CronTaskReport.TYPE.PROVIDER_FEED);
		Provider provider = null;
		Dump dump = null;
		try {
			provider = this.serviceLocator.getProviderService().getProviderById(providerId, Locale.getDefault());
			//Set report description
			report.setDescription(new StringBuffer("Proveedor de feeds: ").append(provider.getName()).toString());
			dump = this.serviceLocator.getDumpService().getDump(dumpId);
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
			List<School> schools = this.serviceLocator.getSchoolsParser().getSchools(dump, provider);
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
				this.serviceLocator.getSchoolService().getSchoolsByProvider(provider.getId(), null, null);
			Queue queue = 
				QueueFactory.getQueue(this.serviceLocator.getAppConfigurationService().getGlobalConfiguration().SCHOOL_QUEUE);
			for(School school : storedSchools) {
				if((school.getFeed()!=null) && (!"".equals(school.getFeed()))) {
					TaskOptions options = TaskOptions.Builder.withUrl("/task/updatecourses");
					options.param("schoolId", school.getId().toString());
					options.param("dumpId", "" + dump.getId());
					options.method(Method.POST);
					queue.add(options);		
				}
			}
			
			// Set result into report
			report.setResult("OK");
		} catch (ParserConfigurationException e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			report.setResult(new StringBuffer("ERROR: ").append(e.getMessage()).toString());
			this.serviceLocator.getCronTaskReportService().save(report);
			return "common.error";
		} catch (SAXException e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			report.setResult(new StringBuffer("ERROR: ").append(e.getMessage()).toString());
			this.serviceLocator.getCronTaskReportService().save(report);
			return "common.error";
		} catch (IOException e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			report.setResult(new StringBuffer("ERROR: ").append(e.getMessage()).toString());
			this.serviceLocator.getCronTaskReportService().save(report);
			return "common.error";
		} catch (Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			report.setResult(new StringBuffer("ERROR: ").append(e.getMessage()).toString());
			this.serviceLocator.getCronTaskReportService().save(report);
			return "common.error";
		}
		LOGGER.info("--- Ending the update schools from provider's (" + 
				providerId + ") queue...");
		this.serviceLocator.getCronTaskReportService().save(report);
		return "task.launched";
	}
		
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
			for(Course course : courses) {
				Queue queue = QueueFactory.getQueue(this.serviceLocator.getAppConfigurationService().getGlobalConfiguration().CATALOG_QUEUE);
				String urlTask = new StringBuffer("/task/catalog/create").toString();
				TaskOptions options = TaskOptions.Builder.withUrl(urlTask);
				options.taskName(""+ Calendar.getInstance().getTimeInMillis());
				options.method(Method.POST);
				options.param("courseId", course.getId().toString());
				queue.add(options);
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
			
			for(Locale locale : locales) {
				LOGGER.info(new StringBuffer("Generacion de instancia de catalogo del curso id: ")
				.append(courseId).append(" y locale: ").append(locale).toString());
				Course course = this.serviceLocator.getCourseService().getCourse(courseId, locale);
				School school = this.serviceLocator.getSchoolService().getSchool(course.getSchool(), locale);
				Provider provider = this.serviceLocator.getProviderService().getProviderById(course.getProvider(), locale);
					
					
				// Territorial data
				String townName = school.getContactInfo() != null && 
					school.getContactInfo().getCity() != null ? 
					school.getContactInfo().getCity() : "";
				List<Town> towns = this.serviceLocator.getTerritorialService().
					findTownsByName(townName, locale);
				Town town = new Town();
				Province province = new Province();
				Region region = new Region();
				if(towns != null && towns.size() > 0) {
					town = towns.get(0);
					region = getRegionsMap().get(locale.getLanguage()).get(town.getRegion());
					province = getProvincesMap().get(locale.getLanguage()).get(town.getProvince());
				}
					
					
										
				CourseCatalog catalog = new CourseCatalog(course, locale.getLanguage(), 
					school, provider.getName(), province.getName(), region.getName(), town.getName());
				CourseCatalog catalogOld = 
					this.serviceLocator.getCatalogService().getCourseCatalogByCourseId(courseId, locale);
				if(catalogOld != null) {
					catalog.setId(catalogOld.getId());
				}
				LOGGER.info(new StringBuffer("Fin generacion instancia de catalago del curso: ")
					.append(courseId).append(" y locale: ").append(locale).toString());
				this.serviceLocator.getCatalogService().save(catalog);
				
			}
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
				Queue queue = QueueFactory.getQueue(this.serviceLocator.getAppConfigurationService().getGlobalConfiguration().CATALOG_QUEUE);
				String urlTask = new StringBuffer("/task/catalog/delete").toString();
				TaskOptions options = TaskOptions.Builder.withUrl(urlTask);
				options.method(Method.POST);
				options.param("courseId", course.getId().toString());
				queue.add(options);
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
	
	/**
	 * PRIVATE METHODS
	 */
	
	private void storeSchools(Dump dump, Provider provider, 
			List<School> schools, Locale locale) throws Exception {
		for(School school : schools) {
			this.serviceLocator.getDumperManager().dumpSchool(dump, school, locale, provider);
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
			Provider provider = this.serviceLocator.getProviderService().getProviderById(school.getProvider(), locale);
			for(Course course : lista) {
				this.serviceLocator.getDumperManager().dumpCourse(dump, course, locale, school, provider);
			}
		}
	}
}