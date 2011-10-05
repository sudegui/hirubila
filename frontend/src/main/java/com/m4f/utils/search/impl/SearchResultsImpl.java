package com.m4f.utils.search.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.m4f.utils.search.ifc.ISearchResult;
import com.m4f.utils.search.ifc.ISearchResults;


public class SearchResultsImpl implements ISearchResults {
	private HashMap<METADATA, String> mMetaMap;
	private ArrayList<ISearchResult> mResults;
	
	public SearchResultsImpl() {
		mMetaMap = new HashMap<METADATA, String>();
		mResults = new ArrayList<ISearchResult>();
		mMetaMap.put(METADATA.TOTAL_RESULTS, "0");
		mMetaMap.put(METADATA.TOTAL_TIME, "0");
	}

	@Override
	public String getMetadata(METADATA meta) {
		return mMetaMap.get(meta) != null ? mMetaMap.get(meta) : "";
	}

	
	@Override
	public Iterator<METADATA> metaDataIterator() {
		return mMetaMap.keySet().iterator();
	}

	@Override
	public void addMetadata(METADATA meta, String value) {
		mMetaMap.put(meta, value);
	}

	@Override
	public boolean add(ISearchResult result) {
		return mResults.add(result);
	}

	@Override
	public boolean addAll(Collection<? extends ISearchResult> results) {
		return mResults.addAll(results);
	}

	@Override
	public void clear() {
		mResults.clear();
	}

	@Override
	public boolean contains(Object result) {
		return mResults.contains(result);
	}

	@Override
	public boolean containsAll(Collection<?> results) {
		return mResults.containsAll(results);
	}

	@Override
	public boolean isEmpty() {
		return mResults.isEmpty();
	}

	@Override
	public Iterator<ISearchResult> iterator() {
		return mResults.iterator();
	}

	@Override
	public boolean remove(Object result) {
		return mResults.remove(result);
	}

	@Override
	public boolean removeAll(Collection<?> results) {
		return mResults.remove(results);
	}

	@Override
	public boolean retainAll(Collection<?> results) {
		return mResults.retainAll(results);
	}

	@Override
	public int size() {
		return mResults.size();
	}

	@Override
	public Object[] toArray() {
		return mResults.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return mResults.toArray(a);
	}

	@Override
	public Map<METADATA, String> getMetadata() {
		return mMetaMap;
	}

	@Override
	public String getClient() {
		return this.getMetadata(METADATA.CLIENT);
	}

	@Override
	public String getIp() {
		return this.getMetadata(METADATA.IP);
	}

	@Override
	public String getOutFormat() {
		return this.getMetadata(METADATA.OUT_FORMAT);
	}

	@Override
	public String getQuery() {
		return this.getMetadata(METADATA.QUERY);
	}

	@Override
	public String getSort() {
		return this.getMetadata(METADATA.SORT);
	}

	@Override
	public String getTotalResults() {
		return this.getMetadata(METADATA.TOTAL_RESULTS);
	}

	@Override
	public String getTotalTime() {
		return this.getMetadata(METADATA.TOTAL_TIME);
	}
}
