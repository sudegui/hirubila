package com.m4f.utils.i18n;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import java.util.Collection;
import java.lang.reflect.Field;
import com.google.appengine.api.datastore.Text;
import com.m4f.business.domain.BaseEntity;
import com.m4f.utils.beans.BeanManager;
import com.m4f.utils.i18n.annotations.Multilanguage;
import com.m4f.utils.i18n.annotations.MultilanguageEmbedded;
import com.m4f.utils.i18n.annotations.MultilanguageCollection;
import com.m4f.utils.i18n.model.ifc.I18nBehaviour;
import com.m4f.utils.i18n.model.impl.JdoI18nEntry;
import com.m4f.utils.i18n.service.ifc.I18nService;
import com.google.appengine.api.datastore.Category;

public class MultilanguageInterceptor {
	
	private static final Logger LOGGER = Logger.getLogger(MultilanguageInterceptor.class.getName());
	
	private I18nService i18nService;
	private BeanManager beanManager;
	
	
	public MultilanguageInterceptor(I18nService i18n, BeanManager beanManager) {
		this.i18nService = i18n;
		this.beanManager = beanManager;
	}
		
		
	public void loadCollection(List retVal, Locale locale, Object clazz) throws Throwable {
		if(locale == null) {
			return;
		}
		LOGGER.info("*********** Load Collection....................");
		if(retVal == null) {
			return;
		}
		this.loadCollection(locale, (List<I18nBehaviour>)retVal);
	}
	
	
	public void loadEntity(Object clazz, I18nBehaviour retVal, 
			Locale locale) throws Throwable {
		if(locale == null) {
			return;
		}
		LOGGER.info("*********** Load entity....................");
		if(retVal == null) {
			return;
		}
		this.loadObject(locale, retVal);
	}
	
	
	
	
	public <T extends I18nBehaviour> void deleteCollection(Collection<T> objs, 
			Locale locale) throws Exception {
		for(T object : objs) {
			this.deleteAllTranslations(object);
		}
	}
	
	
	/**
	 * Intercepts methods that declare com.m4f.utils.i18n.annotations.DeleteMultilanguage 
	 * and prints out the time it takes to complete.
	 * 
	 * @param pjp proceeding join point
	 * @return the intercepted method returned object
	 * @throws Throwable in case something goes wrong in the actual method call
	 */
	@SuppressWarnings("unchecked")
	public <T extends I18nBehaviour> Object deleteMultilanguage(ProceedingJoinPoint pjp) throws Throwable {
		T object = (T) pjp.getArgs()[0];
		Object retVal = pjp.proceed();
		this.deleteAllTranslations(object);
		return retVal;
	}
	
		
	/**
	 * Intercepts methods that declare com.m4f.utils.i18n.annotations.PutMultilanguage
	 * and prints out the time it takes to complete.
	 * 
	 * @param pjp proceeding join point
	 * @return the intercepted method returned object
	 * @throws Throwable in case something goes wrong in the actual method call
	 */
	/*public Object putMultilanguage(ProceedingJoinPoint pjp) throws Throwable {
		Object retVal = pjp.proceed();
		Object object = pjp.getArgs()[0];
		this.deleteTranslations(this.discoverLocale(pjp.getArgs()), (I18nBehaviour)object);
		this.locateMultiLanguageFields(this.discoverLocale(pjp.getArgs()), object);
		return retVal;
	}*/
	
	
	public <T extends I18nBehaviour> void putMultilanguage(T entity, Locale locale) throws Throwable {
		if(locale == null) {
			return;
		}
		this.deleteTranslations(locale, entity);
		this.locateMultiLanguageFields(locale, entity);
	}
	
		
	/****************************************************************************************
	 * 
	 *
	 *									PRIVATE METHODS
	 * @throws Exception 
	 * @throws Exception 
	 *
	 *
	 ****************************************************************************************/
	
	private <T extends I18nBehaviour> void deleteAllTranslations(T object) throws Exception {
		Collection<JdoI18nEntry> entries = this.i18nService.getObjectTranslations(object);
		this.i18nService.delete(entries);
	}
	
