package com.m4f.utils.feeds.events.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.util.Date;
import com.google.appengine.api.datastore.Text;

@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class ParserErrorEvent extends SystemEvent {
	
	@PrimaryKey  
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	@Persistent
	private String entityClass;
	
	@Persistent
	private Long entityId;
	
	@Persistent
	private Text cause;
	
	@Persistent
	private Date when;
	
	@Persistent
	private Long dumpId;
	
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

	public void setCause(Text cause) {
		this.cause = cause;
	}

	public Text getCause() {
		return cause;
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
	
}