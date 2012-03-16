package com.m4f.utils.search.aop;

import java.net.URLDecoder;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.m4f.business.domain.PhraseSearch;
import com.m4f.business.service.ifc.IPhraseSearchService;
import com.m4f.utils.StackTraceUtil;
import com.m4f.utils.search.ifc.ISearchParams;

public class SearchInterceptor {
	private static final Logger LOGGER = Logger.getLogger(SearchInterceptor.class.getName());
	
	private	IPhraseSearchService searchService;
	
	public SearchInterceptor(IPhraseSearchService searchService) {
		this.searchService = searchService;
	}
	
	public void saveSearchedPhrase(ISearchParams params) {
		// First, we check if start is zero. It means that it�s not iterating over the results through pagination
		String start = params.getParam(ISearchParams.PARAM.START);
		if("0".equals(start)) {
			try {
			    PhraseSearch phrase = new PhraseSearch();
				phrase.setLanguage(params.getParam(ISearchParams.PARAM.LANG));
				phrase.setPhrase(URLDecoder.decode(params.getParam(ISearchParams.PARAM.QUERY), "UTF-8")); // URL DECODING
				phrase.setDate(new Date());
				this.searchService.savePhraseSearch(phrase);
			} catch (Exception e) {			
				LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
			}
		}
	}
}