	private <T extends I18nBehaviour> void deleteTranslations(Locale locale, 
			T object) throws Exception {
		Collection<JdoI18nEntry> entries = this.i18nService.
			getObjectTranslations(locale, object);
		this.i18nService.delete(entries);
	}
	
	/****
	 * 
	 * @param locale
	 * @param obj
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 */
	
	private <T extends I18nBehaviour> void loadObject(Locale locale, T obj) throws 
		IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {
		Collection<JdoI18nEntry> entries = this.i18nService.getObjectTranslations(locale, obj);
		this.loadValues(this.checkIntegrity(entries, obj), obj);
	}
	
	private <T extends I18nBehaviour> void loadCollection(Locale locale, List<T> objs) 
		throws IllegalArgumentException, SecurityException, IllegalAccessException, 
		NoSuchFieldException {
		Collection<JdoI18nEntry> entries = null;
		for(T obj : objs) {
			entries = this.i18nService.getObjectTranslations(locale, obj);
			this.loadValues(this.checkIntegrity(entries, obj), obj);
		}
	}
	
	
	private <T extends I18nBehaviour> Collection <JdoI18nEntry> checkIntegrity(Collection<JdoI18nEntry> entries, 
			T obj) throws SecurityException, NoSuchFieldException {
		LOGGER.info("++++ Checking translations integrity");
		Collection<JdoI18nEntry> finalEntries = new ArrayList<JdoI18nEntry>();
		Set<String> ents = new HashSet<String>();
		String propertyName = "";
		for(JdoI18nEntry entry : entries) {
			propertyName = entry.getFieldKey();
			LOGGER.info("Introspecting " + propertyName);
			if(ents.contains(propertyName)) {
				if(propertyName.contains(".")) {
					continue;
				}
				if(this.beanManager.hasProperty(obj, propertyName)) {
					Field field = obj.getClass().getField(propertyName);
					LOGGER.info("Introspecting field element" + field.getName());
					if(field.isAnnotationPresent(MultilanguageCollection.class)) {
						LOGGER.info("Exist, adding " + propertyName);
						finalEntries.add(entry);
					}
				}
			} else {
				LOGGER.info("Not exist, adding " + propertyName);
				finalEntries.add(entry);
				ents.add(propertyName);
			}	
		}
		LOGGER.info("Final resumen.............");
		LOGGER.info("Init size: " + entries.size());
		LOGGER.info("Init size: " + finalEntries.size());
		return finalEntries;
	}
	
	private <T extends I18nBehaviour> void loadValues(Collection<JdoI18nEntry> entries, 
			T obj) throws IllegalArgumentException, SecurityException, 
			IllegalAccessException, NoSuchFieldException {	
		Map<String, Object> embedded = new HashMap<String, Object>();
		Map<String, Object> properties = new HashMap<String, Object>();
		Object value;
		if(entries.size()>0) {
			obj.setTranslated(true);
		}
		for(JdoI18nEntry entry : entries) {
			
			/*Embedded fields are saved with this pattern.*/
			if(entry.getFieldKey().contains(".")) {
				embedded.put(entry.getFieldKey(), entry.getFieldValue().getValue());
				continue;
			}
			
			
			/* Direct fields, this fragment of code has controlled simple or 
			 * collection attributes of the target object.*/
			if(properties.containsKey(entry.getFieldKey())) {
				value = properties.get(entry.getFieldKey());
				if(value instanceof List) {
					((List) value).add(entry.getFieldValue().getValue());
				} else {
					List valueL = new ArrayList();
					valueL.add(value);
					valueL.add(entry.getFieldValue().getValue());
					properties.put(entry.getFieldKey(), valueL);
				}
			} else {
				properties.put(entry.getFieldKey(), entry.getFieldValue().getValue());
			}
			
		}
		this.beanManager.setProperties(properties, obj);
		/*Se procesan los campos embedded.*/
		this.loadEmbedded(obj, embedded);
	}
	
	
	
