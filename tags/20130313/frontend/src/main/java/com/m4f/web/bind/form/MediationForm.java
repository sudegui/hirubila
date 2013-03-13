package com.m4f.web.bind.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.m4f.business.domain.InternalUser;
import com.m4f.business.domain.MediationService;
import com.m4f.business.domain.Provider;

public class MediationForm {
	
	/*Fields with validation annotations*/
	private String feed;
	private String name;
	private String entity;
	private MediationService.UPDATE_METHOD updateMethod;
	
	private MediationService mediationService;
	private Provider provider;
	private List<InternalUser> members = new ArrayList<InternalUser>();
	
	private Float longitude;
	private Float latitude;
	
	
	public void setMediationService(MediationService mService) {
		this.mediationService = mService;
	}
	
	public MediationService getMediationService() {
		return this.mediationService;
	}
	
	public void setProvider(Provider p) {
		this.provider = p;
	}
	
	public Provider getProvider() {
		return this.provider;
	}
	
	public List<InternalUser> getMembers() {
		return this.members;
	}
	
	public void addMember(InternalUser user) {
		this.members.add(user);
	}
	
	public void addMembers(Collection<InternalUser> m) {
		this.members.addAll(m);
	}
	
	public MediationService.UPDATE_METHOD getUpdateMethod() {
		return updateMethod;
	}

	public void setUpdateMethod(MediationService.UPDATE_METHOD updateMethod) {
		this.updateMethod = updateMethod;
	}

	public void setFeed(String feed) {
		this.feed = feed;
	}

	public String getFeed() {
		return feed;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getEntity() {
		return entity;
	}

	public Float getLongitude() {
		return longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	public Float getLatitude() {
		return latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}
	
}