package com.m4f.business.domain;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class InternalUser {
	
	@PrimaryKey  
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	@Persistent
	//message = "{email.blank}"
	@NotNull(message="Mail blank")
	@Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message="Bad mail")
	private String email;
	
	@Persistent
	@NotNull(message="Bad Password")
	@Size(min=6, max=25)
	//@Min(message = "{password.error}", value=6)
	private String password;
	
	@Persistent
	@NotNull(message="Name blank")
	private String name; 
	
	@Persistent
	@NotNull(message="Yes or Not is admin")
	private Boolean admin;
	
	@Persistent
	private Boolean deleted=Boolean.FALSE;
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getAdmin() {
		return admin;
	}
	
	public Boolean isAdmin() {
		return admin;
	}
	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public Boolean isDeleted() {
		return this.deleted;
	}
	
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
}