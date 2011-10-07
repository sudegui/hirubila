package com.m4f.business.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.jdo.annotations.EmbeddedOnly;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.PhoneNumber;
import com.google.appengine.api.datastore.Link;
import com.m4f.utils.i18n.annotations.Multilanguage;

@SuppressWarnings("serial")
@PersistenceCapable(detachable="true")
@EmbeddedOnly
public class ContactInfo implements Serializable {
	
	@Persistent
	public String telephone;
	
	@Persistent
	public String fax;
	
	@Persistent
	@Multilanguage
	public String country;
	
	@Persistent
	@Multilanguage
	public String city;
	
	@Persistent
	public String zipCode;
	
	@Persistent
	@Multilanguage
	public String streetAddress;
	
	@Persistent
	public String webSite;
	
	@Persistent
	public String email;
		
	public String getTelephone() {
		return this.telephone;
	}
	
	public void setTelephone(String t) {
		this.telephone = t;
	}
	
	public String getFax() {
		return this.fax;
	}
	
	public void setFax(String f) {
		this.fax = f;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getCountry() {
		return country;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getCity() {
		return city;
	}
	
	public String getStreetAddress() {
		return this.streetAddress;
	}
	
	public void setStreetAddress(String s) {
		this.streetAddress = s;
	}
	
	public String getWebSite() {
		return this.webSite;
	}
	
	public void setWebSite(String w) {
		this.webSite = w;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public void setEmail(String e) {
		this.email = e;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("[").append(this.getClass().getName()).append(" ");
		if(this.telephone != null) sb.append("Phone: ").append(this.telephone).append(" ");
		if(this.fax != null) sb.append("Fax: ").append(this.fax).append(" ");
		if(this.country != null) sb.append("Country: ").append(this.country).append(" ");
		sb.append("]");
		return super.toString();
	}
}