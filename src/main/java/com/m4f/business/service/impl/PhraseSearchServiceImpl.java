package com.m4f.business.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import com.m4f.business.domain.PhraseSearch;
import com.m4f.business.service.ifc.IPhraseSearchService;
import com.m4f.utils.i18n.dao.ifc.DAOSupport;

public class PhraseSearchServiceImpl extends DAOBaseService implements IPhraseSearchService {
	
	private static final Logger LOGGER = Logger.getLogger(PhraseSearchServiceImpl.class.getName());
	
	public PhraseSearchServiceImpl(DAOSupport dao) {
		super(dao);
	}
	
	@Override
	public void savePhraseSearch(PhraseSearch phrase) throws Exception {
		this.DAO.saveOrUpdate(phrase);		
	}
	
	@Override
	public List<PhraseSearch> getAllPhrases() throws Exception {
		return this.DAO.findAll(PhraseSearch.class);
	}
	 
	@Override
	public List<PhraseSearch> findPhrasesByPhrase(String query, Locale locale) {
		String hack = query + "\ufffd";
		
		Collection<PhraseSearch> phrases = this.DAO.findEntities(PhraseSearch.class, "phrase >= phraseParam && phrase < hackParam", "String phraseParam, String hackParam", new Object[]{query, hack}, null);
		
		// TODO find another way to not return duplicate phrases
		return this.removeDuplicated(phrases);
	}
	
	private List<PhraseSearch> removeDuplicated(Collection<PhraseSearch> phrases) {
		HashMap<String, PhraseSearch> map = new HashMap<String, PhraseSearch>();
		for(PhraseSearch phrase : phrases) map.put(phrase.getPhrase(), phrase);
		
		return new ArrayList(map.values());
	}
}
