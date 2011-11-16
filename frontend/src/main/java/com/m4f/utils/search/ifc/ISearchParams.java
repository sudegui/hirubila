package com.m4f.utils.search.ifc;


public interface ISearchParams extends Iterable<ISearchParams.PARAM> {
	enum PARAM {QUERY, COLLECTION, LANG, START, INMETA, BASE_COLLECTION_NAME, SEARCH_URI, CLIENT}; // TODO ADD MORE PARAMS
	
	public void addParam(PARAM param, String value);
	
	public String getParam(PARAM param);
}
