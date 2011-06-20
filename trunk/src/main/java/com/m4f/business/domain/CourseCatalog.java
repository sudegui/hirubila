package com.m4f.business.domain;

import javax.jdo.annotations.Embedded;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import com.m4f.business.domain.ifc.Taggeable;
import com.m4f.utils.i18n.annotations.Multilanguage;
import com.m4f.utils.i18n.annotations.MultilanguageCollection;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.NotNull;

import com.google.appengine.api.datastore.GeoPt;
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
public class CourseCatalog extends BaseEntity {
	
	
	@PrimaryKey  
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	@Persistent
	private Long courseId;
	
	@Persistent
	private String externalCourseId;
	
	@Persistent
	private String providerName;
	
	@Persistent
	private String schoolName;
	
	@Persistent(defaultFetchGroup="true")
	@Embedded
	private ContactInfo schoolInfo;
	
	@Persistent(defaultFetchGroup="true")
	private GeoPt schoolGeoLocation;
    
    @Persistent
    private String title;
    
    @Persistent
    private String url;
    
    @Persistent
    private Date start;
    
    @Persistent
    private Date end;
    
    @Persistent
    private Text information;
    
    @Persistent
    private String tags;
            
    @Persistent
    private String province;
    
    @Persistent
    private String region;
    
    @Persistent
    private String town;

    @Persistent
    private String lang;
    
    @Persistent
    private Boolean regulated; 
    
    public CourseCatalog() {
		super();
	}
	
	public CourseCatalog(Course course, String lang) {
		this.courseId = course.getId();
		this.externalCourseId = course.getExternalId();
		this.title = course.getTitle();
		this.start = course.getStart();
		this.end = course.getEnd();
		this.information = course.getInformation();
		this.lang = lang;
		this.regulated = course.getRegulated() != null ? course.getRegulated() : Boolean.FALSE;
		this.url = course.getUrl();
		// Metadata
		StringBuffer keyWords = new StringBuffer();
		for(Category tag : course.getTags()) {
			keyWords.append(tag.getCategory()).append(",");
		}
		this.tags = keyWords.toString();
	}
	
	public CourseCatalog(Course course, String lang, School school, String provider, String province, String region, String town) {
		this(course, lang);
		this.schoolName = school.getName();
		this.schoolInfo = school.getContactInfo();
		this.schoolGeoLocation = school.getGeoLocation();
		this.providerName = provider;
		this.province = province;
		this.region = region;
		this.town = town;
	}
    
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

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public ContactInfo getSchoolInfo() {
		return schoolInfo;
	}

	public void setSchoolInfo(ContactInfo schoolInfo) {
		this.schoolInfo = schoolInfo;
	}

	public GeoPt getSchoolGeoLocation() {
		return schoolGeoLocation;
	}

	public void setSchoolGeoLocation(GeoPt schoolGeoLocation) {
		this.schoolGeoLocation = schoolGeoLocation;
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


	public Date getStart() {
		return start;
	}


	public void setStart(Date start) {
		this.start = start;
	}


	public Date getEnd() {
		return end;
	}


	public void setEnd(Date end) {
		this.end = end;
	}


	public Text getInformation() {
		return information;
	}


	public void setInformation(Text information) {
		this.information = information;
	}


	public String getTags() {
		return tags;
	}


	public void setTags(String tags) {
		this.tags = tags;
	}


	public String getProvince() {
		return province;
	}


	public void setProvince(String province) {
		this.province = province;
	}


	public String getRegion() {
		return region;
	}


	public void setRegion(String region) {
		this.region = region;
	}


	public String getTown() {
		return town;
	}


	public void setTown(String town) {
		this.town = town;
	}


	public String getExternalCourseId() {
		return externalCourseId;
	}

	public void setExternalCourseId(String externalCourseId) {
		this.externalCourseId = externalCourseId;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public Boolean getRegulated() {
		return regulated;
	}

	public void setRegulated(Boolean regulated) {
		this.regulated = regulated;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("[");
		sb.append("id: ").append(this.getId()).append(", ");
		sb.append("title: ").append(this.title);
		sb.append("]");
		return sb.toString();
	}
}