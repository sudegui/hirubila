package com.m4f.business.service.extended.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.logging.Logger;

import org.jdom.Attribute;
import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.XMLOutputter;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Category;
import com.m4f.business.domain.MediationService;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.extended.ExtendedCourse;
import com.m4f.business.domain.extended.ExtendedSchool;
import com.m4f.business.domain.extended.FeedCourses;
import com.m4f.business.domain.extended.FeedSchools;
import com.m4f.business.service.extended.ifc.I18nExtendedCourseService;
import com.m4f.business.service.extended.ifc.I18nExtendedSchoolService;
import com.m4f.business.service.extended.ifc.I18nInternalFeedService;
import com.m4f.business.service.ifc.I18nMediationService;
import com.m4f.business.service.ifc.I18nProviderService;
import com.m4f.business.service.impl.DAOBaseService;
import com.m4f.utils.i18n.dao.ifc.DAOSupport;

public class InternalFeedServiceImpl extends DAOBaseService implements I18nInternalFeedService {
	
private static final Logger LOGGER = Logger.getLogger(InternalFeedServiceImpl.class.getName());
	// School's constants
	private static final String SCHOOL_ROOT = "zentroak";
	private static final String SCHOOL_ELEMENT = "zentroa";
	private static final String SCHOOL_ATTR_ID = "unique_id";
	private static final String SCHOOL_ATTR_NAME = "izena";	
	private static final String SCHOOL_ATTR_PHONE = "telefonoa";
	private static final String SCHOOL_ATTR_FAX = "faxa";
	private static final String SCHOOL_ATTR_MAIL = "postae";
	private static final String SCHOOL_ATTR_ADDR = "helbidea";
	private static final String SCHOOL_ATTR_PK = "pk";
	private static final String SCHOOL_ATTR_WEB = "web_orria";
	private static final String SCHOOL_ATTR_CITY = "herria";
	private static final String SCHOOL_ATTR_FEED = "feed";
	private static final String[] SCHOOL_ATTRS = {SCHOOL_ATTR_ID, SCHOOL_ATTR_NAME, SCHOOL_ATTR_PHONE, SCHOOL_ATTR_FAX,
		SCHOOL_ATTR_MAIL, SCHOOL_ATTR_ADDR, SCHOOL_ATTR_PK, SCHOOL_ATTR_WEB, SCHOOL_ATTR_CITY, SCHOOL_ATTR_FEED};
	
	public static final String SCHOOL_DETAIL_URL = "/extended/public/school/feed/";
	public static final String COURSES_FEED = "/extended/public/course/feed/";
	
	// Course's constants
	private static final String COURSE_ROOT = "kurtsoak";
	private static final String COURSE_ELEMENT = "kurtsoa";
	private static final String COURSE_ATTR_ID = "unique_id";
	private static final String COURSE_ATTR_NAME = "izenburua_";
	private static final String COURSE_ATTR_URL = "url_";
	private static final String COURSE_ATTR_START = "hasi";
	private static final String COURSE_ATTR_END = "bukatu";
	private static final String COURSE_ATTR_INFO = "info_";
	private static final String COURSE_ATTR_GAIAK = "gaiak";
	private static final String COURSE_ATTR_GAIA = "gaia";
	private static final String GAIA_ATTR_ID = "gaia_id";
	private static final String GAIA_ATTR_NAME = "gaia_izena";
	
	private static final String[] COURSE_ATTRS = {COURSE_ATTR_ID, COURSE_ATTR_NAME, COURSE_ATTR_URL, COURSE_ATTR_START,
		COURSE_ATTR_END, COURSE_ATTR_INFO, COURSE_ATTR_GAIAK};
	private static final String[] GAIA_ATTRS = {GAIA_ATTR_ID, GAIA_ATTR_NAME};
	
	private static final String COURSE_DETAIL_URL = "/extended/public/course/";
	
