package com.m4f.utils.feeds.parser.aspect;

import java.net.URI;

import com.m4f.utils.StackTraceUtil;
import com.m4f.utils.dao.ifc.DAOSupport;
import com.m4f.utils.diff.xml.ifc.Differ;
import com.m4f.utils.feeds.parser.model.Feed;
import com.m4f.utils.feeds.parser.model.LoadEvent;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.AfterReturning;
import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;

@Aspect
public class FeedContentOptimizer {
	
	@Autowired
	private Differ differ;
	@Autowired
	private DAOSupport dao;
	private static final Logger LOGGER = Logger.getLogger(FeedContentOptimizer.class.getName());
	
	public FeedContentOptimizer() {}
	
	public FeedContentOptimizer(DAOSupport DAO, Differ differ) {
		this.differ = differ;
		this.dao = DAO;
	}
	
	/*@AfterReturning(
	pointcut = "execution(* com.m4f.utils.content.ifc.ContentAcquirer.getContent(..)) && args(source)",
	argNames="source,retVal", returning= "retVal")
	public void optimize(URI source, ByteArrayOutputStream retVal) {
		try {	
			Feed persistContent = this.getStoreContent(source);
			LoadEvent loadEvent = this.dao.createInstance(LoadEvent.class);
			loadEvent.setFullContent(retVal.toByteArray());
			loadEvent.setLaunchDate(Calendar.getInstance().getTime());
			if(persistContent != null) {
				byte[] diffContent = this.differ.diff(retVal.toByteArray(), persistContent.getContent());
				retVal.reset();
				retVal.write(diffContent);
				loadEvent.setDiffContent(diffContent);
				byte[] mergedContent = this.differ.merge(persistContent.getContent(), diffContent);
				persistContent.setContent(mergedContent);
			} else {
				persistContent = this.dao.createInstance(Feed.class);
				persistContent.setKey(source.toASCIIString());
				loadEvent.setDiffContent(loadEvent.getFullContent());
				persistContent.setContent(retVal.toByteArray());
			}
			this.dao.saveOrUpdate(loadEvent);
			persistContent.addLoadEvent(loadEvent);
			this.dao.saveOrUpdate(persistContent);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
		}
		
	}*/
	
	
	private Feed getStoreContent(URI source) {	
		Feed feed = null;
		try {
			feed = this.dao.findByKey(Feed.class, source.toASCIIString());
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
		}
		return feed;
	}
}