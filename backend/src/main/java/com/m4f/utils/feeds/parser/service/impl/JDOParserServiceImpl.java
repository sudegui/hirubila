package com.m4f.utils.feeds.parser.service.impl;

import java.net.URI;
import com.m4f.utils.dao.ifc.DAOSupport;
import com.m4f.utils.feeds.parser.model.Feed;
import com.m4f.utils.feeds.parser.model.LoadEvent;
import com.m4f.utils.feeds.parser.service.ifc.IParserService;
import com.m4f.utils.feeds.events.service.impl.JdoBaseService;
import java.util.Set;
import java.util.TreeSet;

public class JDOParserServiceImpl extends JdoBaseService implements IParserService {
	
	
	public JDOParserServiceImpl(DAOSupport dao) {
		super(dao);
	}
	
	@Override
	public Feed getFeed(URI source) {
		try {
			return this.DAO.findByKey(Feed.class, source.toASCIIString());
		} catch(Exception e) {
			return null;
		}
	}

	@Override
	public Set<LoadEvent> getLoadEvents(Feed feed) throws Exception {
		Set<LoadEvent> loadEvents = new TreeSet<LoadEvent>();
		if(feed.getLoadEvents()!=null)
			loadEvents.addAll(this.DAO.findCollectionById(LoadEvent.class, feed.getLoadEvents()));
		return loadEvents;
	}

}