	I18nExtendedSchoolService schoolService;
	I18nExtendedCourseService courseService;
	I18nMediationService mediationService;
	I18nProviderService providerService;
	
	public InternalFeedServiceImpl(DAOSupport dao,
			I18nExtendedSchoolService schoolService, I18nExtendedCourseService courseService) {
		super(dao);
		this.schoolService = schoolService;
		this.courseService = courseService;
	}

	@Override
	public FeedSchools createFeedSchools(String contextPath, Provider provider, MediationService mediationService) throws Exception {
		FeedSchools feed = (FeedSchools) this.DAO.createInstance(FeedSchools.class);
		
		// TODO Get configuration object a load different languages
		ArrayList<Locale> locales = new ArrayList<Locale>();
		// TODO hard-coded languages
		locales.add(new Locale("es"));
		locales.add(new Locale("eu"));
		
		// Starting xml generation
		Element root = new Element(SCHOOL_ROOT);
		root.setAttribute(new Attribute("noNamespaceSchemaLocation","http://hirubila.appspot.com/schema/zentroak-1.0.xsd",
				Namespace.getNamespace("xsi","http://www.w3.org/1999/XMLSchema-instance")));
		
		// NEW PART Get all schools. To do it, first we've to get all courses by mediationService and then get for each course its school.
		HashMap<Long, ExtendedSchool> schools = new HashMap<Long, ExtendedSchool>();
		Locale locale = locales.get(0);
		//Provider provider = this.providerService.getProviderById(providerId, locale); 
		//MediationService mediation = this.mediationService.getMediationService(provider.getMediationService(), locale);
		Collection<ExtendedCourse> courses = this.courseService.getCoursesByOwner(mediationService.getId(), null, locale);
		for(ExtendedCourse course : courses) {
			ExtendedSchool school = this.schoolService.getSchool(course.getSchool(), locale);
			if(school != null) schools.put(school.getId(), school);
		}
		//OLD List<ExtendedSchool> schools = this.schoolService.getAllSchools(null, locales.get(0));
		
		
		for(ExtendedSchool school : schools.values()) {
			Element schoolNode = new Element(SCHOOL_ELEMENT);
			for(String attr : SCHOOL_ATTRS) {
				Element attrNode = new Element(attr);
				if(SCHOOL_ATTR_ID.equals(attr)) {
					//attrNode.setContent(school.getId() != null ? this.createCData(school.getId().toString()) : "");
					attrNode.setContent(mediationService.getId() != null && school.getId() != null ? new CDATA(new StringBuffer(String.valueOf(mediationService.getId())).append("-").append(String.valueOf(school.getId())).toString()) : new CDATA(""));
				} else if(SCHOOL_ATTR_NAME.equals(attr)) {
					attrNode.setContent(school.getName() != null ? new CDATA(school.getName()) : new CDATA(""));
				} else if(SCHOOL_ATTR_PHONE.equals(attr)) {
					attrNode.addContent(school.getContactInfo() != null && school.getContactInfo().getTelephone() != null ?
							new CDATA(school.getContactInfo().getTelephone()) : new CDATA(""));
				} else if(SCHOOL_ATTR_FAX.equals(attr)) {
					attrNode.addContent(school.getContactInfo() != null &&school.getContactInfo().getFax() != null ?
							new CDATA(school.getContactInfo().getFax()) : new CDATA(""));
				} else if(SCHOOL_ATTR_MAIL.equals(attr)) {
					attrNode.addContent(school.getContactInfo() != null && school.getContactInfo().getEmail() != null ?
							new CDATA(school.getContactInfo().getEmail()) : new CDATA(""));
				} else if(SCHOOL_ATTR_ADDR.equals(attr)) {
					attrNode.addContent(school.getContactInfo() != null && school.getContactInfo().getStreetAddress() != null ?
							new CDATA(school.getContactInfo().getStreetAddress()) : new CDATA(""));
				} else if(SCHOOL_ATTR_PK.equals(attr)) {
					attrNode.addContent(school.getContactInfo() != null && school.getContactInfo().getZipCode() != null ?
							new CDATA(school.getContactInfo().getZipCode()) : new CDATA(""));
				} else if(SCHOOL_ATTR_WEB.equals(attr)) {
					attrNode.addContent(school.getContactInfo() != null && school.getContactInfo().getWebSite() != null ?
							new CDATA(school.getContactInfo().getWebSite()) : new CDATA(""));
				} else if(SCHOOL_ATTR_CITY.equals(attr)) {
					attrNode.addContent(school.getContactInfo() != null && school.getContactInfo().getCity() != null ?
							new CDATA(school.getContactInfo().getCity()) : new CDATA(""));
				} else if(SCHOOL_ATTR_FEED.equals(attr)) {					
					StringBuffer sb = new StringBuffer(contextPath).append("/").append(locale).append(COURSES_FEED).append(provider.getId()).append("/").append(school.getId());
					attrNode.addContent(new CDATA(sb.toString()));
				} 
				schoolNode.addContent(attrNode);
			}
			
			root.addContent(schoolNode);
		}
		Document document = new Document(root);
	    XMLOutputter outputter = new XMLOutputter();		
	    String content = outputter.outputString(document);
	    feed.setContent(new Blob(content.getBytes()));
	    feed.setDate(new Date());
	    feed.setProviderId(provider.getId());
	    
		return feed;
	}

