package com.m4f.business.domain;


import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.m4f.utils.i18n.annotations.Multilanguage;

@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class Provider extends BaseEntity {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	@Persistent
	private Long mediationService;
	
	@Persistent
	@NotNull(message="Name blank")
	@Multilanguage
	public String name;
	
	@Persistent
	@NotNull(message="Feed blank")
	@Pattern(regexp="(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", message="Bad feed url")
	private String feed;
	
	@Persistent
	@NotNull(message="Is regulated?")
	private Boolean regulated=Boolean.FALSE;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFeed() {
		return this.feed;
	}

	public void setFeed(String feed) {
		this.feed = feed;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("[");
		sb.append("key: ").append(this.id != null ? this.id.toString() : "null").append(", ");
		sb.append("name: ").append(this.name);
		sb.append("]");
		return sb.toString();
	}

	@Override
	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public void setMediationService(Long mediatorService) {
		this.mediationService = mediatorService;
	}

	public Long getMediationService() {
		return this.mediationService;
	}

	public Boolean getRegulated() {
		return regulated;
	}

	public void setRegulated(Boolean regulated) {
		this.regulated = regulated;
	}
}