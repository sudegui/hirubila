package com.m4f.web.controller;

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMapping,ResponseStatus,RequestParam}
import org.springframework.web.servlet.ModelAndView
import org.springframework.beans.factory.annotation.Autowired
import com.m4f.business.service.ifc.I18nProviderService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RequestMethod._
import java.util.logging.Logger
import com.m4f.utils.feeds.parser.ifc.ISchoolsParser

@Controller
@RequestMapping(Array("/provider"))
class ProviderController {
 
  var LOGGER: Logger = Logger.getLogger(getClass().getName());
  @Autowired
  private var providerService: I18nProviderService = _;
  @Autowired
  private var schoolsParser: ISchoolsParser = _;
  	
  @RequestMapping {val method = Array(GET)}
  @ResponseStatus(HttpStatus.OK)
  def base{LOGGER.severe("hola")}
  
  @RequestMapping(Array("/feed")) {val method = Array(GET,POST)}
  @ResponseStatus(HttpStatus.OK)
  def loadFeed(@RequestParam("providerId") providerId:Long) {
    LOGGER.info("hola" + providerId + " " + this.providerService)
    val provider = this.providerService.getProviderById(providerId, null);
    val schools =  schoolsParser.getSchools(provider);
    
      
  }
    
}