package com.m4f.business.domain;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.validation.constraints.NotNull;

import com.google.appengine.api.datastore.Blob;

/**
 * 
 * @author david.basoko@m4f.es
 * 
 * Esta clase representa el código HTML generado para acceder desde la 
 * parte pública de la aplicación al detalle de un curso. 
 * 
 * Para ello contiene un atributo identificador propio y otro para 
 * identificar el curso al que se asocia, un campo de fecha indicando 
 * cuándo se generó el código HTML, otro campo que almacena el código 
 * lingüístico del contentido y el campo BLOB que contiene la 
 * página HTML a generar.
 * 
 *
 */

@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class CourseHtml extends BaseEntity {
	
	@PrimaryKey  
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	@Persistent
	@NotNull
	private Long courseId;
	
	@Persistent
	@NotNull
	private String lang;
	
	@Persistent
	@NotNull
	private String title;
	
	@Persistent
	@NotNull
    private Date date = new Date();
	 
	@Persistent
	@NotNull
	private Blob content;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Blob getContent() {
		return content;
	}

	public void setContent(Blob content) {
		this.content = content;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String t) {
		this.title = t;
	}
	
	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}
}