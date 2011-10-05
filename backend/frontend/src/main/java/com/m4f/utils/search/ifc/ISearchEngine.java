package com.m4f.utils.search.ifc;

import java.util.StringTokenizer;

public interface ISearchEngine {
	public ISearchParams getSearchParemeters(); 
	public void search(StringTokenizer terms, ISearchParams parameters) throws Exception;
	public ISearchResults getSearchResults();
}
