package com.m4f.business.domain;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;

import com.m4f.business.domain.annotation.Imported;
import com.m4f.business.domain.ifc.Taggeable;
import com.m4f.utils.i18n.annotations.Multilanguage;
import com.m4f.utils.i18n.annotations.MultilanguageCollection;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.NotNull;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.datastore.Category;

/**
 * 
 * @author eduardo.perrino@m4f.es
 * 
 * Esta clase representa un curso de formación, el cual es agregado al sistema
 * de la importación de un feed perteneciente a un proveedor de cursos registrado.
 * 
 * Por la tanto es importante tener en cuenta que todo curso proviene o  pertenece
 * a un proveedor y que todo curso, es impartido en un centro de formación.
 * Para modelar estas relaciones se inserta dentro de esta clase el id único del
 * proveedor, el cual se encuentra dentro del atributo provider, y el id único
 * del centro se encuentra dentro del atributo school.
 * 
 * También es importante indicar que estas entidades provienen de un repositorio
 * de cursos externos, y para guardar cierta relación con dicho repositorio se 
 * almacena el id único que tiene dentro de dicho repositorio bajo el atributo
 * externalId.
 *
 */

@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class Course extends BaseEntity implements Taggeable {
	
	@PrimaryKey  
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	@Persistent
	@Imported
	private String externalId;
	
	@Persistent
	@NotNull
	private Long school;
	
	@Persistent
	@NotNull
	private Long provider;
    
    @Persistent
    @NotNull
    @Multilanguage
    @Imported
    public String title;
    
    @Persistent
    @Multilanguage
    @Imported
    public String url;
    
    @Persistent
    @Imported
    private Date start;
    
    @Persistent
    @Imported
    private Date end;
    
    @Persistent
    @Multilanguage
    @Imported
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
	private Boolean regulated;
	
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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
	
	public void setProvider(Long provider) {
		this.provider = provider;
	}

	public Long getProvider() {
		return provider;
	}

	public Set<Category> getTags() {
		this.initTags();
		return this.tags;
	}
	
	public void setTags(Set<Category> ts) {
		this.tags = ts;
	}
	
	public Boolean getRegulated() {
		return regulated;
	}

	public void setRegulated(Boolean regulated) {
		this.regulated = regulated;
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
	
	@Override
	public boolean equals(Object obj) {
		boolean equal = true;
		
		if(obj != null && obj instanceof Course) {
			Course c = (Course) obj;
			// First if obj has id. This attribute will mark if they are equal
			if(c.getId() != null) equal &= this.id.equals(c.getId()); 
			if(this.externalId != null/* && c.getExternalId() != null*/) equal &= this.externalId.equals(c.getExternalId()); 
			if(this.school != null /*&& c.getSchool() != null*/) equal &= this.school.equals(c.getSchool());
			if(this.provider != null/* && c.getProvider() != null*/) equal &= this.provider.equals(c.getProvider());
			if(this.title != null /*&& c.getTitle() != null*/) equal &= this.title.equals(c.getTitle());
			if(this.url != null /*&& c.getUrl() != null*/) equal &= this.url.equals(c.getUrl());
			if(this.start != null) equal &= this.start.equals(c.getStart());
			if(this.end != null) equal &= this.end.equals(c.getEnd());
			if(this.information != null) equal &= this.information.equals(c.getInformation());
			if(tags != null) equal &= this.tags.equals(c.getTags());
			
		} else {
			equal = false;
		}
		
		return equal;
	}
}