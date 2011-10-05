package com.m4f.business.domain;

import com.m4f.utils.i18n.model.ifc.I18nBehaviour;
import java.io.Serializable;

public abstract class BaseEntity implements Serializable, I18nBehaviour {
	
	private boolean translated = false;
	
	@Override
	public boolean isTranslated() {
		return this.translated;
	}
	
	@Override
	public void setTranslated(boolean t) {
		this.translated = t;
	}
	
}