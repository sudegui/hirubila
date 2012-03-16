package com.m4f.utils.feeds.parser.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Blob;
import java.util.Set;
import java.util.HashSet;

@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class Feed extends ParseBase {
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	@Persistent
	private Blob content;
	
	 @Persistent
	 private Set<Long> loadEvents = new HashSet<Long>();
	 
	public Key getKey() {
		return this.key;
	}
	
    public void setKey(String identifier) {
    	Key key = KeyFactory.createKey(this.getClass().getSimpleName(), identifier);
        this.key = key;
    }
	
    public byte[] getContent() {
    	return this.content.getBytes();
    }
    
    public void setContent(byte[] c) {
    	this.content = new Blob(c);
    }
    
    public void addLoadEvent(LoadEvent loadEvent) {
    	this.loadEvents.add(loadEvent.getId());
    }
    
    public void removeLoadEvent(LoadEvent loadEvent) {
    	this.loadEvents.remove(loadEvent.getId());
    }
    
    public Set<Long> getLoadEvents() {
    	return this.loadEvents;
    }
    

}