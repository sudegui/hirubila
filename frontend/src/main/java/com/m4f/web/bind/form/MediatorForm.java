package com.m4f.web.bind.form;

import java.util.Set;

import com.m4f.business.domain.InternalUser;
import com.m4f.business.domain.MediationService;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.extended.Town;

public class MediatorForm {
	private Long id;
	private MediationService.UPDATE_METHOD updateMethod;
	private InternalUser user;
	private Provider provider;
	private String name;
	private Set<Town> towns;
	
	public MediatorForm() {
		super();
	}
	
	public MediatorForm(InternalUser user) {
		this();
		this.user = user;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public InternalUser getUser() {
		return user;
	}
	public void setUser(InternalUser user) {
		this.user = user;
	}
	public Provider getProvider() {
		return provider;
	}
	public void setProvider(Provider provider) {
		this.provider = provider;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<Town> getTowns() {
		return towns;
	}
	public void setTowns(Set<Town> towns) {
		this.towns = towns;
	}

	public MediationService.UPDATE_METHOD getUpdateMethod() {
		return updateMethod;
	}

	public void setUpdateMethod(MediationService.UPDATE_METHOD updateMethod) {
		this.updateMethod = updateMethod;
	}
}
