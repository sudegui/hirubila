package com.m4f.utils.feeds.parser.service.ifc;

import java.net.URI;
import com.m4f.utils.feeds.parser.model.ParseBase;
import com.m4f.utils.feeds.parser.model.LoadEvent;
import com.m4f.utils.feeds.parser.model.Feed;
import java.util.Set;

public interface IParserService {
	
	Feed getFeed(URI source);
	Set<LoadEvent> getLoadEvents(Feed feed) throws Exception;
	
}