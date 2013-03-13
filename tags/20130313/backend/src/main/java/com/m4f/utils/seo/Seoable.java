package com.m4f.utils.seo;

import java.util.Map;
import java.util.HashMap;

public abstract class Seoable {
	
	private Map<String, String> keywords = new HashMap<String, String>();
	
	
	public void addKeyword(String name, String value) {
		this.keywords.put(name, value);
	}
	
	public Map<String, String> getKeywords() {
		return this.getKeywords();
	}
	
	
}