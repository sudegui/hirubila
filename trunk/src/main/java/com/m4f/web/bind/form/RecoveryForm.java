package com.m4f.web.bind.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class RecoveryForm {
	@NotNull(message="{validation.error.null}")
	@Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message="{validation.error.regexp.field.mail}")
	private String email;
	
	public RecoveryForm() {
		super();
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
