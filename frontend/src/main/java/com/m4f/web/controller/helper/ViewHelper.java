package com.m4f.web.controller.helper;

import java.util.logging.Logger;
import com.m4f.business.service.exception.ServiceNotFoundException;
import com.m4f.business.service.exception.ContextNotActiveException;
import com.m4f.utils.StackTraceUtil;

public class ViewHelper {
	
	private static final Logger LOGGER = Logger.getLogger(ViewHelper.class.getName());
	
	public String errorManagement(Exception e) {
		LOGGER.severe(".....Management Error");
		LOGGER.severe(StackTraceUtil.getStackTrace(e));
		if(e.getClass().isInstance(ServiceNotFoundException.class)) {
			return "redirect:/j_spring_security_logout";
		}
		if(e.getClass().isInstance(ContextNotActiveException.class)) {
			return "redirect:/j_spring_security_logout";
		}
		return "common.error";
	}
	
}