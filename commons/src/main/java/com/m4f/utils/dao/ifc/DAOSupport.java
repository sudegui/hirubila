package com.m4f.utils.dao.ifc;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface DAOSupport {
	
	<T> T createInstance(Class<T> clazz);
	
	<T> void saveOrUpdate(T obj) throws Exception; 
	
	<T> void saveOrUpdateCollection(Collection<T> objs) throws Exception;
    
    <T> void delete(T object) throws Exception;
    
    <T> void delete(Collection<T> objs) throws Exception;
    
    <T> void deleteAll(Class<T> clazz) throws Exception;
    
    <T> long count(Class<T> entityClass);
	
	<T> long count(Class<T> entityClass, Map<String, Object> propertyMap);
    
    <T> T findById(Class<T> clazz, Long id) throws Exception;
    
    <T> T findByKey(Class<T> clazz, String key) throws Exception;
    
    <T> T findEntity(Class<T> entityClass, String filter, String params, Object[] values);
    
    <T> List<Long> getAllIds(Class<T> clazz, String order, String filter, String params, Object[] values) throws Exception;
    
    <T> List<T> findAll(Class<T> clazz) throws Exception;
    
    <T> List<T> findAll(Class<T> clazz, String ordering) throws Exception;
    
    <T> Collection<T> findCollectionById(Class<T> clazz, Collection<Long> ids) throws Exception;
	
	<T> Collection<T> findEntities(Class<T> entityClass, String filter, String params, Object[] values, String ordering);
	
	<T> Collection<T> findEntitiesByRange(Class<T> entityClass, String ordering, int init, int end);
			
	<T> Collection<T> findEntitiesByRange(Class<T> entityClass, String filter, String params, Object[] values, String ordering,int init, int end);
	
}