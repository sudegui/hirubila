package com.m4f.business.service.impl;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;
import com.m4f.business.service.ifc.URLShortenerService;
import com.m4f.utils.cache.annotations.Cacheable;
import com.m4f.utils.link.ifc.URLShortener;

public class CachedURLShortenerService implements URLShortenerService {
	
	private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class.getName());

	private URLShortener urlShortener;

	public CachedURLShortenerService(URLShortener uShortener) {
		this.urlShortener = uShortener;
	}
	
	@Cacheable(cacheName="urlshort")
	public String shortURL(String url) throws NoSuchAlgorithmException, IOException {
		return this.urlShortener.shortURL(url);
	}
	
	
}