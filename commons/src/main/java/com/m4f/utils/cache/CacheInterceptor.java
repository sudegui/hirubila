package com.m4f.utils.cache;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import com.m4f.utils.StackTraceUtil;
import com.m4f.utils.cache.annotations.Cacheable;
import com.m4f.utils.cache.annotations.Cacheflush;



public class CacheInterceptor {
	protected static final Logger LOGGER = Logger.getLogger(CacheInterceptor.class.getName());
	protected static Cache cache;
	
	public CacheInterceptor() {
	}
		
	
	public Object cacheAble(ProceedingJoinPoint pjp) throws Throwable {
		// Getting the cacheName linked with this method
		String cacheName = "";
		Method method = null;
		try {
			method = this.getInterceptedMethod(pjp);
			Cacheable annotation = method.getAnnotation(Cacheable.class);
			cacheName = annotation != null ? annotation.cacheName() : "default";
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
			cacheName = "default";
		} finally {
			
		}
				
		Object key = this.getMethodKey(method.toGenericString(),pjp.getArgs());
		Object result = null;
		InternalCache internalCache = this.getCache(cacheName);
		if(internalCache == null) {
			LOGGER.info("NO CACHE! with name -> " + cacheName);
			LOGGER.info("NO CACHE! Creating cache with name -> " + cacheName);
			internalCache = new InternalCache();
		} else {
			result = internalCache.get(key);
		}
		
		if(result != null) {
			LOGGER.info("Returning value with key: " + key + " from cache.");
			return result;
		} else {
			try {
				LOGGER.info("NO CACHE! Invoking the method!");
				result =  pjp.proceed();
				LOGGER.info("NO CACHE! Inserting result into internal cache with key: " + key);
				internalCache.put(key, result);
				LOGGER.info("NO CACHE! Adding to cache internal cache with name: " + cacheName);
				this.addCache(cacheName, internalCache);
			} catch(Exception e) {
				LOGGER.severe(StackTraceUtil.getStackTrace(e));
			}
		}
		return result;
	}
	
	public void cacheFlush(ProceedingJoinPoint pjp) throws Throwable {
		// Getting the cacheName linked with this method
		String cacheName = "";
		Method method = null;
		try {
			method = this.getInterceptedMethod(pjp);
			Cacheflush annotation = method.getAnnotation(Cacheflush.class);
			cacheName = annotation != null ? annotation.cacheName() : "default";
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
			cacheName = "default";
		}
		
		InternalCache internalCache = this.getCache(cacheName);
		if(internalCache == null) {
			LOGGER.log(Level.INFO, "NO CACHE TO FLUSH! with name -> " + cacheName);
		} else {
			LOGGER.log(Level.INFO, "Flushing cache with name -> " + cacheName);
			this.addCache(cacheName, null);
		}
		
		pjp.proceed();
	}
	
	protected InternalCache getCache(String name) {
		if(cache == null) {
			initCacheService();
		}
		try {
			InternalCache internal = (InternalCache) cache.get(name);
			return internal;
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return new InternalCache();
		}
	}
	
	protected void addCache(String name, InternalCache internalCache) {
		if(cache == null) {
			initCacheService();
		}
		LOGGER.log(Level.INFO, "Adding new Cache to cacheTable with name: " + name);
		cache.put(name, internalCache);
	}
	
	protected Method getInterceptedMethod(ProceedingJoinPoint pjp) throws SecurityException, NoSuchMethodException {
		MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
	    Method method = methodSignature.getMethod();
	    Object target = pjp.getTarget();
		Class clazz = target.getClass();
		Method m = clazz.getMethod(method.getName(), method.getParameterTypes());
		
		return m;
    }
	
	protected Object getMethodKey(String methodName, Object[] args) {
		// TODO Improve this way to generate unique keys for cache
		StringBuffer key = new StringBuffer(methodName.trim().replace(" ", ".")).append(".");
		for(Object o : args) {
			if(o != null)	key.append(o.hashCode());
		}
		LOGGER.log(Level.INFO, "Generation key ->" + key.toString());
		return key.toString();
	}
	
	protected void initCacheService() {
		try {
			LOGGER.log(Level.INFO, "No cache, initializing cache service...");
	        CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
	        cache = cacheFactory.createCache(Collections.emptyMap());
	       
	    } catch (CacheException e) {
	    	LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
	    }
	}
}