package com.m4f.utils.diff.xml.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.DifferenceConstants;
import org.custommonkey.xmlunit.XMLUnit;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.SAXException;
import com.m4f.utils.diff.xml.ifc.Differ;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import org.jdom.output.*;
import java.io.ByteArrayOutputStream;

public class XmlUnitDiffer implements Differ {
	
	/*
	private class Group extends HashSet {
		
		public Group(List<Element> c) {
			for(Element el : c) {
				this.add(new ExtendElement(el));
			}
		}
		
		public Set<Element> getElements() {
			Set<Element> elements = new HashSet<Element>();
			for (Iterator<ExtendElement> it = this.iterator(); it.hasNext(); ) {
					elements.add(((ExtendElement)it.next()).node);
			}
			return elements;
		}
		
		@Override
		public boolean contains(Object o) {
			for (Iterator<ExtendElement> it = this.iterator(); it.hasNext(); ) {
				if(((ExtendElement)it.next()).equals(o)){
					return true;
				}
			}
			return true;
		}
		
		private class ExtendElement {
			
			public Element node;
			
			public ExtendElement(Element n) {
				this.node = n;
			}
			
			@Override
			public boolean equals(java.lang.Object ob) {
				return false;
			}
		}
		
	}*/
	
	
	
	public XmlUnitDiffer() {
		//XMLUnit.setControlParser("org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
		//XMLUnit.setTestParser("org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
		//XMLUnit.setSAXParserFactory("org.apache.xerces.jaxp.SAXParserFactoryImpl");
		//XMLUnit.setTransformerFactory("org.apache.xalan.processor.TransformerFactoryImpl");
		XMLUnit.setIgnoreComments(true);
		XMLUnit.setIgnoreWhitespace(true);
		XMLUnit.setIgnoreDiffBetweenTextAndCDATA(true);
		XMLUnit.setIgnoreAttributeOrder(true);
	}

	
	public String diff(String a, String b) throws SAXException, IOException {
		Diff diff = new Diff(a,b);
		if(diff.identical()) {
			return "";
		}
		DetailedDiff detailedDiff = new DetailedDiff(diff);
		for(Difference difference : (List<Difference>)detailedDiff.getAllDifferences()) {
			System.out.println("(" + difference.getId() + ") Description: " +difference.getDescription());
			System.out.println("ControlNodeDetail: " + difference.getControlNodeDetail().getValue());
			System.out.println("TestNodeDetail: " + difference.getTestNodeDetail().getValue());
		}
		return detailedDiff.toString();
	}

	@Override
	public boolean equals(String a, String b) throws SAXException, IOException {
		Diff diff = new Diff(a,b);
		return diff.identical();
	}
	
	@Override
	public byte[] diff(byte[] xmlA, byte[] xmlB) throws SAXException, IOException, JDOMException {
		SAXBuilder builder = new SAXBuilder();
		builder.setIgnoringElementContentWhitespace(true);
		Document docA = builder.build(new ByteArrayInputStream(xmlA));
		Document docB = builder.build(new ByteArrayInputStream(xmlB));
		
		List<Element> children = (List<Element>)docA.getRootElement().getChildren();
		Set<Element> groupA = new HashSet<Element>(children);
		
		children = (List<Element>)docB.getRootElement().getChildren();
		Set<Element> groupB = new HashSet<Element>(children);
		
		//Solo quedaran los nuevos
		groupA.removeAll(groupB);
		
		return this.buildFinalXML(docA, groupA);
	}
	
	
	private byte[] buildFinalXML(Document original, Set<Element> elements) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		Element root = (Element)original.getRootElement().clone();
		root.removeContent();
		Document doc = new Document((Element)root.detach(), 
				original.getDocType(), original.getBaseURI());
		XMLOutputter xop = new XMLOutputter();
		xop.output(doc, bos);
		return bos.toByteArray();
	}
	
	
}