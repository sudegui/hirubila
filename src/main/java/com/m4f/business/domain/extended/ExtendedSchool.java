package com.m4f.business.domain.extended;

import java.util.Date;
import javax.jdo.annotations.Embedded;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.validation.constraints.NotNull;
import com.google.appengine.api.datastore.GeoPt;
import com.m4f.business.domain.BaseEntity;
import com.m4f.business.domain.ContactInfo;
import com.m4f.utils.i18n.annotations.Multilanguage;
import com.m4f.utils.i18n.annotations.MultilanguageEmbedded;

@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class ExtendedSchool extends BaseEntity {
	
	@PrimaryKey  
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	@Persistent
	@Multilanguage
	@NotNull
	public String name;
	
	@Persistent
	private Long town;
	
	@Persistent(defaultFetchGroup="true")
	private GeoPt geoLocation;
	
	@Persistent(defaultFetchGroup="true")
	@Embedded
	@MultilanguageEmbedded
	public ContactInfo contactInfo;
	
	@Persistent
	private Date created;
	
	@Persistent
	private Date updated;
	
	public void setName(String n) {
		this.name = n;
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

	public void setTown(Long town) {
		this.town = town;
	}

	public Long getTown() {
		return town;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("[");
		sb.append("key: ").append(this.id != null ? this.id.toString() : "null").append(", ");
		sb.append("name: ").append(this.name);
		sb.append("]");
		return sb.toString();
	}
}