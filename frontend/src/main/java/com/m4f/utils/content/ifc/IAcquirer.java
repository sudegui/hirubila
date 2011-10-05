package com.m4f.utils.content.ifc;

import java.net.URI;

public interface IAcquirer {
	
	byte[] getContent(URI source) throws Exception;
	
}