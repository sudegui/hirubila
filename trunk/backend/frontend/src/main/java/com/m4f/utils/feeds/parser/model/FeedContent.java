package com.m4f.utils.feeds.parser.model;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;

@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class FeedContent implements Serializable {
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	@Persistent
	private Text content;
	
	public Key getKey() {
		return this.key;
	}
	
    public void setKey(String identifier) {
    	Key key = KeyFactory.createKey(this.getClass().getSimpleName(), identifier);
        this.key = key;
    }
	
    public Text getContent() {
    	return this.content;
    }
    
    public void setContent(Text c) {
    	this.content = c;
    }

}