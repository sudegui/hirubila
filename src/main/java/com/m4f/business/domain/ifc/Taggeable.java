package com.m4f.business.domain.ifc;

import java.util.Collection;

public interface Taggeable {
	
	void addTag(String tag);
	void removeTag(String tag);
	void addTags(Collection<String> tags);
	void removeTags(Collection<String> tags);
	
}