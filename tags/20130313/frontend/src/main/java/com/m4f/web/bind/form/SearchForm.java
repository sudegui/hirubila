package com.m4f.web.bind.form;

public class SearchForm {
	
	private String query;
	private Integer start;
	private String inMeta;
	private String collection;
	
	public SearchForm() {
		super();
		this.query = "";
		this.start = new Integer(0);
		this.inMeta = "";
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public String getInMeta() {
		return inMeta;
	}

	public void setInMeta(String inMeta) {
		this.inMeta = inMeta;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public String getCollection() {
		return collection;
	}
}
