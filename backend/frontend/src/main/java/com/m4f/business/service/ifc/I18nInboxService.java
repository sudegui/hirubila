package com.m4f.business.service.ifc;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import com.m4f.business.domain.Inbox;
import com.m4f.business.domain.ResultSearchEmail;
import com.m4f.business.domain.Inbox.ORIGIN;
import com.m4f.business.domain.Inbox.TYPE;
import com.m4f.business.domain.Inbox.USER;

public interface I18nInboxService {
	Inbox createInbox();
	List<Inbox> getAllInbox(Locale locale) throws Exception;
	Long count(Inbox.ORIGIN origin) throws Exception;
	Long count(Boolean active, Boolean readed, TYPE type, ORIGIN origin, USER user) throws Exception;
	Collection<Inbox> getAllInbox(Boolean active, Boolean readed, TYPE type, ORIGIN origin,  USER user,
			Locale locale, int init, int end, String ordering) throws Exception;  
	void save(Inbox inbox, Locale locale) throws Exception;
	void delete(Inbox inbox, Locale locale) throws Exception;
	Inbox getInbox(Long id, Locale locale) throws Exception;
	
	ResultSearchEmail createResultSearchEmail();
	void save(ResultSearchEmail resultSearchEmail, Locale locale) throws Exception;
	ResultSearchEmail getResultSearchEmail(Long id, Locale locale) throws Exception;
}
