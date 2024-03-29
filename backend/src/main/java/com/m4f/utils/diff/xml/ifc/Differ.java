package com.m4f.utils.diff.xml.ifc;

import java.io.IOException;

import org.jdom.JDOMException;
import org.xml.sax.SAXException;

public interface Differ {
	
	byte[] diff(byte[] xmlA, byte[] xmlB) throws SAXException, IOException, JDOMException;
	byte[] merge(byte[] xmlA, byte[] xmlB) throws SAXException, IOException, JDOMException;
	
}