package com.m4f.web.controller;

import java.util.Locale;
import java.util.logging.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.m4f.utils.i18n.model.impl.JdoI18nEntry;
import com.m4f.utils.StackTraceUtil;

@Controller
@RequestMapping(value="/i18n")
public class I18nController extends BaseController {
	
	private static final Logger LOGGER = Logger.getLogger(I18nController.class.getName());
	
	public I18nController() {
		super();
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String getView(Model model) {
		try {
			model.addAttribute("total", this.serviceLocator.getI18nService().countTotalTranslations());
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "i18n.list";
	}
	
	@RequestMapping(value="/translations/{contentId}", method=RequestMethod.GET)
	public String getTranslations(@PathVariable Long contentId, 
			Model model, Locale locale) {
		try {
			model.addAttribute("translations", 
					this.serviceLocator.getI18nService().getContentTranslationsMap("" + contentId));
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "i18n.form";
	}
	
	@RequestMapping(value="/delete/{entryId}", method=RequestMethod.GET)
	public String deleteI18n(@PathVariable Long entryId, Model model, 
			@RequestParam(required=false, defaultValue="en") String lang) {
		try {
			JdoI18nEntry entry = this.serviceLocator.getI18nService().getEntry(entryId);
			this.serviceLocator.getI18nService().delete(entry);
		} catch (Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "redirect:/" + lang + "/i18n";
	}
	
	/********************************************************************
	 * 
	 * 				HEAVY BATCH PROCESSES
	 * 
	 *********************************************************************/
	
	
	/**
	 * 
	 * @param model
	 * @param locale
	 * @return
	 */
	@RequestMapping(value="/delete/all", method=RequestMethod.GET)
	public String deleteAll(Model model, Locale locale) {
		try {
			LOGGER.severe("#### deleting all.............");
			this.serviceLocator.getI18nService().deleteAll();
		} catch (Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "task.launched";
	}
}