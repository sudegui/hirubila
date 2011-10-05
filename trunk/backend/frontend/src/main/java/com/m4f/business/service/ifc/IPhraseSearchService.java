package com.m4f.business.service.ifc;

import java.util.List;
import java.util.Locale;

import com.m4f.business.domain.PhraseSearch;

public interface IPhraseSearchService {

	void savePhraseSearch(PhraseSearch phrase) throws Exception;
	List<PhraseSearch> getAllPhrases() throws Exception;
	List<PhraseSearch> findPhrasesByPhrase(String query, Locale locale) throws Exception;
}
