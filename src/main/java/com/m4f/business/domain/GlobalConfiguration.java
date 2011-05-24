package com.m4f.business.domain;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Value;

@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class GlobalConfiguration extends BaseEntity {
	public enum SEARCH_ENGINE {GSA};
	
	@PrimaryKey  
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	@Persistent
	//@Value("#{settings['search.engine.name']}")
	@NotNull(message="Not Null")
	private String searchEngine = "GSA";
	
	@Persistent
	//@Value("#{settings['search.engine.uri']}")
	@NotNull(message="Not Null")
	private String searchUri = "http://gsa.m4f.es:8081/search";
	
	@Persistent
	//@Value("#{settings['page.size']}")
	@NotNull(message="Not Null")
	@Min(value=10, message="Value should be higher")
	@Max(value=100, message="Value should be lower")
	private Integer pageSize = 30;
	
	@Persistent
	//@Value("#{settings['application.languages']}")
	@NotNull(message="Not Null")
	private String languages = "es,eu";
	
	@Persistent
	//@Value("#{settings['search.engine.collection']}")
	@NotNull(message="Not Null")
	private String searchBaseCollectionName = "hirubila-";

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSearchEngine() {
		return searchEngine;
	}

	public void setSearchEngine(String searchEngine) {
		this.searchEngine = searchEngine;
	}

	public String getSearchUri() {
		return searchUri;
	}

	public void setSearchUri(String searchUri) {
		this.searchUri = searchUri;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getLanguages() {
		return languages;
	}

	public void setLanguages(String languages) {
		this.languages = languages;
	}

	public String getSearchBaseCollectionName() {
		return searchBaseCollectionName;
	}

	public void setSearchBaseCollectionName(String searchBaseCollectionName) {
		this.searchBaseCollectionName = searchBaseCollectionName;
	}
}
