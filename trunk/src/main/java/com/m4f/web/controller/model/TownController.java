package com.m4f.web.controller.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.m4f.business.domain.School;
import com.m4f.business.domain.extended.Region;
import com.m4f.business.domain.extended.Town;
import com.m4f.utils.PageManager;
import com.m4f.utils.StackTraceUtil;
import com.m4f.web.controller.BaseController;


@Controller
@RequestMapping(value="/territorial/town")
public class TownController extends BaseModelController {
private static final Logger LOGGER = Logger.getLogger(TownController.class.getName());
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method=RequestMethod.GET)
	public String getForm(Model model, Locale locale,
			@RequestHeader("referer") String referer, HttpSession session) {
		try {
			model.addAttribute("provinces", this.serviceLocator.getTerritorialService().getAllProvinces(locale));
			model.addAttribute("town", this.serviceLocator.getTerritorialService().createTown());
			session.setAttribute(this.REFERER_PARAM, referer);
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
		}
		return "territorial.town.form";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/edit/{townId}", method=RequestMethod.GET)
	public String edit(@PathVariable Long townId, Model model, Locale locale,
			@RequestHeader("referer") String referer, HttpSession session) {
		try {
			model.addAttribute("provinces", this.serviceLocator.getTerritorialService().getAllProvinces(locale));
			model.addAttribute("town", this.serviceLocator.getTerritorialService().getTown(townId, locale));
			session.setAttribute(this.REFERER_PARAM, referer);
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		
		return "territorial.town.form";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method=RequestMethod.POST)
	public String save(@ModelAttribute("town") @Valid Town town, 
			BindingResult result, Model model, Locale locale,
			@RequestHeader("Host") String host, HttpSession session) {
		if(result.hasErrors()) return "territorial.town.form";
		
		try {
			if(town.getName() != null) town.setName(town.getName().toLowerCase());
			this.serviceLocator.getTerritorialService().save(town, locale);
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		
		String returnURL = this.buildReturnURL(host, 
				(String) session.getAttribute(this.REFERER_PARAM), locale);
		return "redirect:" + returnURL;
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/delete/{townId}", method=RequestMethod.GET)
	public String delete(@PathVariable Long townId, Model model, Locale locale, 
			@RequestHeader("referer") String referer, @RequestHeader("Host") String host) {
		try {
			Town town = this.serviceLocator.getTerritorialService().getTown(townId, locale);
			this.serviceLocator.getTerritorialService().delete(town, locale);
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "redirect:" + this.buildReturnURL(host, referer, locale);
	}
		
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public String list(Model model, Locale locale,
			@RequestParam(defaultValue="1", required=false) Integer page, 
			@RequestParam(defaultValue="", required=false) String order) {
		try {
			String ordering = order != null && !("").equals(order) ? order : "name";
			PageManager<Town> paginator = new PageManager<Town>();
			paginator.setOffset(this.getPageSize());
			paginator.setUrlBase("/" + locale.getLanguage() + "/territorial/town/list");
			paginator.setStart((page-1)*paginator.getOffset());
			paginator.setSize(this.serviceLocator.getTerritorialService().countTowns());
			paginator.setCollection(this.serviceLocator.getTerritorialService().getTowns(locale, order, 
					paginator.getStart(), paginator.getEnd()));
			model.addAttribute("paginator", paginator);
			model.addAttribute("order", ordering);
			
			model.addAttribute("provincesMap", this.getProvincesMap());
			model.addAttribute("regionsMap", this.getRegionsMap());
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "territorial.town.list";
	}
		
	/*
	 * AJAX
	 */
	@Secured({"ROLE_MANUAL_MEDIATOR","ROLE_ADMIN"})
	@RequestMapping(value="/ajax/townsByRegion", method=RequestMethod.GET)
	public @ResponseBody List<Town> getTownsByRegion(@RequestParam(required=true) Long regionId, Locale locale) {
		List<Town> towns = new ArrayList<Town>();
		try {
			towns.addAll(this.serviceLocator.getTerritorialService().getTownsByRegion(regionId, locale, null));
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
		}
		return towns;
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/regions/json", method=RequestMethod.GET)
	public @ResponseBody List<Region> regionListJson(@RequestParam(required=true, defaultValue="") Long provinceId, Locale locale) {
		try {
			return this.serviceLocator.getTerritorialService().getRegionsByProvince(provinceId, locale);
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
			return new ArrayList<Region>();
		}
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/region/json", method=RequestMethod.GET)
	public @ResponseBody Region regionJson(@RequestParam(required=true, defaultValue="") Long regionId, Locale locale) {
		try {
			return this.serviceLocator.getTerritorialService().getRegion(regionId, locale);
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
			return null;
		}
	}
}
