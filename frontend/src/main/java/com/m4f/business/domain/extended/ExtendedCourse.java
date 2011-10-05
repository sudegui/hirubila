package com.m4f.business.domain.extended;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import com.m4f.business.domain.BaseEntity;
import com.m4f.business.domain.ifc.Taggeable;
import com.m4f.utils.i18n.annotations.Multilanguage;
import com.m4f.utils.i18n.annotations.MultilanguageCollection;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.NotNull;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.datastore.Category; 

@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class ExtendedCourse extends BaseEntity implements Taggeable {
	
	@PrimaryKey  
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	@Persistent
	private String externalId;
	
	@Persistent
	@NotNull
	private Long school;
	
	@Persistent
	private Long mediationService;
    
    @Persistent
    @NotNull
    @Multilanguage
    public String title;
    
    @Persistent
    private Date start;
    
    @Persistent
    private Date end;
    
    @Persistent
    @Multilanguage
    public Text information;
    
    @Persistent
    @MultilanguageCollection
    public Set<Category> tags;
        
    @Persistent
    private Boolean active = Boolean.TRUE;
    
    @Persistent
	public Date created;
	
	@Persistent
	private Date updated;
	
	@Persistent
	@Multilanguage
	public String timeTable;
	
	@Persistent
	private Boolean free;
	
	@Persistent
	@Multilanguage
	public String billMode;
	
	@Persistent
	private Set<Category> languages;
	
    public Date getStart() {
    	return this.start;
    }
    
    public void setStart(Date d) {
    	this.start = d;
    }
    
    public Date getEnd() {
    	return this.end;
    }
    
    public void setEnd(Date e) {
    	this.end = e;
    }
    
    public Long getSchool() {
    	return this.school;
    }
    
    public void setSchool(Long sch) {
    	this.school = sch;
    }
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return this.id;
	}
	
	public String getExternalId() {
		return this.externalId;
	}
	
	public void setExternalId(String extId) {
		this.externalId = extId;
	}	
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Text getInformation() {
		return information;
	}

	public void setInformation(Text information) {
		this.information = information;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	
	public Date getCreated() {
		return this.created;
	}
	
	public void setCreated(Date d) {
		this.created = d;
	}
	
	
	public Date getUpdated() {
		return this.updated;
	}
	
	public void setUpdated(Date u) {
		this.updated = u;
	}

	public Set<Category> getTags() {
		return this.tags;
	}
	
	public void setTags(Set<Category> ts) {
		this.tags = ts;
	}
	
	public Long getMediationService() {
		return mediationService;
	}

	public void setMediationService(Long mediationService) {
		this.mediationService = mediationService;
	}

	public void setFree(Boolean free) {
		this.free = free;
	}

	public Boolean getFree() {
		return free;
	}

	public void setLanguages(Set<Category> languages) {
		this.languages = languages;
	}

	public Set<Category> getLanguages() {
		return languages;
	}
	
	public String getTimeTable() {
		return timeTable;
	}

	public void setTimeTable(String timeTable) {
		this.timeTable = timeTable;
	}

	public String getBillMode() {
		return billMode;
	}

	public void setBillMode(String billMode) {
		this.billMode = billMode;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("[");
		sb.append("id: ").append(this.getId()).append(", ");
		sb.append("title: ").append(this.title);
		sb.append("]");
		return sb.toString();
	}

	@Override
	public void addTag(String tag) {
		this.initTags();
		this.tags.add(new Category(tag));
	}

	@Override
	public void removeTag(String tag) {
		this.initTags();
		this.tags.remove(new Category(tag));
	}

	@Override
	public void addTags(Collection<String> tags) {
		this.initTags();
		Category cTag;
		for(String tag : tags) {
			cTag = new Category(tag);
			if(!this.tags.contains(cTag)){
				this.tags.add(cTag);
			}
		}
	}

	@Override
	public void removeTags(Collection<String> tags) {
		this.initTags();
		Category cTag;
		for(String tag : tags) {
			cTag = new Category(tag);
			this.tags.remove(cTag);
		}
		
	}
	
	private void initTags() {
		if(this.tags == null) {
			this.tags = new HashSet<Category>();
		}
	}

	
	
}