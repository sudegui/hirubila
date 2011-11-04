package com.m4f.utils.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.m4f.business.domain.InternalUser;
import com.m4f.business.persistence.PMF;
import com.m4f.utils.dao.ifc.DAOSupport;

public class JdoDAO implements DAOSupport {
	
		
	public <T> T createInstance(Class<T> clazz) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		return pm.newInstance(clazz);
	}
	
	public <T> List<Long> getAllIds(Class<T> clazz, String ordering, String filter, String params, Object[] values) throws Exception {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = null;
		if(filter != null && ("").equals(filter)) {
			q = pm.newQuery("select id from " + clazz.getName(), filter);
			q.declareParameters(params);
		} else {
			q = pm.newQuery("select id from " + clazz.getName());
		}
		 
		this.setOrdering(q, ordering);
		List<Long> ids = null;
		
		if(filter != null && ("").equals(filter)) {
			ids = (List<Long>) q.executeWithArray(values);
		} else {
			ids = (List<Long>) q.execute();
		}
		
		 return ids;
	}
	
	@Override
	public <T> void saveOrUpdate(T obj) throws Exception {
		PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
        	pm.makePersistent(obj);
        } catch(Exception e) {
        	throw e;
        } finally {
            pm.close();
        }
	}
	
	@Override
	public <T> void delete(T object) throws Exception {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.deletePersistent(object);
		} catch(Exception e) {
	    	throw e;
	    } finally {
	    	pm.close();
	    }
	}
	
	@Override
	public <T> void delete(Collection<T> objects) throws Exception {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.deletePersistentAll(objects);
		} catch(Exception e) {
	    	throw e;
	    } finally {
	    	pm.close();
	    }
	}
	
	@Override
	public <T> void deleteAll(Class<T> clazz) throws Exception {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		// The Query interface assembles a query
		com.google.appengine.api.datastore.Query q = 
			new com.google.appengine.api.datastore.Query(clazz.getSimpleName());
		PreparedQuery pq = datastore.prepare(q);
		for (Entity result : pq.asIterable()) {
			datastore.delete(result.getKey());
		}
	}
	
	@Override
	public <T> long count(Class<T> entityClass, 
			Map<String, Object> propertyMap) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		// The Query interface assembles a query
		com.google.appengine.api.datastore.Query q = 
			new com.google.appengine.api.datastore.Query(entityClass.getSimpleName());
		for(String propertyName : propertyMap.keySet()) {
			q.addFilter(propertyName, 
					com.google.appengine.api.datastore.Query.FilterOperator.EQUAL, 
					propertyMap.get(propertyName));
		}
		PreparedQuery pq = datastore.prepare(q);
		return pq.countEntities(FetchOptions.Builder.withLimit(100000));
	}
	
	@Override
	public <T> long count(Class<T> entityClass) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		// The Query interface assembles a query
		com.google.appengine.api.datastore.Query q = 
			new com.google.appengine.api.datastore.Query(entityClass.getSimpleName());
		PreparedQuery pq = datastore.prepare(q);	
		return pq.countEntities(FetchOptions.Builder.withLimit(100000));
	}
	
	@Override
	public <T> T findById(Class<T> clazz, Long id) throws Exception {	
		PersistenceManager pm = PMF.get().getPersistenceManager();
		T obj = null;
		try {
			obj = pm.getObjectById(clazz, id);
		} catch(Exception e) {
			throw e;
		} finally {
	        pm.close();
	    }
		return obj;
	}
	
	@Override
	 public <T> T findByKey(Class<T> clazz, String key) throws Exception {
		 PersistenceManager pm = PMF.get().getPersistenceManager(); 
		 T obj = null;
		 try {
			 Key k = KeyFactory.createKey(clazz.getSimpleName(), key);
			 obj = pm.getObjectById(clazz, k);
		 } catch(Exception e) {
			 throw e;
		 } finally {
			 pm.close();
		} 
	    return obj;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> Collection<T> findCollectionById(Class<T> clazz, Collection<Long> ids) throws Exception {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Collection<T> objs = new ArrayList<T>();
		try {
			 Query query = pm.newQuery(clazz,":p.contains(id)");
			 if((ids!=null)&&(ids.size()>0)) {
				 objs.addAll((Collection<T>)query.execute(ids));
			 }
		} catch(Exception e) {
			throw e;
		} finally {
	        pm.close();
	    }
		return objs;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> List<T> findAll(Class<T> clazz) throws Exception {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(clazz);
		List<T> results = new ArrayList<T>();
		try {
	        results.addAll((List<T>)query.execute());
	    } catch(Exception e) {
	    	throw e;
	    } finally {
	        query.closeAll();
	        pm.close();
	    }
	    return results;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> List<T> findAll(Class<T> clazz, String ordering) throws Exception {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(clazz);
		this.setOrdering(query, ordering);
		List<T> results = new ArrayList<T>();
		try {
	        results.addAll((List<T>)query.execute());
	    } catch(Exception e) {
	    	throw e;
	    } finally {
	        query.closeAll();
	        pm.close();
	    }
	    return results;
	}
	
	/**
	 * Find an entity of a given class using a filter.
	 * @param <T> type of entity to look up.
	 * @param entityClass class of entity to be found.
	 * @param filter (JDOQL) filter to apply to entity lookup.
	 * @param params declarations of parameters in filter string.
	 * @param values values of parameters (in order of appearance in
	 * 	'params' string).
	 * @return entity or <code>null</code> if no entity of the provided class
	 * 	exists matching the provided filter. 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T findEntity(
			Class<T> entityClass,
			String filter,
			String params,
			Object[] values)	{
		Collection<T> entities =
			findEntities(entityClass, filter, params, values, null);
		if (entities.isEmpty())
			return null;		
		return entities.iterator().next();
	}
	
	
	/**
	 * Find all entities of a given class in a given order, matching a filter.
	 * @param <T> type of entity to look up.
	 * @param entityClass class of entities to be found.
	 * @param filter (JDOQL) filter to apply to entity lookup.
	 * @param params declarations of parameters in filter string.
	 * @param values values of parameters (in order of appearance in
	 * 	'params' string).
	 * @param ordering ordering to be applied to results (can be <code>null</code>).
	 * @return entity or <code>null</code> if no entity of the provided class
	 * 	exists matching the provided filter. 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> Collection<T> findEntities(Class<T> entityClass, 
			String filter, String params, Object[] values, String ordering) {
		// construct query
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(entityClass, filter);
		query.declareParameters(params);
		this.setOrdering(query, ordering);
		// execute query
		Collection<T> entities = (Collection<T>) query.executeWithArray(values);
		Collection<T> detachedEntities = pm.detachCopyAll(entities);
		//pm.close();
		return detachedEntities;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> Collection<T> findEntitiesByRange(Class<T> entityClass, 
			String ordering, int init, int end) {
		// construct query
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(entityClass);
		this.setOrdering(query, ordering);
		if((init<end) && (init>=0)) {
			query.setRange(init, end);
		}
		// execute query
		Collection<T> entities = (Collection<T>) query.execute();
		Collection<T> detached = pm.detachCopyAll(entities);
		pm.close();
		return detached;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Collection<T> findEntitiesByRange(
			Class<T> entityClass, String filter, String params,
			Object[] values, String ordering, int init, int end) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(entityClass, filter);
		query.declareParameters(params);
		this.setOrdering(query, ordering);
		if((init<end) && (init>=0)) {
			query.setRange(init, end);
		}
		// execute query
		Collection<T> entities = (Collection<T>) query.executeWithArray(values);
		Collection<T> detached = pm.detachCopyAll(entities);
		pm.close();
		return detached;
	}
	
	private void setOrdering(Query query, String ordering) {
		if (ordering != null && !("").equals(ordering)){
			StringBuffer sb = new StringBuffer();
			String[] orderParams = ordering.split("[,]");
			for(String param : orderParams) {
				if(!param.startsWith("-")) sb.append(param).append(" ascending");
				else sb.append(param.substring(1)).append(" descending");
				sb.append(",");
			}
			sb.deleteCharAt(sb.length()-1); // Removes the last comma
			query.setOrdering(sb.toString());		
		}
	}
}