package com.m4f.business.domain;

import com.m4f.business.domain.annotation.Imported;
import com.m4f.utils.i18n.model.ifc.I18nBehaviour;
import java.io.Serializable;
import java.lang.reflect.Field;

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
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("[");
		for(Field field: this.getClass().getDeclaredFields()) {
			if(!field.isAccessible()) field.setAccessible(true);
			try {
				if(!field.isAnnotationPresent(Imported.class)) {
					continue;
				}
				if(field.get(this) != null) {
					String value = field.getName() + ":" + field.get(this).toString();
					sb.append(value + ",");
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
}