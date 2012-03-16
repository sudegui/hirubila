package com.m4f.utils.i18n.dao.ifc;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.google.appengine.api.datastore.Category;
import com.m4f.business.domain.BaseEntity;
import com.m4f.business.domain.ifc.Taggeable;

public interface I18nDAOSupport {
	
	<T extends BaseEntity> T createInstance(Class<T> clazz);
	
	<T extends BaseEntity> List<Long> getAllIds(Class<T> clazz, String order, String filter, String params, Object[] values) throws Exception;
	
	void saveOrUpdate(BaseEntity entity, Locale locale) throws Exception;

	<T extends BaseEntity> void saveOrUpdateCollection(Collection<T> entities, Locale locale) throws Exception; 
    
    void delete(BaseEntity entity, Locale locale) throws Exception;
    
    <T extends BaseEntity> T findById(Class<T> clazz, Locale locale, Long id) throws Exception;
    
    <T extends BaseEntity> T findEntity(Class<T> entityClass, Locale locale, String filter, String params, Object[] values);
    
    <T extends BaseEntity> void delete(Collection<T> objs, Locale locale) throws Exception;
    
    <T extends BaseEntity> void erasure(Class<T> clazz) throws Exception;
    	
	<T extends BaseEntity> long count(Class<T> entityClass);
	
	<T extends BaseEntity> long count(Class<T> entityClass, Map<String, Object> propertyMap);
	
	Collection<Category> getCategories(Class<? extends Taggeable> clazz, String fieldName, Locale locale);
	
	<T extends BaseEntity> List<T> findAll(Class<T> clazz, Locale locale, String ordering) throws Exception;
    
    <T extends BaseEntity> Collection<T> findEntities(Class<T> clazz, Locale locale, String filter, String params, Object[] values, String ordering);
    
    <T extends BaseEntity> Collection<T> findEntitiesByRange(Class<T> entityClass, Locale locale, int init, int end, String ordering);
	
    <T extends BaseEntity> Collection<T> findEntitiesByRange(Class<T> entityClass, Locale locale, String filter, String params, Object[] values, int init, int end,String ordering);
    
    <T extends BaseEntity> long countByIds(Class<T> entityClass, String idField, List<Long> ids);
    
    <T extends BaseEntity> Collection<T> findEntitiesByIds(Class<T> entityClass, Locale locale, String idField, List<Long> ids, int init, int end, String ordering) throws Exception;
}