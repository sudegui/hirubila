package com.m4f.utils.link.ifc;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public interface URLShortener {
	
	String shortURL(String url) throws NoSuchAlgorithmException, IOException;
	
}