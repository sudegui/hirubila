package com.m4f.business.domain;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.jdo.annotations.EmbeddedOnly;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import com.m4f.business.domain.annotation.Comparable;
import com.m4f.utils.i18n.annotations.Multilanguage;

@SuppressWarnings("serial")
@PersistenceCapable(detachable="true")
@EmbeddedOnly
public class ContactInfo implements Serializable {
	
	@Persistent
	@Comparable
	public String telephone;
	
	@Persistent
	@Comparable
	public String fax;
	
	@Persistent
	@Multilanguage
	public String country;
	
	@Persistent
	@Multilanguage
	@Comparable
	public String city;
	
	@Persistent
	@Comparable
	public String zipCode;
	
	@Persistent
	@Multilanguage
	@Comparable
	public String streetAddress;
	
	@Persistent
	@Comparable
	public String webSite;
	
	@Persistent
	@Comparable
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
		StringBuffer sb = new StringBuffer("[");
		for(Field field: this.getClass().getDeclaredFields()) {
			try {
				if(!field.isAnnotationPresent(Comparable.class)) {
					continue;
				}
				if(field.get(this) != null) {
					String value = field.getName() + ":" + field.get(this).toString();
					sb.append(value + ",");
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		sb.append("]");
		return sb.toString();
	}
}