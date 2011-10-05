package com.m4f.utils.diff.xml.impl;

import java.io.IOException;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.DifferenceConstants;
import org.custommonkey.xmlunit.XMLUnit;
import org.xml.sax.SAXException;
import com.m4f.utils.diff.xml.ifc.Differ;
import java.util.List;

public class XmlUnitDiffer implements Differ {
	
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
	
	
	
}