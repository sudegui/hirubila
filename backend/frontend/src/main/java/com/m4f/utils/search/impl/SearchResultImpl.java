package com.m4f.utils.search.impl;

import com.m4f.utils.search.ifc.ISearchResult;

public class SearchResultImpl implements ISearchResult {

	private String title;
	private String description;
	private String information;
	private String link;
	private String score;
	private String lang;
	private String mime;
	
	public SearchResultImpl() {
		super();
	}
	
	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getInformation() {
		return information;
	}

	@Override
	public String getLang() {
		return lang;
	}

	@Override
	public String getLink() {
		return link;
	}

	@Override
	public String getScore() {
		return score;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getMime() {
		return this.mime;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setInformation(String information) {
		this.information = information;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}
	
	public void setMime(String mime) {
		this.mime = mime;
	}
}
