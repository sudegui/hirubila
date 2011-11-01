package com.m4f.utils.feeds.parser.aspect;

import java.net.URI;

import com.google.appengine.api.datastore.Text;
import com.m4f.utils.StackTraceUtil;
import com.m4f.utils.dao.ifc.DAOSupport;
import com.m4f.utils.diff.xml.ifc.Differ;
import com.m4f.utils.feeds.parser.model.FeedContent;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.AfterReturning;


@Aspect
public class FeedContentOptimizer {
	
	private String encoding = "UTF8";
	private Differ differ;
	private DAOSupport DAO;
	
	public FeedContentOptimizer(DAOSupport DAO, Differ differ) {
		this.differ = differ;
		this.DAO = DAO;
	}
	
	@AfterReturning(
	pointcut = "execution(* com.m4f.utils.content.ifc.ContentAcquirer.*(..))",
	returning= "retVal")
	public void optimize(URI source, byte[] retVal) {
		try {
			String newContent = new String(retVal, this.encoding);
			FeedContent persistContent = this.getStoreContent(source);
			if(persistContent != null) {
				String oldContent = persistContent.getContent().getValue();
				if(this.differ.equals(newContent, oldContent)) {
					retVal = this.createEmptyFeed();
				} else {
					retVal = this.diff(newContent, oldContent);
				}
			} else {
				persistContent = this.DAO.createInstance(FeedContent.class);
				persistContent.setKey(source.toASCIIString());
				Text content = new Text(newContent);
				persistContent.setContent(content);
				this.DAO.saveOrUpdate(persistContent);
			}
		} catch(Exception e) {
			StackTraceUtil.getStackTrace(e);
		}
	}
	
	private byte[] createEmptyFeed() {
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><root/>";
		return xml.getBytes();
	}
	
	private byte[] diff(String newContent, String storeContent) {
		String diff  = "";
		return diff.getBytes();
	}
	
	private FeedContent getStoreContent(URI source) {
		FeedContent cached = null;
		try {
			cached = this.DAO.findByKey(FeedContent.class, source.toASCIIString());
		} catch(Exception e) {
			
		}
		return cached;
	}
}