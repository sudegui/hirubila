package com.m4f.utils.search.impl;

import java.util.HashMap;
import java.util.Iterator;

import com.m4f.utils.search.ifc.ISearchParams;

public class SearchParamsImpl implements ISearchParams {
	private HashMap<PARAM, String> mParams;
	
	public SearchParamsImpl() {
		super();
		mParams = new HashMap<PARAM, String>();
	}
	
	@Override
	public void addParam(PARAM param, String value) {
		mParams.put(param, value);
	}

	@Override
	public String getParam(PARAM param) {
		return mParams.get(param) != null ? mParams.get(param) : "";
	}

	@Override
	public Iterator<PARAM> iterator() {
		return mParams.keySet().iterator();
	}
}
