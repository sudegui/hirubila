package com.m4f.utils.diff.xml.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.SAXException;
import com.m4f.utils.diff.xml.ifc.Differ;
import org.jdom.xpath.XPath;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.logging.Logger;
import org.jdom.output.*;
import java.io.ByteArrayOutputStream;

public class JDOMDiffer implements Differ {
	
	private static final Logger LOGGER = Logger.getLogger(JDOMDiffer.class.getName());
	
	private class ExtendElement {
		
		Element node;
		
		public ExtendElement(Element n) {
			this.node = n;
		}
		
		@Override
		public String toString() {
			return extractContent(node);
		}
		
		private String extractContent(Element element) {
			String signature = "";
			for(Element child : (List<Element>)node.getChildren()) {
				if(!"".equals(signature)) {
					signature +=",";
				}
				signature += child.getName().trim() + ":" + child.getText().trim();
			}
			return signature;
		}
		
		
		@Override
		public boolean equals(java.lang.Object ob) {
			return this.toString().equals(ob.toString());
		}	
		
		@Override
		public int hashCode() {
			return 1;
		}
	}

	@Override
	public byte[] merge(byte[] xmlA, byte[] xmlB) throws SAXException, IOException, JDOMException {
		SAXBuilder builder = new SAXBuilder();
		builder.setIgnoringElementContentWhitespace(true);
		builder.setValidation(false);
		Document docA = builder.build(new ByteArrayInputStream(xmlA));
		Document docB = builder.build(new ByteArrayInputStream(xmlB));
		List<Element> childs = docB.getRootElement().getChildren();
		Iterator i = childs.iterator();
		while (i.hasNext()) {
	    	Element clonedChild = (Element)(((Element)((Element)i.next()).clone()).detach());
	    	System.out.println("Unique-ID: " + clonedChild.getChild("unique_id").getText());
	    	XPath xPath = XPath.newInstance("/" + docA.getRootElement().getName() + "/*[unique_id=" + 
	    			clonedChild.getChild("unique_id").getText() + "]/.");
	    	List<Element> list = xPath.selectNodes(docA);
	    	for(Element element : list) {
	    		docA.getRootElement().removeContent(element);
	    	}
	    	docA.getRootElement().addContent(clonedChild);
	    	xPath = XPath.newInstance("/" + docA.getRootElement().getName() + "/*[unique_id=" + 
	    			clonedChild.getChild("unique_id").getText() + "]/.");
	    	list = xPath.selectNodes(docA);
	    }
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		XMLOutputter xop = new XMLOutputter();
		xop.output(docA, bos);
		return bos.toByteArray();
	}
	
	@Override
	public byte[] diff(byte[] xmlA, byte[] xmlB) throws SAXException, IOException, JDOMException {
		SAXBuilder builder = new SAXBuilder();
		builder.setIgnoringElementContentWhitespace(true);
		Document docA = builder.build(new ByteArrayInputStream(xmlA));
		Document docB = builder.build(new ByteArrayInputStream(xmlB));
		
		List<Element> children = (List<Element>)docA.getRootElement().getChildren();
		Set<ExtendElement> groupA = new HashSet<ExtendElement>();
		for(Element child: children) {
			groupA.add(new ExtendElement(child));
		}
		
		children = (List<Element>)docB.getRootElement().getChildren();
		Set<ExtendElement> groupB = new HashSet<ExtendElement>();
		for(Element child: children) {
			groupB.add(new ExtendElement(child));
		}
		//Solo quedaran los nuevos
		LOGGER.info("RemoveAll: " + groupA.removeAll(groupB));
		LOGGER.info("Group A size: " + groupA.size());
		return this.buildFinalXML(docA, groupA);
	}
	
	
	
	
	private byte[] buildFinalXML(Document original, Set<ExtendElement> elements) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		Element root = (Element)original.getRootElement().clone();
		root.removeContent();
		Document doc = new Document((Element)root.detach(), 
				original.getDocType(), original.getBaseURI());
		for(ExtendElement element : elements) {
			root.addContent(element.node.detach());
		}
		XMLOutputter xop = new XMLOutputter();
		xop.output(doc, bos);
		return bos.toByteArray();
	}
	
	
}