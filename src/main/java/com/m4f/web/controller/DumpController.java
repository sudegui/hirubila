package com.m4f.web.controller;

import java.util.Locale;
import java.util.logging.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.m4f.utils.PageManager;
import com.m4f.utils.StackTraceUtil;
import com.m4f.utils.feeds.events.model.Dump;
import com.m4f.utils.feeds.events.model.StoreErrorEvent;
import com.m4f.utils.feeds.events.model.StoreSuccessEvent;

@Controller
@RequestMapping("/dump")
public class DumpController extends BaseController {
	
	private static final Logger LOGGER = Logger.getLogger(DumpController.class.getName());
	
	@RequestMapping(method=RequestMethod.GET)
	public String goHome(Model model, Locale locale, 
			@RequestParam(defaultValue="1", required=false) Integer page) {
		try {		
			PageManager<Dump> paginator = new PageManager<Dump>();
			paginator.setUrlBase("/" + locale.getLanguage() + "/dump/");
			paginator.setStart((page-1)*paginator.getOffset());
			paginator.setSize(this.serviceLocator.getDumpService().countDumps());
			paginator.setCollection(this.serviceLocator.getDumpService().getAllDumps());
			model.addAttribute("paginator", paginator);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "dump.main";
	}
	
	
	@RequestMapping(value="/{dumpId}/delete", method=RequestMethod.GET)
	public String delete(@PathVariable Long dumpId, 
			Model model, Locale locale) {
		try {
			Dump dump = this.serviceLocator.getDumpService().getDump(dumpId);
			this.serviceLocator.getDumpService().delete(dump);
		} catch (Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "redirect:/" + locale.getLanguage() + "/dump/";
	}
	
	@RequestMapping(value="/{dumpId}/events/parser-error", method=RequestMethod.GET)
	public String getParseErrors(@PathVariable Long dumpId, Model model, Locale locale) {
		try {
			Dump dump = this.serviceLocator.getDumpService().getDump(dumpId);
			if(dump == null) {
				model.addAttribute("message", "Dump with id " + dumpId + " doesn´t exist.");
				return "common/error";
			}
			model.addAttribute("errors",this.serviceLocator.getEventService().getParserErrorByDump(dump));
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			model.addAttribute("message", "Exception produced ...");
			return "common.error";
		}
		return "dump.parsererror.list";
	}
	
	@RequestMapping(value="/{dumpId}/events/store-error", method=RequestMethod.GET)
	public String getStoreErrorEvents(@PathVariable Long dumpId, Model model, 
			Locale locale, @RequestParam(defaultValue="1", required=false) Integer page) {
		try {
			Dump dump = this.serviceLocator.getDumpService().getDump(dumpId);
			if(dump == null) {
				model.addAttribute("message", "Dump with id " + dumpId + " doesn´t exist.");
				return "common/error";
			}
			model.addAttribute("errors",this.serviceLocator.getEventService().getStoreErrorEventsByDump(dump));
			PageManager<StoreErrorEvent> paginator = new PageManager<StoreErrorEvent>();
			paginator.setUrlBase("/" + locale.getLanguage() + "/dump/" + dump.getId() + 
					"/events/store-error");
			paginator.setStart((page-1)*paginator.getOffset());
			paginator.setSize(this.serviceLocator.getEventService().countStoreErrorEventsByDump(dump));
			paginator.setCollection(this.serviceLocator.getEventService().getStoreErrorEventByDump(dump, 
					paginator.getStart(), paginator.getEnd(), null));
			model.addAttribute("paginator", paginator);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			model.addAttribute("message", "Exception produced ...");
			return "common.error";
		}
		return "dump.dumpererror.list";
	}
	
	@RequestMapping(value="/{dumpId}/events/store-success", method=RequestMethod.GET)
	public String getStoreSuccessEvents(@PathVariable Long dumpId, Model model, 
			Locale locale, @RequestParam(defaultValue="1", required=false) Integer page) {
		try {
			Dump dump = this.serviceLocator.getDumpService().getDump(dumpId);	
			if(dump == null) {
				model.addAttribute("message", "Dump with id " + dumpId + " doesn´t exist.");
				return "common/error";
			}
			model.addAttribute("dump", dump);
			PageManager<StoreSuccessEvent> paginator = new PageManager<StoreSuccessEvent>();
			paginator.setUrlBase("/" + locale.getLanguage() + "/dump/" + dump.getId() + 
					"/events/store-success");
			paginator.setStart((page-1)*paginator.getOffset());
			paginator.setSize(this.serviceLocator.getEventService().countStoreSuccessEventsByDump(dump));
			paginator.setCollection(this.serviceLocator.getEventService().getStoreSuccessEventByDump(dump, 
					paginator.getStart(), paginator.getEnd(), null));
			model.addAttribute("paginator", paginator);	
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			model.addAttribute("message", "Exception produced ...");
			return "common.error";
		}
		return "dump.dumpersuccess.list";
	}
	
	@RequestMapping(value="/events/success/delete/all/{dumpId}", method=RequestMethod.GET)
	public String deleteAllSuccessEvents(@PathVariable Long dumpId, 
			Model model, Locale locale) {
		Dump dump = null;
		try {
			dump = this.serviceLocator.getDumpService().getDump(dumpId);
			if(dump == null) {
				model.addAttribute("message", "Dump with id " + dumpId + " doesn´t exist.");
				return "common/error";
			}
			this.serviceLocator.getEventService().deleteStoreSuccessEventsByDump(dump);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			model.addAttribute("message", "Exception produced ...");
			return "common.error";
		}
		return "redirect:/" + locale.getLanguage() + "/dump/events/success/list/" + dump.getId();
	}
	
}