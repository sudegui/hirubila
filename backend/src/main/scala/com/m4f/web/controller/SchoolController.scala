package com.m4f.web.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMapping,ResponseStatus,RequestParam}
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RequestMethod._
import com.m4f.business.domain._
import scala.collection.JavaConversions._
import scala.collection.mutable.Buffer

@Controller
@RequestMapping(Array("/school"))
class SchoolController extends BaseController {
	
  @RequestMapping(value=Array("/feed"), method=Array(GET,POST))
  @ResponseStatus(HttpStatus.OK)
  def loadFeed(@RequestParam(value="id", required=true) schoolId:Long) {
	  var school:School = schoolService.getSchool(schoolId, null)
	  var parsedCourses: scala.collection.mutable.Map[String, java.util.List[Course]]  = coursesParser.getCourses(school)
	  parsedCourses.foreach{case (key, value) => storeCourses(new java.util.Locale(key),school,value)}
  }
  
  def storeCourses(locale:java.util.Locale, school:School, courses: Buffer[Course]) {
    var provider: Provider = providerService.getProviderById(school.getProvider(), locale)
	courses.foreach((course:Course) => dumperManager.dumpCourse(null, course, locale, school, provider))				
  }
  
}