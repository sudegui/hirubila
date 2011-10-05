package com.m4f.web.controller.summary;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.m4f.business.domain.CronTaskReport;
import com.m4f.business.domain.Provider;
import com.m4f.utils.PageManager;
import com.m4f.utils.StackTraceUtil;
import com.m4f.utils.feeds.events.model.Dump;
import com.m4f.web.bind.form.CronTaskReportFilterForm;
import com.m4f.web.controller.BaseController;
import com.m4f.web.controller.task.TaskController;

@Controller
@Secured({"ROLE_ADMIN","ROLE_MANUAL_MEDIATOR","ROLE_AUTOMATIC_MEDIATOR"})
@RequestMapping("/summary")
public class TodayController extends BaseController {
	private static final Logger LOGGER = Logger.getLogger(TaskController.class.getName());
	
	@RequestMapping(method=RequestMethod.GET)
	public String getSummary(Principal principal, Model model, Locale locale) {
		try {
			Provider provider = this.serviceLocator.getTransversalService().getProviderByUserName(principal.getName(), locale);
			/* List of Longs for representing the number of xml error, validation errors, and successful operation */
			List<Long> stats = new ArrayList<Long>(3);
			Dump dump = this.serviceLocator.getDumpService().getLastDumpByOwner(provider.getId());
			/*Long parseErrors = this.serviceLocator.getEventService().countParserErrorEventsByDump(dump);
			Long storeErrors = this.serviceLocator.getEventService().countStoreErrorEventsByDump(dump);
			Long successful = this.serviceLocator.getEventService().countStoreSuccessEventsByDump(dump);
			stats.add(parseErrors);
			stats.add(storeErrors);
			stats.add(successful);*/
			
			model.addAttribute("dumpId", dump.getId());
			model.addAttribute("providerId", provider.getId());
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		
		return "summary.today";
	}	
	
	/*@RequestMapping(value="/{dumpId}/parseerrors", method=RequestMethod.GET)
	public String getParseErrors(@PathVariable Long dumpId, @RequestParam(defaultValue="1", required=false) Integer page, 
			@RequestParam(defaultValue="", required=false) String order, Principal principal, Model model, Locale locale) {
		try {
			String ordering = "";
			Provider provider = this.getProviderByUserName(principal.getName(), locale);	
			Dump dump = this.serviceLocator.getDumpService().getLastDumpByOwner(provider.getId());
			PageManager<ParserErrorEvent> paginator = new PageManager<ParserErrorEvent>();
			paginator.setOffset(this.getPageSize());
			//paginator.setUrlBase("/" + locale.getLanguage()+ "/extended/school/list");
			paginator.setStart((page-1)*paginator.getOffset());
			paginator.setSize(this.serviceLocator.getEventService().countStoreErrorEventsByDump(dump));
			paginator.setCollection(this.serviceLocator.getEventService().
					getParserErrorEventByDump(dump, paginator.getStart(), paginator.getEnd(), ordering));
			model.addAttribute("paginator", paginator);
			model.addAttribute("order", ordering);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		
		return "summary.parseerrors";
	}*/
	
	
	/*
	 * AJAX
	 */
	/*
	 * Returns a list of dumps filtering by date. Historical dumps.
	 */
	@RequestMapping(value="/today/ajax/dump/{dumpId}", method=RequestMethod.GET)
	@ResponseBody
	public List<Long> getDump(@PathVariable Long dumpId, Principal principal, Locale locale) {
		List<Long> stats = new ArrayList<Long>(3);
		try {
			Dump dump = this.serviceLocator.getDumpService().getDump(dumpId);
			/* List of Longs for representing the number of xml error, validation errors, and successful operation */
			Long parseErrors = this.serviceLocator.getEventService().countParserErrorEventsByDump(dump, locale);
			Long storeErrors = this.serviceLocator.getEventService().countStoreErrorEventsByDump(dump, locale);
			Long successful = this.serviceLocator.getEventService().countStoreSuccessEventsByDump(dump, locale);
			stats.add(parseErrors);
			stats.add(storeErrors);
			stats.add(successful);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
		}
		return stats;
	}
	
	/*
	 * Returns a list of dumps filtering by date. Historical dumps.
	 */
	@RequestMapping(value="/today/ajax/table/{providerId}", method=RequestMethod.GET)
	@ResponseBody
	public PageManager<Dump> getHistoricalList(@PathVariable Long providerId, 
			@RequestParam(required=false) Date start, @RequestParam(required=false) Date finish, 
			@RequestParam(defaultValue="1", required=false) Integer page, @RequestParam(defaultValue="", required=false) String order,
			Principal principal, Locale locale) {
		String orderParam = "-launched";
		if(order != null && !("").equals(order)) {
			orderParam = order; 
		}
		
		PageManager<Dump> paginator = new PageManager<Dump>();
		try {
			/*Provider provider = null;
			if(providerId != null) provider = this.serviceLocator.getProviderService().getProviderById(providerId, locale);
			else provider = this.getProviderByUserName(principal.getName(), locale);*/
			paginator.setOffset(this.getPageSize());
			//paginator.setUrlBase("/" + locale.getLanguage()+ "/extended/school/list");
			paginator.setStart((page-1)*paginator.getOffset());
			paginator.setSize(this.serviceLocator.getDumpService().countDumpsByOwner(providerId));
			paginator.setCollection(this.serviceLocator.getDumpService().getDumpsByOwner(providerId, 
					paginator.getStart(), paginator.getEnd(), start, finish, orderParam));
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
		}
		
		return paginator;
	}
	
	/*
	 * Cron task report
	 */
	@RequestMapping(value="/crontaskreport")
	public String getCronTaskReportList(@ModelAttribute("filter") CronTaskReportFilterForm filter, Principal principal, Model model, Locale locale,
			@RequestParam(defaultValue="1", required=false) Integer page, @RequestParam(defaultValue="", required=false) String order,
			@RequestParam(defaultValue="", required=false) String typeFilter) {
		try {
			String ordering = order != null ? order : "-date";
			PageManager<CronTaskReport> paginator = new PageManager<CronTaskReport>();
			paginator.setOffset(this.getPageSize());
			paginator.setUrlBase("/" + locale.getLanguage() + "/summary/crontaskreport");
			paginator.setStart((page-1)*paginator.getOffset());
			
			if(filter.getType() == null && typeFilter != null && !("").equals(typeFilter)) {
				for(CronTaskReport.TYPE type : CronTaskReport.TYPE.values()) {
					if(type.getValue().equals(typeFilter)) {
						filter.setType(type);
					}
				}
			}
			if(filter.getType() != null) {
				paginator.setSize(this.serviceLocator.getCronTaskReportService().countByType(filter.getType()));
				paginator.setCollection(this.serviceLocator.getCronTaskReportService().getCronTaskReportByType(filter.getType(), ordering, paginator.getStart(), paginator.getEnd()));
			} else {
				paginator.setSize(this.serviceLocator.getCronTaskReportService().count());
				paginator.setCollection(this.serviceLocator.getCronTaskReportService().getAllCronTaskReport(ordering, paginator.getStart(), paginator.getEnd()));
			}

			model.addAttribute("paginator", paginator);
			model.addAttribute("order", ordering);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		
		return "summary.admin.crontaskreport.list";
	}
}
