package com.m4f.web.controller;

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMapping,ResponseStatus,RequestParam}
import com.m4f.business.service.ifc.I18nProviderService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RequestMethod._
import java.util.logging.Logger
import com.m4f.utils.feeds.parser.ifc.ISchoolsParser
import com.m4f.business.domain._
import java.util.List
import java.util.Locale
import com.m4f.utils.feeds.events.model.Dump
import scala.collection.JavaConversions._
import scala.collection.mutable.Buffer
import com.m4f.business.service.ifc._
import com.m4f.utils.worker.WorkerFactory


@Controller
@RequestMapping(Array("/provider"))
class ProviderController extends BaseController {
 
  var LOGGER: Logger = Logger.getLogger(getClass().getName())
   
  @RequestMapping(method = Array(GET))
  @ResponseStatus(HttpStatus.OK)
  def base{LOGGER.severe("hola " + this.providerService.getAllProviderIds())}
  
  @RequestMapping(value=Array("/loadschools"), method=Array(GET,POST)) 
  @ResponseStatus(HttpStatus.OK)
  def loadSchools(@RequestParam(value="id",required=true) providerId:Long) { 
	val provider = this.providerService.getProviderById(providerId, null)
    val schools:Buffer[School] =  asBuffer(schoolsParser.getSchools(provider))
    var locales:Buffer[Locale] = asBuffer(configurationService.getLocales())
    locales.foreach((locale: Locale) => storeSchools(provider, schools, locale))
  }
  
 
  @RequestMapping(value=Array("/loadcourses"), method=Array(GET,POST))
  def loadCourses(@RequestParam(value="id",required=true) providerId:Long) {
	var storedSchools:Buffer[School] = asBuffer(schoolService.getSchoolsByProvider(providerId, null, null))
    //storedSchools.foreach((school:School) => loadCourses(school))
  }
  
  
  def storeSchools(provider:Provider, schools:Buffer[School], locale:Locale) {
    schoolStorage.store(schools, locale, provider)
  }
  
  /*
  def loadCourses(school:School) {
	  var params: java.util.Map[String, String] = new java.util.HashMap[String, String]()
	  if((school.getFeed()!=null) && (!"".equals(school.getFeed()))) {
		var parsedCourses: scala.collection.mutable.Map[String, java.util.List[Course]]  = coursesParser.getCourses(school)
	    parsedCourses.foreach{case (key, value) => storeCourses(new java.util.Locale(key),school,value)}
	  }
  }
  
  def storeCourses(locale:java.util.Locale, school:School, courses: Buffer[Course]) {
    var provider: Provider = providerService.getProviderById(school.getProvider(), locale)
	courses.foreach((course:Course) => dumperManager.dumpCourse(null, course, locale, school, provider))				
  }
    */
}