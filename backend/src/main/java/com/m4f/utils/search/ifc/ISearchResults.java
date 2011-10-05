package com.m4f.utils.search.ifc;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public interface ISearchResults extends Collection<ISearchResult> {
	public enum METADATA  {QUERY, CLIENT, IP, SORT, OUT_FORMAT, TOTAL_RESULTS, TOTAL_TIME }
	public String getQuery();
	public String getClient();
	public String getIp();
	public String getSort();
	public String getOutFormat();
	public String getTotalResults();
	public String getTotalTime();
	public String getMetadata(METADATA meta);
	public void addMetadata(METADATA meta, String value);
	public Iterator<METADATA> metaDataIterator();
	public Map<METADATA, String> getMetadata(); 
}
