package com.m4f.business.domain;

import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.m4f.utils.i18n.annotations.Multilanguage;

@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class Topic extends BaseEntity {
	
	@PrimaryKey  
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;

	@Persistent
	public String externalId;
	
	@Persistent
	@Multilanguage
	public String name;
	
	@Persistent
	public Set<Long> courses;
	
	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String n) {
		this.name = n;
	}
	
	public String getExternalId() {
		return this.externalId;
	}
	
	public void setExternalId(String extId) {
		this.externalId = extId;
	}
	
	public Set<Long> getCourses() {
		return this.courses;
	}
	
	public void setCourses(Set<Long> cs) {
		this.courses = cs;
	}
	
}