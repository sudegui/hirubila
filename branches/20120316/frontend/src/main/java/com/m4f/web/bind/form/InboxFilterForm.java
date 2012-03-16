package com.m4f.web.bind.form;

import com.m4f.business.domain.Inbox;


public class InboxFilterForm {
	
	private Inbox.TYPE type;

	private Inbox.ORIGIN origin;
	
	private Inbox.USER user;
	
	private Boolean readed;

	public Inbox.TYPE getType() {
		return type;
	}

	public void setType(Inbox.TYPE type) {
		this.type = type;
	}

	public Inbox.ORIGIN getOrigin() {
		return origin;
	}

	public void setOrigin(Inbox.ORIGIN origin) {
		this.origin = origin;
	}

	public Inbox.USER getUser() {
		return user;
	}

	public void setUser(Inbox.USER user) {
		this.user = user;
	}

	public Boolean getReaded() {
		return readed;
	}

	public void setReaded(Boolean readed) {
		this.readed = readed;
	}
}
