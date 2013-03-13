package com.m4f.utils.feeds.parser.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.google.appengine.api.datastore.Text;
import com.m4f.business.domain.Course;
import com.m4f.business.domain.School;
import com.m4f.business.domain.Topic;
import com.m4f.utils.content.ifc.ContentAcquirer;
import com.m4f.utils.feeds.events.model.Dump;
import com.m4f.utils.feeds.parser.ifc.ICoursesParser;

public class CoursesFeedParser implements ICoursesParser {
	
	private static final Logger LOGGER = Logger.getLogger(CoursesFeedParser.class.getName());
	private List<SimpleDateFormat> dateFormatters = new ArrayList<SimpleDateFormat>();
	@Autowired
	private ContentAcquirer contentAcquirer;
	
	public CoursesFeedParser(List<String> dateFormats) {
		for(String dateFormat : dateFormats) {
			this.dateFormatters.add(new SimpleDateFormat(dateFormat));
		}
	}
	
	@Override
	public Map<String, List<Course>> getCourses(School school, Dump dump) 
			throws ParserConfigurationException, SAXException, IOException, Exception {
		List<Course> courses_es = new ArrayList<Course>();
		List<Course> courses_eu = new ArrayList<Course>();
		Map<String, List<Course>> courses = new HashMap<String, List<Course>>();
		courses.put("es", courses_es);
		courses.put("eu", courses_eu);
		byte[] content = this.contentAcquirer.getContent(new URI(school.getFeed())).toByteArray();
		InputSource inputFeed = new InputSource(new ByteArrayInputStream(content));
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		sp.parse(inputFeed, new CoursesFeedReader(school, courses));
		return courses;
	}
	
	private class CoursesFeedReader extends DefaultHandler {
		private static final String ROOT = "kurtsoak";
		private static final String COURSE = "kurtsoa";
		private static final String COURSE_ID = "unique_id";
		private static final String COURSE_NAME_ES = "izenburua_es";
		private static final String COURSE_NAME_EU = "izenburua_eu";
		private static final String COURSE_URL_ES = "url_es";
		private static final String COURSE_URL_EU = "url_eu";
		private static final String COURSE_START = "hasi";
		private static final String COURSE_END = "bukatu";
		private static final String COURSE_INFO_ES = "info_es";
		private static final String COURSE_INFO_EU = "info_eu";
		private static final String COURSE_GAIAK = "gaiak";
		private static final String COURSE_GAIA = "gaia";
		private static final String COURSE_GAIA_ID = "gaia_id";
		private static final String COURSE_GAIA_NAME = "gaia_izena";
		private Course course_es, course_eu;
		private School school;
		private List<String> topics;
		private Topic topic;
		private StringBuffer sb;
		private Map<String, List<Course>> courses;
		
		public CoursesFeedReader(School school, 
				Map<String, List<Course>> courses) {
			this.courses = courses;
			this.school = school;
		}
		
		@Override
		public void startDocument() throws SAXException {
			this.sb = new StringBuffer();
		}
		
		@Override
		public void endDocument() throws SAXException {
			this.course_es = null;
			this.course_eu = null;
			this.topic = null;
			this.topic= null;
			this.sb = null;
		}
		
		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if(COURSE.equals(qName)) {
				this.course_es = new Course();
				this.course_es.setSchool(school.getId());
				this.course_eu = new Course();
				this.course_eu.setSchool(school.getId());
				this.course_es.setProvider(school.getProvider());
				this.course_eu.setProvider(school.getProvider());
				this.sb.toString();
			} else if(COURSE_GAIAK.equals(qName)) {
				this.topics = new ArrayList<String>();
			} else if(COURSE_GAIA.equals(qName)) {
				this.topic = new Topic();
			}
			this.sb.delete(0, this.sb.length());
		}
		
		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			if(COURSE.equals(qName)) {
				this.courses.get("es").add(this.course_es);
				this.courses.get("eu").add(this.course_eu);
			} else if(COURSE_ID.equals(qName)) {
				this.course_es.setExternalId(this.school.getId() + "-" + this.sb.toString());
				this.course_eu.setExternalId(this.school.getId() + "-" + this.sb.toString());
			} else if(COURSE_NAME_ES.equals(qName)) {
				this.course_es.setTitle(this.sb.toString()); 
			} else if(COURSE_NAME_EU.equals(qName)) {
				this.course_eu.setTitle(this.sb.toString());
			} else if(COURSE_URL_ES.equals(qName)) { 
				this.course_es.setUrl(this.sb.toString());
			} else if(COURSE_URL_EU.equals(qName)) {
				this.course_eu.setUrl(this.sb.toString());
			} else if(COURSE_START.equals(qName)) {
				//Se permite fecha de inicio vacía.
				if(!"".equals(this.sb.toString())) {
					Date start = this.parseDate(this.sb.toString());
					this.course_es.setStart(start);
					this.course_eu.setStart(start);
				}
			} else if(COURSE_END.equals(qName)) {
				//Se permite fecha de finalización vacia.
				if(!"".equals(this.sb.toString())) {
					Date end = this.parseDate(this.sb.toString());
					this.course_es.setEnd(end);
					this.course_eu.setEnd(end);
				}
			} else if(COURSE_INFO_ES.equals(qName)) {
				this.course_es.setInformation(new Text(this.sb.toString())); 
			} else if(COURSE_INFO_EU.equals(qName)) {
				this.course_eu.setInformation(new Text(this.sb.toString()));
			} else if(COURSE_GAIAK.equals(qName)) {
				this.course_es.addTags(this.topics);
				this.course_eu.addTags(this.topics);
			} else if(COURSE_GAIA_NAME.equals(qName)) {
				this.topics.add(this.sb.toString().toLowerCase().replace("\n", ""));
			}
		}
		
		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			this.sb.append(new String(ch, start, length));
		}
		
		
		private Date parseDate(String strDate) throws SAXException {
			Date date = null;
			SAXException exception = null;
			for(SimpleDateFormat dateFormatter : dateFormatters) {
				exception = null;
				try {
					date = dateFormatter.parse(strDate);
					break;
				} catch(Exception e) {
					exception = new SAXException(e);
				}
			}
			if(exception!=null) {
				throw exception;
			}
			return date;
		}
	}
}