	@Override
	public FeedCourses createFeedCourses(String contextPath, Provider provider, 
			MediationService mediationService, ExtendedSchool school, Collection<Locale> locales) throws Exception {
				
		FeedCourses feed = (FeedCourses) this.DAO.createInstance(FeedCourses.class);
		
		// Starting xml generation
		Element root = new Element(COURSE_ROOT);
		root.setAttribute(new Attribute("noNamespaceSchemaLocation","http://hirubila.appspot.com/schema/kurtsoak-1.0.xsd",
				Namespace.getNamespace("xsi","http://www.w3.org/1999/XMLSchema-instance")));

		Collection<ExtendedCourse> courses = this.courseService.getCoursesBySchoolByMediation(school.getId(), 
				mediationService.getId(), "title", null);

		
		// DATE format
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		
		for(ExtendedCourse course : courses) {
			// Make a map with languages and course with this language
			HashMap<String, ExtendedCourse> i18nCourse = new HashMap<String, ExtendedCourse>();

			
			for(Locale locale : locales) {
				i18nCourse.put(locale.getLanguage(), this.courseService.getCourse(course.getId(), locale));
			}
			
			// Make course node element with i18n properties
			Element courseNode = new Element(COURSE_ELEMENT);
			for(String attr : COURSE_ATTRS) {
				if(COURSE_ATTR_ID.equals(attr)) {
					courseNode.setContent(new Element(attr).addContent(mediationService.getId() != null && course.getId() != null ? new CDATA(new StringBuffer(String.valueOf(mediationService.getId())).append("-").append(String.valueOf(course.getId())).toString()) : new CDATA("")));
				} else if(COURSE_ATTR_NAME.equals(attr)) { // I18nProperty
					Iterator<String> it = i18nCourse.keySet().iterator();
					while(it.hasNext()) {
						String language = it.next();
						String i18nAttr = new StringBuffer(attr).append(language).toString();
						ExtendedCourse c = i18nCourse.get(language);
						courseNode.addContent(new Element(i18nAttr).setContent(c.getTitle() != null ? new CDATA(c.getTitle()) : new CDATA("")));
					}
				} else if(COURSE_ATTR_URL.equals(attr)) { // I18nProperty // TODO may be better a function...
					Iterator<String> it = i18nCourse.keySet().iterator();
					while(it.hasNext()) {
						String language = it.next();
						String i18nAttr = new StringBuffer(attr).append(language).toString();
						ExtendedCourse c = i18nCourse.get(language);
						String url = new StringBuffer(contextPath).append("/").append(language).append(COURSE_DETAIL_URL).append(course.getId()).toString();
						courseNode.addContent(new Element(i18nAttr).setContent(new CDATA(url)));
					}
				} else if(COURSE_ATTR_START.equals(attr)) {
					courseNode.addContent(new Element(attr).setContent(course.getStart() != null ? new CDATA(dateFormat.format(course.getStart())) : new CDATA("")));
				} else if(COURSE_ATTR_END.equals(attr)) {
					courseNode.addContent(new Element(attr).setContent(course.getEnd() != null ? new CDATA(dateFormat.format(course.getEnd())) : new CDATA("")));
				} else if(COURSE_ATTR_INFO.equals(attr)) { // I18nProperty // TODO may be better a function...
					Iterator<String> it = i18nCourse.keySet().iterator();
					while(it.hasNext()) {
						String language = it.next();
						String i18nAttr = new StringBuffer(attr).append(language).toString();
						ExtendedCourse c = i18nCourse.get(language);
						courseNode.addContent(new Element(i18nAttr).setContent(c.getInformation() != null ? new CDATA(c.getInformation().getValue()) : new CDATA("")));
					}
				} else if(COURSE_ATTR_GAIAK.equals(attr)) {
					Element tagsNode = new Element(attr);
					for(Category category : course.getTags()) {
						Element tagNode = new Element(COURSE_ATTR_GAIA);
						for(String gaiaAttr : GAIA_ATTRS) {
							Element tagAttrNode = new Element(gaiaAttr);
							if(GAIA_ATTR_ID.equals(gaiaAttr)) {
								tagAttrNode.setText("");
							} else if(GAIA_ATTR_NAME.equals(gaiaAttr)) {
								tagAttrNode.setContent(new CDATA(category.getCategory()));
							}
							tagNode.addContent(tagAttrNode);
						}
						tagsNode.addContent(tagNode);
					}
					courseNode.addContent(tagsNode);
				}
			}
			root.addContent(courseNode);
		}
		
		Document document = new Document(root);
	    XMLOutputter outputter = new XMLOutputter();		
	    String content = outputter.outputString(document);
	    feed.setContent(new Blob(content.getBytes("UTF-8")));
	    feed.setSchool(school.getId());
	    feed.setDate(new Date());
	    feed.setProviderId(provider.getId());
	    
		return feed;
	}

