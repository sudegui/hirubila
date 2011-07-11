package com.m4f.utils.seo;

public interface SeoCatalogBuilder {
	
	<T extends Seoable> T buildSeoEntity();
}