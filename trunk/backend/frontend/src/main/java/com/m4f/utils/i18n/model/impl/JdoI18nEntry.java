package com.m4f.utils.i18n.model.impl;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.datastore.Text;


@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class JdoI18nEntry extends I18nEntry {
	
	@PrimaryKey  
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	@Persistent
	private String contentId;
	
	@Persistent
	private String objectClass;
	
	@Persistent
	private String fieldKey;
	
	@Persistent
	private Text fieldValue;
	
	@Persistent
	private String lang;
	
	
	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}
	
	public void setObjectClass(String objectClass) {
		this.objectClass = objectClass;
	}

	public String getObjectClass() {
		return objectClass;
	}

	public void setFieldKey(String fieldKey) {
		this.fieldKey = fieldKey;
	}

	public String getFieldKey() {
		return fieldKey;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public String getContentId() {
		return this.contentId;
	}

	public void setFieldValue(Text fieldValue) {
		this.fieldValue = fieldValue;
	}

	public Text getFieldValue() {
		return fieldValue;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getLang() {
		return lang;
	}
		
}