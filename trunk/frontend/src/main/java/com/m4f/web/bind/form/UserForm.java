package com.m4f.web.bind.form;

import javax.validation.constraints.NotNull;

import com.m4f.business.domain.InternalUser;

public class UserForm {
	
	/*Fields with validation.*/
	private String email;
	private String password;
	private String name;
	@NotNull
	private Long mediationService;
	private InternalUser user;
	
	public InternalUser getUser() {
		return this.user;
	}
	
	public void setUser(InternalUser u) {
		this.user = u;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setMediationService(Long mediationService) {
		this.mediationService = mediationService;
	}

	public Long getMediationService() {
		return mediationService;
	}
	
	
}