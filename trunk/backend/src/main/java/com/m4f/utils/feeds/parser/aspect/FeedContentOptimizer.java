package com.m4f.utils.feeds.parser.aspect;

import java.net.URI;
import com.google.appengine.api.datastore.Text;
import com.m4f.utils.StackTraceUtil;
import com.m4f.utils.dao.ifc.DAOSupport;
import com.m4f.utils.diff.xml.ifc.Differ;
import com.m4f.utils.feeds.parser.model.FeedContent;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.AfterReturning;
import org.jdom.JDOMException;
import org.xml.sax.SAXException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Aspect
public class FeedContentOptimizer {
	
	private Differ differ;
	private DAOSupport dao;
	
	public FeedContentOptimizer(DAOSupport DAO, Differ differ) {
		this.differ = differ;
		this.dao = DAO;
	}
	
	@AfterReturning(
	pointcut = "execution(* com.m4f.utils.content.ifc.ContentAcquirer.*(..)) && args(source)",
	argNames="source,retVal", returning= "retVal")
	public void optimize(URI source, ByteArrayOutputStream retVal) {
		try {	
			FeedContent persistContent = this.getStoreContent(source);
			if(persistContent != null) {
				this.buildContent(retVal.toByteArray(), persistContent, retVal);
			} else {
				persistContent = this.dao.createInstance(FeedContent.class);
				persistContent.setKey(source.toASCIIString());
				persistContent.setContent(retVal.toByteArray());
				this.dao.saveOrUpdate(persistContent);
			}
		} catch(Exception e) {
			StackTraceUtil.getStackTrace(e);
		}
	}
	
	private void buildContent(byte[] currentContent, 
			FeedContent storedFeed, ByteArrayOutputStream container) 
					throws SAXException, IOException, JDOMException {
		byte[] content;
		content = this.diff(currentContent, storedFeed);
		container.reset();
		container.write(content);
	}
	
	
	private byte[] diff(byte[] currentContent, FeedContent storedFeed) 
			throws IOException, SAXException, JDOMException {
		return this.differ.diff(currentContent, storedFeed.getContent());
		
	}
	
	private FeedContent getStoreContent(URI source) {	
		try {
			return this.dao.findByKey(FeedContent.class, source.toASCIIString());
		} catch(Exception e) {
			return null;
		}
		
	}
}