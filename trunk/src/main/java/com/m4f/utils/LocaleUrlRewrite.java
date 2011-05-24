package com.m4f.utils;

import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.i18n.LocaleContextHolder;
import com.m4f.utils.StackTraceUtil;

public class LocaleUrlRewrite {
	
	private static final Logger LOGGER = Logger.getLogger(LocaleUrlRewrite.class.getName());
	
	public void chooseLocale(HttpServletRequest request, HttpServletResponse response) {
		String locale = LocaleContextHolder.getLocale().getLanguage();
		LOGGER.info("LA LOCALE ES ------>>>> " + locale);
		try {
			if(locale != null && !("").equals(locale)) {
				response.sendRedirect(locale + request.getRequestURI());
			}
		} catch (IOException e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
		}
	}
	
}