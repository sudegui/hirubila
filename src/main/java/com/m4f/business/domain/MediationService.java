package com.m4f.business.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.jdo.annotations.Embedded;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.validation.constraints.NotNull;
import com.google.appengine.api.datastore.GeoPt;
import com.m4f.utils.i18n.annotations.MultilanguageEmbedded;
import com.m4f.utils.i18n.annotations.Multilanguage;
import com.m4f.business.domain.ifc.UserSeteable;

@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class MediationService extends BaseEntity implements UserSeteable {
	
	public enum UPDATE_METHOD {
		OVERWRITE_ALL("Sobreescribir TODO"), APPEND_NEW("Añadir sólo nuevos");
		
		private final String displayName;
		
		private UPDATE_METHOD(String dName) {
			this.displayName = dName;
		}
		
		public String getDisplayName() {
			return this.displayName;
		}
	
	};
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	@Persistent
	@NotNull(message="Name blank")
	@Multilanguage
	public String name;
	
	@Persistent
	@Multilanguage
	public String entity;
	
	@Persistent(defaultFetchGroup="true")
	@Embedded
	@MultilanguageEmbedded
	public ContactInfo contactInfo;
		
	@Persistent
	@NotNull(message="Type blank")
	private UPDATE_METHOD updateMethod;
	
	@Persistent
	@NotNull(message="Yes or Not has feed")
	private Boolean hasFeed;
	
	@Persistent
	private Set<Long> members = new HashSet<Long>();
	
	@Persistent
	private GeoPt geoLocation = new GeoPt(0.0F, 0.0F);
	
	@Persistent
	private Long province;
	
	@Override
	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getEntity() {
		return this.entity;
	}
	
	public void setEntity(String e) {
		this.entity = e;
	}
	
	public ContactInfo getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(ContactInfo contactInfo) {
		this.contactInfo = contactInfo;
	}
	
	public Boolean getHasFeed() {
		return this.hasFeed;
	}
	
	public void setHasFeed(Boolean hFeed) {
		this.hasFeed = hFeed;
	}
	
	public UPDATE_METHOD getUpdateMethod() {
		return updateMethod;
	}

	public void setUpdateMethod(UPDATE_METHOD updateMethod) {
		this.updateMethod = updateMethod;
	}
	
	public void setMembers(Set<Long> users) {
		this.members = users;
	}
	
	public Set<Long> getMembers() {
		return this.members;
	}
	
	public void setGeoLocation(GeoPt location) {
		this.geoLocation = location;
	}

	public GeoPt getGeoLocation() {
		if(this.geoLocation == null) {
			this.geoLocation = new GeoPt(0.0F, 0.0F);
		}
		return this.geoLocation;
	}
	
	@Override
	public void addUser(InternalUser user) {
		HashSet<Long> users = new HashSet<Long>(this.members);
		users.add(user.getId());
		this.members = users;
	}

	@Override
	public void addUsers(Collection<InternalUser> users) {
		HashSet<Long> tmp = new HashSet<Long>(this.members);
		for(InternalUser user : users) {
			tmp.add(user.getId());
		}
		this.members = tmp;
	}

	@Override
	public void removeUser(InternalUser user) {
		HashSet<Long> users = new HashSet<Long>(this.members);
		users.remove(user.getId());
		this.members = users;
	}

	@Override
	public void removeUsers(Collection<InternalUser> users) {
		HashSet<Long> tmp = new HashSet<Long>(this.members);
		for(InternalUser user : users) {
			tmp.remove(user.getId());
		}
		this.members = tmp;
	}

	public Long getProvince() {
		return province;
	}

	public void setProvince(Long province) {
		this.province = province;
	}
	
}