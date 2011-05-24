package com.m4f.business.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.Embedded;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.validation.constraints.NotNull;

import com.google.appengine.api.datastore.GeoPt;
import com.m4f.utils.i18n.annotations.Multilanguage;
import com.m4f.utils.i18n.annotations.MultilanguageEmbedded;

@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class School extends BaseEntity {
	
	@PrimaryKey  
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;

	@Persistent
	private String externalId;
	
	@Persistent
	@Multilanguage
	@NotNull
	public String name;
		
	@Persistent(defaultFetchGroup="true")
	private GeoPt geoLocation;
	
	@Persistent(defaultFetchGroup="true")
	@Embedded
	@MultilanguageEmbedded
	public ContactInfo contactInfo;
	
	@Persistent
	//@Pattern(regexp="(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", message="Bad feed url")
	public String feed;
		
	@Persistent
	//@NotNull TODO enable this annotation
	private Long provider;
	
	@Persistent
	public Date created;
	
	@Persistent
	public Date updated;
	
	@Persistent
	private Set<String> search;
	
	public void setName(String n) {
		this.name = n;
		if(name != null && !("").equals(name)) {
			this.search = new HashSet<String>();
			String[] words = name.replace("\"", "").trim().split(" ");
			for(String word : words) this.search.add(word.toLowerCase());
		}
	}

	public String getName() {
		return this.name;
	}
	
	public void setGeoLocation(GeoPt location) {
		this.geoLocation = location;
	}

	public GeoPt getGeoLocation() {
		return this.geoLocation;
	}
	
	public ContactInfo getContactInfo() {
		return this.contactInfo;
	}
	
	public void setContactInfo(ContactInfo cInfo) {
		this.contactInfo = cInfo;
	}
	
	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getExternalId() {
		return this.externalId;
	}
	
	public void setExternalId(String extId) {
		this.externalId = extId;
	}
	
	public String getFeed() {
		return feed;
	}

	public void setFeed(String feed) {
		this.feed = feed;
	}
	
	public Date getCreated() {
		return this.created;
	}
	
	public void setCreated(Date d) {
		this.created = d;
	}
	
	
	public Date getUpdated() {
		return this.updated;
	}
	
	public void setUpdated(Date u) {
		this.updated = u;
	}

	public Set<String> getSearch() {
		return search;
	}

	public void setSearch(Set<String> search) {
		this.search = search;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("[");
		sb.append("key: ").append(this.id != null ? this.id.toString() : "null").append(", ");
		sb.append("name: ").append(this.name);
		sb.append("]");
		return sb.toString();
	}

	public void setProvider(Long provider) {
		this.provider = provider;
	}

	public Long getProvider() {
		return provider;
	}

}