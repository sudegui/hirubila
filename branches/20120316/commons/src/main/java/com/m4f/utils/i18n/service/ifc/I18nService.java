package com.m4f.utils.i18n.service.ifc;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import com.m4f.utils.i18n.model.ifc.I18nBehaviour;
import com.m4f.utils.i18n.model.impl.JdoI18nEntry;

public interface I18nService {
	
	JdoI18nEntry createTranslationUnit();
	
	void saveOrUpdate(JdoI18nEntry entry) throws Exception;
	
	void delete(JdoI18nEntry entry) throws Exception;
	
	void delete(Collection<JdoI18nEntry> entries) throws Exception;
	
	<T extends I18nBehaviour> void selectiveDelete(T obj, List<String> fieldNames, Locale locale) throws Exception;
	
	void deleteAll() throws Exception;
	
	JdoI18nEntry getEntry(Long id) throws Exception;
	
	Map<String, List<JdoI18nEntry>> getTranslations() throws Exception;
	
	<T extends I18nBehaviour> Collection<JdoI18nEntry> getObjectTranslations(Locale locale, T obj);
	
	<T extends I18nBehaviour> Collection<JdoI18nEntry> getObjectTranslations(T obj);
	
	Collection<JdoI18nEntry> getContentTranslations(String contentId);
	
	Map<String, List<JdoI18nEntry>> getContentTranslationsMap(String contentId);
	
	long countTranslationsByLocale(Locale locale);
	
	long countTotalTranslations();
	
}