	@SuppressWarnings("unchecked")
	private <T extends I18nBehaviour> void loadEmbedded(T parent, 
			Map<String, Object> embedded) throws SecurityException, 
			IllegalArgumentException, IllegalAccessException, NoSuchFieldException {
		Object child;
		String[] comps;
		Map<String, Object> properties;
		String propertyName = "";
		for(String embed : embedded.keySet()) {	
			comps = embed.split("[.]");
			propertyName = comps[0];
			if(!this.beanManager.hasProperty(parent, propertyName)) {
				continue;
			}
			/*Se consigue el objeto Embedded.*/
			child = parent.getClass().getDeclaredField(propertyName).get(parent);
			properties = new HashMap<String, Object>();
			properties.put(comps[1], embedded.get(embed));
			this.beanManager.setProperties(properties, child);				
		}
	}
	
	
	private void locateMultiLanguageFields(Locale locale, Object object) 
		throws IllegalArgumentException, IllegalAccessException, Exception {
		Class aClass = object.getClass();
		Field[] fields = aClass.getDeclaredFields();
		for(Field field : fields) {
			if(field.isAnnotationPresent(Multilanguage.class)) {
				LOGGER.info("Annotation located in " + field.getName());
				Object value;
				if(field.getType().getName().equals(Text.class.getName()) && field.get(object) != null) {
					value = ((Text)field.get(object)).getValue();
				} else {
					value = field.get(object);
				}
				this.putSimpleField(locale, (I18nBehaviour)object, field.getName(), value);			
			} else if(field.isAnnotationPresent(MultilanguageEmbedded.class)) {
				this.putObjectField(locale, (I18nBehaviour)object, field);
			} else if(field.isAnnotationPresent(MultilanguageCollection.class)) {
				this.putCollectionField(locale, (I18nBehaviour)object, field);
			}
		}
	}
	
	
	@SuppressWarnings("unchecked")
	private void putCollectionField(Locale locale, I18nBehaviour mainObj, 
			Field field) throws Exception {
		Collection collection = null;
		if(field.get(mainObj)!=null) {
			collection = (Collection)field.get(mainObj);
		}
		Object value;
		//Se borran todas las traducciones 
		List<String> fieldNames = new ArrayList<String>();
		fieldNames.add(field.getName());
		this.i18nService.selectiveDelete(mainObj, fieldNames, locale);
		if(collection != null) {
			for(Object element : collection) {
				if(element instanceof Category) {
					value = ((Category)element).getCategory();
					this.putSimpleField(locale, mainObj, field.getName(), value);
				}
			}
		}
	}
	
		
	private void putObjectField(Locale locale, I18nBehaviour mainObj, 
			Field field) throws Exception {
		Object secondObj = field.get(mainObj);
		if(secondObj == null) {
			return;
		}
		for(Field targetField : secondObj.getClass().getDeclaredFields()) {
			if(targetField.isAnnotationPresent(Multilanguage.class)) {
				Object value ;
				if(targetField.getType().isInstance(Text.class)) {
					value = ((Text)targetField.get(secondObj)).getValue();
					 
				} else {
					value = targetField.get(secondObj);
				}
				this.putSimpleField(locale, mainObj, field.getName() + 
						"." + targetField.getName(), value);
			}
		}
	}
	
	private void putSimpleField(Locale locale, I18nBehaviour obj, 
			String fieldKey, Object fieldContent) throws Exception {
		String fieldValue =  fieldContent==null?"":fieldContent.toString();
		JdoI18nEntry entry = (JdoI18nEntry) this.i18nService.createTranslationUnit();
		entry.setFieldKey(fieldKey);
		entry.setFieldValue(new Text(fieldValue));
		entry.setObjectClass(obj.getClass().getName());
		entry.setContentId("" + obj.getId());
		entry.setLang(locale.getLanguage());
		this.i18nService.saveOrUpdate(entry);
	}
	
	
	
	private Locale discoverLocale(Object[] objects) {
		Locale locale = Locale.getDefault();
		for(Object object : objects) {
			if(object.getClass().isInstance(locale)) {
				locale = (Locale) object;
			}
		}
		return locale;
	}
	
	
}