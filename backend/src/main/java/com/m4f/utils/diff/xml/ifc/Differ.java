package com.m4f.utils.diff.xml.ifc;

import java.io.IOException;

import org.jdom.JDOMException;
import org.xml.sax.SAXException;

public interface Differ {
	
	boolean equals(String a, String b) throws SAXException, IOException;
	byte[] diff(byte[] xmlA, byte[] xmlB) throws SAXException, IOException, JDOMException;
	
}