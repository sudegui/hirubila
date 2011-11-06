package com.m4f.utils.feeds.parser.impl;

import com.m4f.business.domain.BaseEntity;
import com.m4f.business.service.ifc.I18nCourseService;
import com.m4f.business.service.ifc.I18nSchoolService;
import com.m4f.utils.beans.BeanManager;
import java.util.List;
import java.util.Locale;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Validator;

public abstract class StoreBase<T extends BaseEntity> {
	
	@Autowired
	protected I18nCourseService courseService;
	@Autowired
	protected I18nSchoolService schoolService;
	@Autowired
	protected Validator validator;
	@Autowired
	protected BeanManager beanManager;
	
	protected List<T> entities = new ArrayList<T>();
	
	protected abstract void flush(Locale locale) throws Exception;
	
}