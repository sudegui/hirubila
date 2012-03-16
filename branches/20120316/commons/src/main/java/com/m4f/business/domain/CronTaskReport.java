package com.m4f.business.domain;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class CronTaskReport extends BaseEntity {
	
	public enum TYPE {
		PROVIDER_FEED("Importacion del feed de centros de un proveedor"), 
		PROVIDER_SCHOOLS("Importacion de los cursos de los centros de un proveedor"),
		INTERNAL_FEED("Generacion interna feed mediador manual");
		
		private final String displayName;
		
		private TYPE(String dName) {
			this.displayName = dName;
		}
		
		public String getDisplayName() {
			return this.displayName;
		}
		
		public String getValue() {
			return this.toString();
		}
	
	};
	
	public CronTaskReport() {
		super();
	}
	
	public CronTaskReport(Date date, Long object_id, TYPE type, String result, String description) {
		this.date = date;
		this.object_id = object_id;
		this.type = type;
		this.result = result;
		this.description = description;
	}
	
	@PrimaryKey  
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	@Persistent
    private Date date;
	
	@Persistent
    private Long object_id;
	
	@Persistent
	private TYPE type;
	
	@Persistent
	private String result;

	@Persistent
	private String description;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getObject_id() {
		return object_id;
	}

	public void setObject_id(Long objectId) {
		object_id = objectId;
	}

	public TYPE getType() {
		return type;
	}

	public void setType(TYPE type) {
		this.type = type;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
		if(result.length()>500) {
			this.result = result.substring(0, 490);
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		if(description.length()>500) {
			this.description = description.substring(0, 490);
		}
	}
}
