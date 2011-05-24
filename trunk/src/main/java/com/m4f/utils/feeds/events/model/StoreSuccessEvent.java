package com.m4f.utils.feeds.events.model;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Text;

@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class StoreSuccessEvent extends SystemEvent {
	
	@PrimaryKey  
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	@Persistent
	private String entityClass;
	
	@Persistent
	private Long entityId;
	
	@Persistent
	private Date when;
	
	@Persistent
	private Long dumpId;
	
	@Persistent
	private String language;
	
	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setEntityClass(String entityClass) {
		this.entityClass = entityClass;
	}

	public String getEntityClass() {
		return entityClass;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public Long getEntityId() {
		return entityId;
	}

	
	public void setWhen(Date when) {
		this.when = when;
	}

	public Date getWhen() {
		return when;
	}

	public void setDumpId(Long dumpId) {
		this.dumpId = dumpId;
	}

	public Long getDumpId() {
		return dumpId;
	}

	public void setLanguage(String lang) {
		this.language = lang;
	}

	public String getLanguage() {
		return this.language;
	}
	
}