	@Override
	public FeedCourses getLastFeedCourses(Long providerId, Long extendedSchoolId)
			throws Exception {
		Collection<FeedCourses> feed = this.DAO.findEntitiesByRange(FeedCourses.class, "providerId == providerIdParam && school == idParam", "java.lang.Long providerIdParam, java.lang.Long idParam", new Object[]{providerId, extendedSchoolId},  "date desc", 0, 1);
		//Collection<FeedCourses> feed = this.DAO.findEntitiesByRange(FeedCourses.class, "date desc", 0, 1);
		if(feed != null && !feed.isEmpty()) return (FeedCourses) feed.toArray()[0];
		return new FeedCourses();
	}

	@Override
	public FeedSchools getLastFeedSchools(Long providerId) throws Exception {
		Collection<FeedSchools> feed = this.DAO.findEntitiesByRange(FeedSchools.class, "providerId == providerIdParam", "java.lang.Long providerIdParam", new Object[]{providerId}, "date desc", 0, 1);
		//Collection<FeedSchools> feed = this.DAO.findEntitiesByRange(FeedSchools.class, "date desc", 0, 1);
		if(feed != null && !feed.isEmpty()) return (FeedSchools) feed.toArray()[0];
		return new FeedSchools();
	}

	@Override
	public void saveFeedCourses(FeedCourses feed) throws Exception {
		this.DAO.saveOrUpdate(feed);
	}

	@Override
	public void saveFeedSchools(FeedSchools feed) throws Exception {
		this.DAO.saveOrUpdate(feed);		
	}
}
