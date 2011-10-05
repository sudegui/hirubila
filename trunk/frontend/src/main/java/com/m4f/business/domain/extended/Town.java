package com.m4f.business.domain.extended;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotNull;

import com.google.appengine.api.datastore.GeoPt;
import com.m4f.business.domain.BaseEntity;
import com.m4f.utils.i18n.annotations.Multilanguage;

@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class Town extends BaseEntity {
		
	@PrimaryKey  
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
		
	@Persistent
	@Multilanguage
	@NotNull
	public String name;
	
	@Persistent
	@Multilanguage
	public String upperName;
	
	@Persistent(defaultFetchGroup="true")
	public GeoPt geoLocation;
	
	@Persistent
	private Long province;
	
	@Persistent
	private Long region;
	
	@Override
	public Long getId() {
		return this.id;
	}

	public void setRegion(Long region) {
		this.region = region;
	}

	public Long getRegion() {
		return region;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		if(name != null) {
			this.upperName = name.toUpperCase();
		}
	}

	public GeoPt getGeoLocation() {
		return geoLocation;
	}

	public void setGeoLocation(GeoPt geoLocation) {
		this.geoLocation = geoLocation;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProvince() {
		return province;
	}

	public void setProvince(Long province) {
		this.province = province;
	}

	public String getUpperName() {
		return upperName;
	}

	public void setUpperName(String upperName) {
		this.upperName = upperName;
	}
	
	@PrePersist
	@PreUpdate
	public void prePersist() {
		if (name != null) {
			upperName = name.toUpperCase();
		} else {
			upperName = null;
		}
	} 
}
