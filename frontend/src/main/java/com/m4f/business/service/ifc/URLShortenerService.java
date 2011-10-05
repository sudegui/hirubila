package com.m4f.business.service.ifc;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public interface URLShortenerService {
	
	String shortURL(String url) throws NoSuchAlgorithmException, IOException;
	
}