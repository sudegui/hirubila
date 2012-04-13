package com.m4f.utils.dao;

import com.google.appengine.api.datastore.Query.FilterOperator;

public class GaeFilter {
	public GaeFilter(String field, FilterOperator operator, Object value) {
		this.field = field;
		this.operator = operator;
		this.value = value;
	}
	
	private String field;
	
	private FilterOperator operator;
	
	private Object value;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public FilterOperator getOperator() {
		return operator;
	}

	public void setOperator(FilterOperator operator) {
		this.operator = operator;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	
}
