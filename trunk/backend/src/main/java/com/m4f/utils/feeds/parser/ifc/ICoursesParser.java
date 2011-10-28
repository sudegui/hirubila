package com.m4f.utils.feeds.parser.ifc;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import com.m4f.business.domain.Course;
import com.m4f.business.domain.School;
import com.m4f.utils.feeds.events.model.Dump;

public interface ICoursesParser {
	Map<String, List<Course>> getCourses(School school) throws ParserConfigurationException, SAXException, IOException;
}
