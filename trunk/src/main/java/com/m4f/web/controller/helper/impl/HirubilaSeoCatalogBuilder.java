package com.m4f.web.controller.helper.impl;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.appengine.repackaged.org.joda.time.tz.Provider;
import com.m4f.business.domain.Course;
import com.m4f.business.domain.CourseCatalog;
import com.m4f.business.domain.School;
import com.m4f.business.domain.extended.Province;
import com.m4f.business.service.ifc.ICatalogService;
import com.m4f.business.service.ifc.IServiceLocator;
import com.m4f.utils.seo.SeoCatalogBuilder;
import com.m4f.utils.seo.Seoable;

public class HirubilaSeoCatalogBuilder implements SeoCatalogBuilder {

	@Autowired
	protected IServiceLocator serviceLocator;
	
	
	@Override
	public <T extends Seoable> T buildSeoEntity() {
		// TODO Auto-generated method stub
		return null;
	}

}