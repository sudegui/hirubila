package com.m4f.utils.feeds.parser.ifc;

import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.School;
import com.m4f.utils.feeds.events.model.Dump;

public interface ISchoolsParser {
	
	List<School> getSchools(Dump dump, Provider provider) throws ParserConfigurationException, SAXException, IOException, Exception;
}
