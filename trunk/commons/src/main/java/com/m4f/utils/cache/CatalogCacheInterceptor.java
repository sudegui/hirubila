package com.m4f.utils.cache;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.aspectj.lang.ProceedingJoinPoint;

import com.google.appengine.api.memcache.MemcacheService;
import com.m4f.business.domain.Course;
import com.m4f.business.domain.CourseCatalog;
import com.m4f.business.service.impl.CourseServiceImpl;
import com.m4f.business.service.impl.GaeJdoCatalogService;
import com.m4f.utils.StackTraceUtil;
import com.m4f.utils.cache.annotations.Cacheable;
import com.m4f.utils.cache.annotations.CatalogCacheable;



public class CatalogCacheInterceptor extends CacheInterceptor {
	
	protected static final Logger LOGGER = Logger.getLogger(CatalogCacheInterceptor.class.getName());
	
	public Object cacheAble(ProceedingJoinPoint pjp) throws Throwable {
		// Getting the cacheName linked with this method
		String cacheName = "";
		Method method = null;
		try {
			method = this.getInterceptedMethod(pjp);
			CatalogCacheable annotation = method.getAnnotation(CatalogCacheable.class);
			cacheName = annotation != null ? annotation.cacheName() : "default";
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
			cacheName = "default";
		} finally {
			
		}
		
		// Look if method is getCoursesCatalog
		boolean cacheCollection = false;
		if(this.isCatalogCollection(method.getName(), pjp.getArgs())) cacheCollection = true;
		
		Object key = this.getMethodKey(method.toGenericString(), pjp.getArgs());
		
		Object result = null;
		//InternalCache internalCache = this.getCache(cacheName);
		MemcacheService syncCache = this.getCache(cacheName);
		
		if(syncCache == null) {
			LOGGER.severe("NO CACHE!!!!! Executing the method with no cache!!");
		} else {
			result = syncCache.get(key);
		}
		
		if(result != null) {
			LOGGER.info("Returning value with key: " + key + " from cache.");
			return result;
		} else {
			try {
				LOGGER.info("NO CACHE! Invoking the method!");
				result =  pjp.proceed();
				if(cacheCollection) this.cacheCollection(result, pjp.getArgs(), syncCache);
				LOGGER.info("NO CACHE! Inserting result into internal cache with key: " + key);
				syncCache.put(key, result);
				LOGGER.info("NO CACHE! Adding to cache internal cache with name: " + cacheName);
				//this.addCache(cacheName, internalCache);
			} catch(Exception e) {
				LOGGER.severe(StackTraceUtil.getStackTrace(e));
				LOGGER.severe("Memcache KBs: " + 
						syncCache.getStatistics().getTotalItemBytes() / 1024);
			}
		}
		return result;
	}
	
	/**
	 * This method returns true if it matches the getCoursesCatalog(boolean reglated,
			String ordering, Locale locale, int init, int end) from GaeJdoCatalogService.
	 * @param methodName
	 * @param args
	 */
	private boolean isCatalogCollection(String methodName, Object[] args) {
		boolean match = true;
		
		if("getCourses".equals(methodName) && args.length == 5) {
			for(int i = 0; i < args.length && match; i++) {
				switch(i) {
				case 0:
					match = match && (args[i].getClass().equals(Boolean.class));
					break;
				case 1:
					match = match && (args[i].getClass().equals(String.class));
					break;
				case 2:
					match = match && (args[i].getClass().equals(Locale.class));
					break;
				case 3:
					match = match && (args[i].getClass().equals(Integer.class));
					break;
				case 4:
					match = match && (args[i].getClass().equals(Integer.class));
					break;
				}
			}
		} else {
			match = false;
		}
		
		return match;
	}
	
	/**
	 * This method will insert all objects from the result collection into the cache, simulating that was executed the detail method of each one.
	 * @param result
	 * @param args
	 * @param cache
	 */
	private void cacheCollection(Object result, Object[] args, MemcacheService cache) {
		if(result instanceof Collection) {
			Collection collection = (Collection) result;
			Iterator it = collection.iterator();
			while(it.hasNext()) {
				Object o = it.next();
				if(o instanceof Course) {
					Course course = (Course) o;
					try {
						Method goalMethod = 
								CourseServiceImpl.class.getMethod("getCourse", new Class[]{Long.class, Locale.class});
						String detailMethodName = goalMethod.toGenericString();
						Object[] params = new Object[] {course.getId(), args[2]};
						Object key = this.getMethodKey(detailMethodName, params);
						cache.put(key, course);
					} catch(NoSuchMethodException e) {
						LOGGER.severe(StackTraceUtil.getStackTrace(e));
					} catch(Exception e) {
						LOGGER.severe(StackTraceUtil.getStackTrace(e));
						LOGGER.severe("Memcache KBs: " + 
								cache.getStatistics().getTotalItemBytes() / 1024);
					}
				}
			}
		}
	}
}