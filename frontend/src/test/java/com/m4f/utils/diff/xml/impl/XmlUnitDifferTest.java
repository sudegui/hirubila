package com.m4f.utils.diff.xml.impl;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.xml.sax.SAXException;

public class XmlUnitDifferTest {
	
	private XmlUnitDiffer INSTANCE; 
	private String a, b, c;
	
	@Before
	public void setUp() {
		this.INSTANCE = new XmlUnitDiffer();
		this.a = "<?xml version='1.0' encoding='ISO-8859-1'?>" +
		"<zentroak>" +
			"<zentroa><unique_id>160</unique_id><izena>Abarka Dantza Taldea</izena><telefonoa>610404912</telefonoa><faxa></faxa><postae></postae><web_orria>http://www.dantzan.com</web_orria><helbidea>Herrilagunak z/g</helbidea><gaiak></gaiak><herria>Bergara</herria><feed>http://www.bizikasi.com/feed/ikasgida/bergara_zentroa.php?id=160</feed><moderatua>0</moderatua></zentroa>" +
			"<zentroa><unique_id>59</unique_id><izena>Abaroa</izena><telefonoa>943798157</telefonoa><faxa></faxa><postae>jubypenabaroa@gmail.com</postae><web_orria>http://</web_orria><helbidea>Garibai 1 </helbidea><gaiak></gaiak><herria>Arrasate</herria><feed>http://www.bizikasi.com/feed/ikasgida/bergara_zentroa.php?id=59</feed><moderatua>0</moderatua></zentroa>" +
		"</zentroak>";
		this.b = "<?xml version='1.0' encoding='ISO-8859-1'?>" +
		"<zentroak>" +
		"<zentroa><unique_id>160</unique_id><izena>Abarka Dantza Taldea</izena><telefonoa>610404912</telefonoa><faxa></faxa><postae></postae><web_orria>http://www.dantzan.com</web_orria><helbidea>Herrilagunak z/g</helbidea><gaiak></gaiak><herria>Bergara</herria><feed>http://www.bizikasi.com/feed/ikasgida/bergara_zentroa.php?id=160</feed><moderatua>0</moderatua></zentroa>" +
			"<zentroa><unique_id>59</unique_id><izena>Abaroa</izena><telefonoa>943798157</telefonoa><faxa></faxa><postae>jubypenabaroa@gmail.com</postae><web_orria>http://</web_orria><helbidea>Garibai 1 </helbidea><gaiak></gaiak><herria>Arrasate</herria><feed>http://www.bizikasi.com/feed/ikasgida/bergara_zentroa.php?id=59</feed><moderatua>0</moderatua></zentroa>" +	
		"</zentroak>";
		this.c = "<?xml version='1.0' encoding='ISO-8859-1'?>" +
		"<zentroak>" +
			"<zentroa><unique_id>160</unique_id><izena>Abarka Dantza Taldea</izena><telefonoa>610404912</telefonoa><faxa></faxa><postae></postae><web_orria>http://www.dantzan.com</web_orria><helbidea>Herrilagunak z/g</helbidea><gaiak></gaiak><herria>Bergara</herria><feed>http://www.bizikasi.com/feed/ikasgida/bergara_zentroa.php?id=160</feed><moderatua>0</moderatua></zentroa>" +
			"<zentroa><unique_id>59</unique_id><izena>Abaroa</izena><telefonoa>943798157</telefonoa><faxa></faxa><postae>jubypenabaroa@gmail.com</postae><web_orria>http://</web_orria><helbidea>Garibai 1 </helbidea><gaiak></gaiak><herria>Arrasate</herria><feed>http://www.bizikasi.com/feed/ikasgida/bergara_zentroa.php?id=59</feed><moderatua>0</moderatua></zentroa>" +	
			"<zentroa><unique_id>128</unique_id><izena>Abaroa Jubilatu elkartea</izena><telefonoa>943796611</telefonoa><faxa></faxa><postae>jubypenabaroa@gmail.com</postae><web_orria>http://</web_orria><helbidea>Garibai 1</helbidea><gaiak></gaiak><herria>Arrasate</herria><feed>http://www.bizikasi.com/feed/ikasgida/bergara_zentroa.php?id=128</feed><moderatua>0</moderatua></zentroa>" +
		"</zentroak>";
	}
	
	@Test
	public void equals() throws SAXException, IOException {
		Assert.assertTrue(this.INSTANCE.equals(this.a, this.b));
		Assert.assertFalse(this.INSTANCE.equals(this.a, this.c));
	}
	
	
	public void diff() throws SAXException, IOException {
		this.INSTANCE.diff(a, c);
		//System.out.println();
	}
	
}