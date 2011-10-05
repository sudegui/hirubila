package com.m4f.utils.beans;

import java.beans.PropertyDescriptor;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.MutablePropertyValues;
import java.util.HashSet;
import com.m4f.utils.beans.exception.NotSameClassException;

public class BeanManager {
	
	private static final Logger LOGGER = Logger.getLogger(BeanManager.class.getName());
	
	public void setProperties(Map<String, Object> properties, 
			Object target) throws IllegalArgumentException, IllegalAccessException {
		BeanWrapperImpl beanWrapper = new BeanWrapperImpl();
		beanWrapper.setWrappedInstance(target);
		/**Checking the properties*/
		Set<String> propertyNames = new HashSet<String>(); 
		propertyNames.addAll(properties.keySet());
		for(String propertyName : propertyNames) {
			if(!this.hasProperty(target, propertyName)) {
				properties.remove(propertyName);
			}
		}
		MutablePropertyValues pvs = new MutablePropertyValues(properties);
		beanWrapper.setPropertyValues(pvs);
	}
	
	public boolean hasProperty(Object target, String propertyName) {
		BeanWrapperImpl beanWrapper = new BeanWrapperImpl();
		beanWrapper.setWrappedInstance(target);
		return beanWrapper.isReadableProperty(propertyName);
	}
	
	/**
	 * This method merge two objects, but the 
	 * 
	 * 
	 * @param source
	 * @param target
	 * @param properties
	 * @return
	 */
	public void mergeObjects(Object source, Object target, 
			Set<String> properties) throws NotSameClassException {
		if(!source.getClass().getName().equals(target.getClass().getName())) {
			throw new NotSameClassException("Source class (" + source.getClass().getName() + 
					") is not equals that TARGET class (" + target.getClass().getName() + ")");
		}
		BeanWrapperImpl sourceW = new BeanWrapperImpl();
		sourceW.setWrappedInstance(source);
		BeanWrapperImpl targetW = new BeanWrapperImpl();
		targetW.setWrappedInstance(target);
		for(String propertyName : properties) {
			if(sourceW.isWritableProperty(propertyName)) {
				this.mergeProperty(sourceW, targetW, propertyName);
			}
		}
		
	}
		
	private void mergeProperty(BeanWrapperImpl source, 
			BeanWrapperImpl target, String propertyName) {
		target.setPropertyValue(propertyName, 
				source.getPropertyValue(propertyName));
	}
}