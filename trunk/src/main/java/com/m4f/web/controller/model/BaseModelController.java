package com.m4f.web.controller.model;

import java.util.Locale;

import com.m4f.web.controller.BaseController;

public class BaseModelController extends BaseController {
	
	private enum REQUEST_PROTOCOL {HTTP("http://"), HTTPS("https://");
		private final String protocol;
		private REQUEST_PROTOCOL(String p) {
			this.protocol = p;
		}
		public String getProtocol() {
			return this.protocol;
		}
	};
	
	protected final String REFERER_PARAM = "sourceReferer";
	
	protected String buildReturnURL(String host, String referer, Locale locale) {
		if(referer == null || "".equals(referer)) {
			return "/" + locale.getLanguage() + "/";
		}
		String partToDel = "";
		if(referer.startsWith(REQUEST_PROTOCOL.HTTP.getProtocol())) {
			partToDel += REQUEST_PROTOCOL.HTTP.getProtocol() + host;
		} else if(referer.startsWith(REQUEST_PROTOCOL.HTTPS.getProtocol())){
			partToDel += REQUEST_PROTOCOL.HTTPS.getProtocol() + host;
		}
		return referer.replace(partToDel, "");
	}
	
	
}