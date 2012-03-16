package com.m4f.utils.feeds.parser.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.util.Date;
import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class LoadEvent extends ParseBase implements Comparable {
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;
	
	@Persistent
	private Blob fullContent;
	
	@Persistent
	private Blob diffContent;
	
	@Persistent
	private Date launchDate;
	
	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
        this.id = id;
    }
	
	public byte[] getFullContent() {
		return this.fullContent.getBytes();
	}
	    
	public void setFullContent(byte[] c) {
		this.fullContent = new Blob(c);
	}
	
	public byte[] getDiffContent() {
		return this.diffContent.getBytes();
	}
	    
	public void setDiffContent(byte[] c) {
		this.diffContent = new Blob(c);
	}
	    
	public Date getLaunchDate() {
        return this.launchDate;
    }
	
    public void setLaunchDate(Date launchDate) {
        this.launchDate = launchDate;
    }
    
    public int compareTo(Object o) {
    	return this.launchDate.compareTo(((LoadEvent)o).getLaunchDate());
    }
    
}