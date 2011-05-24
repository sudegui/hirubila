package com.m4f.web.bind.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.m4f.business.domain.Inbox.TYPE;

public class InboxForm {

	private Long id;

	private String name;
	
	@NotNull
	private String content;
	
	private TYPE type;

	@NotNull(message="{validation.error.null}")
	@Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message="{validation.error.regexp.field.mail}")
	private String from;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public TYPE getType() {
		return type;
	}

	public void setType(TYPE type) {
		this.type = type;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}
}
