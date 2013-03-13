package com.m4f.utils.content.ifc;

import java.net.URI;
import java.io.ByteArrayOutputStream;

public interface ContentAcquirer {
	
	ByteArrayOutputStream getContent(URI source) throws Exception;
	
}