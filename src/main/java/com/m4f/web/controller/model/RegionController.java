package com.m4f.web.controller.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
import com.m4f.business.domain.extended.Province;
import com.m4f.business.domain.extended.Region;
import com.m4f.business.domain.extended.Town;
import com.m4f.utils.PageManager;
import com.m4f.utils.StackTraceUtil;
import com.m4f.web.controller.BaseController;


@Controller
@RequestMapping(value="/territorial/region")
public class RegionController extends BaseModelController {
	
	private static final Logger LOGGER = Logger.getLogger(RegionController.class.getName());
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method=RequestMethod.GET)
	public String getForm(Model model, Locale locale,
			@RequestHeader("referer") String referer, HttpSession session) {
		try {
			model.addAttribute("provinces", this.serviceLocator.getTerritorialService().getAllProvinces(locale));
			model.addAttribute("region", this.serviceLocator.getTerritorialService().createRegion());
			session.setAttribute(this.REFERER_PARAM, referer);
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
		}
		return "territorial.region.form";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/edit/{regionId}", method=RequestMethod.GET)
	public String edit(@PathVariable Long regionId, Model model, Locale locale,
			@RequestHeader("referer") String referer, HttpSession session) {
		try {
			model.addAttribute("provinces", this.serviceLocator.getTerritorialService().getAllProvinces(locale));
			model.addAttribute("region", this.serviceLocator.getTerritorialService().getRegion(regionId, locale));
			session.setAttribute(this.REFERER_PARAM, referer);
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		
		return "territorial.region.form";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method=RequestMethod.POST)
	public String save(@ModelAttribute("region") @Valid Region region, 
			BindingResult result, Model model, Locale locale,
			@RequestHeader("Host") String host, HttpSession session) {
		if(result.hasErrors()) return "territorial.region.form";
		
		try {
			if(region.getName() != null) region.setName(region.getName().toLowerCase());
			this.serviceLocator.getTerritorialService().save(region, locale);
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
			return "common.error";
		}	
		String returnURL = this.buildReturnURL(host, 
				(String) session.getAttribute(this.REFERER_PARAM), locale);
		return "redirect:" + returnURL;
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/delete/{regionId}", method=RequestMethod.GET)
	public String delete(@PathVariable Long regionId, Model model, Locale locale, 
			@RequestHeader("referer") String referer, @RequestHeader("Host") String host) {
		try {
			Region region = this.serviceLocator.getTerritorialService().getRegion(regionId, locale);
			this.serviceLocator.getTerritorialService().delete(region, locale);
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "redirect:" + this.buildReturnURL(host, referer, locale);
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/{regionId}/towns", method=RequestMethod.GET)
	public String getTowns(@PathVariable Long regionId, Model model, Locale locale,
			@RequestParam(defaultValue="1", required=false) Integer page, 
			@RequestParam(defaultValue="", required=false) String order) {
		try {	
			String ordering = order != null && !("").equals(order) ? order : "name";
			PageManager<Town> paginator = new PageManager<Town>();
			paginator.setOffset(this.getPageSize());
			paginator.setUrlBase("/" + locale.getLanguage() + 
					"/territorial/region/" +  regionId + "/towns/");
			paginator.setStart((page-1)*paginator.getOffset());
			paginator.setSize(this.serviceLocator.getTerritorialService().countTownsByRegion(regionId));
			paginator.setCollection(this.serviceLocator.getTerritorialService().getTownsByRegion(regionId, 
					locale, ordering, paginator.getStart(), paginator.getEnd()));
			model.addAttribute("paginator", paginator);
			model.addAttribute("order", ordering);
			
			model.addAttribute("provincesMap", this.serviceLocator.getTransversalService().getProvincesMap());
			model.addAttribute("regionsMap", this.serviceLocator.getTransversalService().getRegionsMap());
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "territorial.town.list";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public String list(Model model, Locale locale,
			@RequestParam(defaultValue="1", required=false) Integer page, 
			@RequestParam(defaultValue="", required=false) String order) {
		try {
			String ordering = order != null && !("").equals(order) ? order : "name";
			PageManager<Region> paginator = new PageManager<Region>();
			paginator.setOffset(this.getPageSize());
			paginator.setUrlBase("/" + locale.getLanguage() + 
					"/territorial/region/list");
			paginator.setStart((page-1)*paginator.getOffset());
			paginator.setSize(this.serviceLocator.getTerritorialService().countRegions());
			paginator.setCollection(this.serviceLocator.getTerritorialService().getRegions(locale, ordering, paginator.getStart(), paginator.getEnd()));
			model.addAttribute("paginator", paginator);
			model.addAttribute("order", ordering);
			model.addAttribute("provincesMap", 
					this.serviceLocator.getTransversalService().getProvincesMap());
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "territorial.region.list";
	}
	
	/*
	 * AJAX
	 */
	@Secured({"ROLE_MANUAL_MEDIATOR","ROLE_ADMIN"})
	@RequestMapping("/ajax/regionsByProvince")
	public @ResponseBody List<Region> getRegionsByProvince(@RequestParam(required=true) Long provinceId, Locale locale) {
		List<Region> regions = new ArrayList<Region>();
		try {
			regions = this.serviceLocator.getTerritorialService().getRegionsByProvince(provinceId, locale);
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
		}
		
		return regions;
	}
}