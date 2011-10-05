package com.m4f.utils.i18n.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.m4f.utils.dao.ifc.DAOSupport;
import com.m4f.utils.feeds.events.model.ParserErrorEvent;
import com.m4f.utils.i18n.service.ifc.I18nService;
import com.m4f.utils.i18n.model.ifc.I18nBehaviour;
import com.m4f.utils.i18n.model.impl.JdoI18nEntry;

public class JdoI18nService implements I18nService {
	
	private DAOSupport DAO;
	
	public JdoI18nService(DAOSupport dao) {
		this.DAO = dao;
	}
	
	@Override
	public JdoI18nEntry createTranslationUnit() {
		return (JdoI18nEntry) this.DAO.createInstance(JdoI18nEntry.class);
	}
	
	@Override
	public void saveOrUpdate(JdoI18nEntry entry) throws Exception {
		this.DAO.saveOrUpdate(entry);
	}
	
	@Override
	public JdoI18nEntry getEntry(Long id) throws Exception {
		return (JdoI18nEntry) this.DAO.findById(JdoI18nEntry.class, id);
	}
	
	@Override
	public void delete(JdoI18nEntry entry) throws Exception {
		this.DAO.delete(entry);
	}
	
	@Override
	public void delete(Collection<JdoI18nEntry> entries) throws Exception {
		this.DAO.delete(entries);
	}
	
	@Override
	public Map<String, List<JdoI18nEntry>> getTranslations() throws Exception {
		List<JdoI18nEntry> entries = this.DAO.findAll(JdoI18nEntry.class);
		return this.createIndexedLangMap(entries);
	}
	
	
	
	@Override
	public <T extends I18nBehaviour> Collection<JdoI18nEntry> getObjectTranslations(T obj) {
		Collection<JdoI18nEntry> i18nEntries = this.DAO.findEntities(JdoI18nEntry.class,
			    "contentId == pContentId", "java.lang.String pContentId",
			    new Object[] {obj.getId()}, null);
		return i18nEntries;
	}
	
	@Override
	public Collection<JdoI18nEntry> getContentTranslations(String contentId) {
		Collection<JdoI18nEntry> i18nEntries = this.DAO.findEntities(JdoI18nEntry.class,
			    "contentId == pContentId", "java.lang.String pContentId", 
			    new Object[] {contentId}, null);
		return i18nEntries;
	}
	
	@Override
	public Map<String, List<JdoI18nEntry>> getContentTranslationsMap(String contentId) {
		Collection<JdoI18nEntry> entries = this.DAO.findEntities(JdoI18nEntry.class,
			    "contentId == pContentId", "java.lang.String pContentId", 
			    new Object[] {contentId}, null);
		return this.createIndexedLangMap(entries);
	}
	
	@Override
	public <T extends I18nBehaviour> Collection<JdoI18nEntry> getObjectTranslations(Locale locale, T obj){
		Collection<JdoI18nEntry> i18nEntries = this.DAO.findEntities(JdoI18nEntry.class,
				"contentId == pContentId && lang == pLang",
				"java.lang.String pContentId,java.lang.String pLang", 
				new Object[] {""+obj.getId(),locale.getLanguage()}, null);
		return i18nEntries;
	}

	@Override
	public void deleteAll() throws Exception {
		this.DAO.deleteAll(JdoI18nEntry.class);
	}

	@Override
	public <T extends I18nBehaviour> void selectiveDelete(T obj,
			List<String> fieldNames, Locale locale) throws Exception {
		String filter = "";
		int index = 0;
		String param = "";
		for(String fieldName : fieldNames) {
			param = "fieldKey == '" + fieldName + "'";
			if("".equals(filter)) {
				filter += "(" + param;
			} else {
				filter += " || " + param;
			}
			index++;
		}
		filter += ")";
		filter += " && contentId == pContentId";
		filter += " && lang == pLang";
		Collection<JdoI18nEntry> i18nEntries = this.DAO.findEntities(JdoI18nEntry.class,
				filter,"java.lang.String pContentId, String pLang", new Object[] {
			        ""+obj.getId(), locale.getLanguage()}, null);
		this.DAO.delete(i18nEntries);	
	}
	
	@Override
	public long countTotalTranslations() {
		return this.DAO.count(JdoI18nEntry.class);
	}

	@Override
	public long countTranslationsByLocale(Locale locale) {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("lang", locale.getLanguage());
		return this.DAO.count(JdoI18nEntry.class, filter);
	}
	
	
	/***************************************************************************
	 * 
	 *							PRIVATE METHODS 
	 *
	 **************************************************************************/
	
	private Map<String, List<JdoI18nEntry>> createIndexedLangMap(Collection<JdoI18nEntry> entries) {
		Map<String, List<JdoI18nEntry>> translations = 
			new HashMap<String, List<JdoI18nEntry>>();
		for(JdoI18nEntry entry : entries) {
			if(!translations.containsKey(entry.getLang())) {
				translations.put(entry.getLang(), new ArrayList<JdoI18nEntry>());
			} 
			translations.get(entry.getLang()).add(entry);
		}
		return translations;
	}

	

	
	
}