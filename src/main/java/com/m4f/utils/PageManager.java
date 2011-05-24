package com.m4f.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class PageManager<T> implements Iterable<T>{
	/**
	 * Actual collection of data.
	 */
	private Collection<T> collection;
	
	/**
	 * Total size.
	 */
	private long size;
	
	/**
	 * Start index.
	 */
	private int start;
	
	/**
	 * Offset.
	 */
	private int offset;
	
	/**
	 * Page range to generate the index.
	 */
	private int pageRange;
	
	/**
	 * Page range to generate the index.
	 */
	private String urlBase;
	
	public PageManager() {
		this.collection = null;
		this.offset = 10; // By default 10 elements in one page. It can be modified using another constructor and configuration property or by setOffset
		this.start = 0;
		this.size = 0;
		this.pageRange = 20; // By default 20 page's index. It can be modified using another constructor and configuration property or by setPageRange
	}
	
	public PageManager(int start) {
		this();
		this.start = start;
	}
	
	
	public PageManager(int start, int size) {
		this(start);
		this.size = size;
	}
	
	public PageManager(int start, int size, int offset, int pageRange) {
		this(start, size);
		this.offset = offset;
		this.pageRange = pageRange;
	}

	public Collection<T> getCollection() {
		return collection;
	}

	public void setCollection(Collection<T> collection) {
		this.collection = collection;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	public int getEnd() {
		return this.start + this.offset;
	}

	
	public int getCurrentPage() {
		return this.start/this.offset < 0 || this.start/this.offset > this.size ? 1 : (this.start/this.offset)+1;
	}
	
	public int getPageRange() {
		return pageRange;
	}

	public void setPageRange(int pageRange) {
		this.pageRange = pageRange;
	}
	
	public int getPagesMax() {
		int maxPages = (int)this.size/this.offset;
		if(this.size%this.offset > 0) {
			maxPages++;
		}
		return maxPages;
	}
	
	public int getPageStart() {
		int current = this.getCurrentPage();
		int pageStart = 1;
		
		if((current - this.pageRange/2) < pageStart) pageStart = 1;
		else pageStart = current - this.pageRange/2;
		
		return pageStart;
	}

	public int getPageEnd() {
		int current = this.getCurrentPage();
		int pageEnd = current + this.pageRange/2;
		
		return pageEnd > this.getPagesMax() ? this.getPagesMax() : pageEnd;
	}

	@Override
	public Iterator<T> iterator() {
		return this.collection != null ? this.collection.iterator() : (new ArrayList<T>()).iterator();
	}
	
	public List<Integer> getPagesIterator() {
		List<Integer> pagesIndex = new ArrayList<Integer>();
		for(int i=this.getPageStart() ; i <= this.getPageEnd() ; i++) {
			pagesIndex.add(i);
		}
		return pagesIndex;
		
	}

	public void setUrlBase(String urlBase) {
		this.urlBase = urlBase;
	}

	public String getUrlBase() {
		return urlBase;
	}
}
