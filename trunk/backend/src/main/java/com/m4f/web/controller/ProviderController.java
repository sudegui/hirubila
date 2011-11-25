package com.m4f.web.controller;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.xml.sax.SAXException;
import java.util.Collection;
import java.util.Date;
import com.m4f.business.domain.CronTaskReport;
import com.m4f.business.domain.Provider;
import com.m4f.utils.feeds.importer.ProviderImporter;


@Controller
@RequestMapping("/provider")
public class ProviderController extends BaseController {
	
	@RequestMapping(value = "/ids", method = RequestMethod.GET)
	@ResponseBody
	public Collection<Long> getIds() throws Exception {
		return this.providerService.getAllProviderIds();
	}
	
	@RequestMapping(value = "/feed", method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseStatus(HttpStatus.OK)
	public void loadFeed(@RequestParam Long providerId) 
			throws ParserConfigurationException, SAXException, IOException, Exception {
		CronTaskReport report = cronTaskReportService.create();
		report.setObject_id(providerId);
		report.setDate(new Date());
		report.setType(CronTaskReport.TYPE.PROVIDER_FEED);
		try {
			Provider provider = this.providerService.getProviderById(providerId, null);
			report.setDescription("Importing schools from " + provider.getName() + " provider.");	
			report.setResult("OK");
		} catch(Exception e) {
			report.setResult(new StringBuffer("ERROR: ").append(e.getMessage()).toString());
			throw e;
		} finally {
			cronTaskReportService.save(report);
		}
	}
	
	
	@RequestMapping(value = "/schools", method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseStatus(HttpStatus.OK)
	public void loadSchools(@RequestParam(required=false) Long providerId) 
			throws ParserConfigurationException, SAXException, IOException, Exception {
		CronTaskReport report = cronTaskReportService.create();
		report.setObject_id(providerId);
		report.setDate(new Date());
		report.setType(CronTaskReport.TYPE.PROVIDER_SCHOOLS);
		try {
			Provider provider = this.providerService.getProviderById(providerId, null);
			report.setDescription("Importing courses from " + provider.getName() + " provider.");
			ProviderImporter.importCourses(provider);
			report.setResult("OK");
		} catch(Exception e) {
			report.setResult(new StringBuffer("ERROR: ").append(e.getMessage()).toString());
			throw e;
		} finally {
			cronTaskReportService.save(report);
		}
		
	}
	
	
	
}