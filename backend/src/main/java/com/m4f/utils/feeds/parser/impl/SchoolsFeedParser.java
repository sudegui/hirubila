package com.m4f.utils.feeds.parser.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.m4f.business.domain.ContactInfo;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.School;
import com.m4f.utils.feeds.parser.ifc.ISchoolsParser;
import com.m4f.utils.content.ifc.ContentAcquirer;

public class SchoolsFeedParser implements ISchoolsParser {
	
	private ContentAcquirer contentAcquirer;
	private List<School> schools = new ArrayList<School>();
	

	public SchoolsFeedParser(ContentAcquirer cAcquirer) {
		super();
		this.contentAcquirer =  cAcquirer;
	}

	@Override
	public List<School> getSchools(Provider provider) throws ParserConfigurationException, 
		SAXException, IOException, Exception {
		byte[] content = this.contentAcquirer.getContent(new URI(provider.getFeed())).toByteArray();
		InputSource inputFeed = new InputSource(new ByteArrayInputStream(content));
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		sp.parse(inputFeed, new SchoolsFeedReader(provider));
		return this.schools;
	}
	
	private class SchoolsFeedReader extends DefaultHandler {
		private static final String ROOT = "zentroak";
		private static final String SCHOOL = "zentroa";
		private static final String SCHOOL_ID = "unique_id";
		private static final String SCHOOL_NAME = "izena";
		private static final String SCHOOL_PHONE = "telefonoa";
		private static final String SCHOOL_FAX = "faxa";
		private static final String SCHOOL_ZIPCODE = "pk";
		private static final String SCHOOL_EMAIL = "postae";
		private static final String SCHOOL_WEB = "web_orria";
		private static final String SCHOOL_ADDR = "helbidea";
		private static final String SCHOOL_CITY = "herria";
		private static final String SCHOOL_FEED = "feed";
		
		private School school;
		private ContactInfo info;
		private StringBuffer sb;
		private Provider provider;
		
		public SchoolsFeedReader(Provider provider) {
			super();
			this.provider = provider;
		}
		
		@Override
		public void startDocument() throws SAXException {
			this.sb = new StringBuffer();
		}
		
		@Override
		public void endDocument() throws SAXException {
			this.school = null;
			this.info = null;
			this.sb = null;
		}
		
		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if(SCHOOL.equals(qName)) {
				this.school = new School();
				this.info = new ContactInfo();
				this.school.setProvider(provider.getId());
			}
			this.sb.delete(0, this.sb.length());
		}
		
		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			if(SCHOOL.equals(qName)) {
				this.school.setContactInfo(this.info);
				schools.add(this.school);
			} else if(SCHOOL_NAME.equals(qName)) {
				this.school.setName(this.sb.toString().trim());  
			} else if(SCHOOL_PHONE.equals(qName)) {
				this.info.setTelephone(this.sb.toString().trim());
			} else if(SCHOOL_FAX.equals(qName)) {
				this.info.setFax(this.sb.toString().trim());
			} else if(SCHOOL_ZIPCODE.equals(qName)) {
				this.info.setZipCode(this.sb.toString().trim());
			} else if(SCHOOL_EMAIL.equals(qName)) {
				this.info.setEmail(this.sb.toString().trim());
			} else if(SCHOOL_WEB.equals(qName)) {
				this.info.setWebSite(this.sb.toString().trim());
			} else if(SCHOOL_ADDR.equals(qName)) {
				this.info.setStreetAddress(this.sb.toString().trim());
			} else if(SCHOOL_ID.equals(qName)) {
				//Important make this distinction because two providers have
				//the same id generator strategy it's posible match to externalId's
				this.school.setExternalId(this.provider.getId() + "-" + 
						this.sb.toString().trim());
			} else if(SCHOOL_CITY.equals(qName)) {
				this.info.setCity(this.sb.toString().trim().toLowerCase());
			} else if(SCHOOL_FEED.equals(qName)) {
				this.school.setFeed(this.parseFeed(this.sb.toString().trim()));
			}
		}
		
		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			this.sb.append(new String(ch, start, length));
		}
		
		private String parseFeed(String source) {
			String target = source;
			if((!source.startsWith("https://")) && (!source.startsWith("http://"))) {
				target = "http://" + source;
			}
			return target;
		}
	}

	
}