package com.m4f.web.controller.model;

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

import com.m4f.business.domain.extended.Province;
import com.m4f.business.domain.extended.Region;
import com.m4f.business.domain.extended.Town;
import com.m4f.utils.PageManager;
import com.m4f.utils.StackTraceUtil;


@Controller
@RequestMapping(value="/territorial/province")
public class ProvinceController extends BaseModelController {
	private static final Logger LOGGER = Logger.getLogger(ProvinceController.class.getName());
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method=RequestMethod.GET)
	public String getForm(Model model,
			@RequestHeader("referer") String referer, HttpSession session) {
		try  {
			model.addAttribute("province", this.serviceLocator.getTerritorialService().createProvince());
			session.setAttribute(this.REFERER_PARAM, referer);
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "territorial.province.form";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/edit/{provinceId}", method=RequestMethod.GET)
	public String edit(@PathVariable Long provinceId, Model model, Locale locale,
			@RequestHeader(required=false,value="referer") String referer, HttpSession session) {
		try {
			model.addAttribute("province", this.serviceLocator.getTerritorialService().getProvince(provinceId, locale));
			session.setAttribute(this.REFERER_PARAM, referer);
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		
		return "territorial.province.form";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method=RequestMethod.POST)
	public String save(@ModelAttribute("province") @Valid Province province, 
			BindingResult result, Model model, Locale locale,
			@RequestHeader("Host") String host, HttpSession session) {
		if(result.hasErrors()) return "territorial.province.form";
		
		try {
			if(province.getName() != null) province.setName(province.getName().toLowerCase());
			this.serviceLocator.getTerritorialService().save(province, locale);
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		String returnURL = this.buildReturnURL(host, 
				(String) session.getAttribute(this.REFERER_PARAM), locale);
		return "redirect:" + returnURL;
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/delete/{provinceId}", method=RequestMethod.GET)
	public String delete(@PathVariable Long provinceId, Model model, Locale locale, 
			@RequestHeader("referer") String referer, @RequestHeader("Host") String host) {
		try {
			Province province = this.serviceLocator.getTerritorialService().getProvince(provinceId, locale);
			this.serviceLocator.getTerritorialService().delete(province, locale);
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "redirect:" + this.buildReturnURL(host, referer, locale);
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/{provinceId}/regions", method=RequestMethod.GET)
	public String getRegions(@PathVariable Long provinceId, Model model, Locale locale,
			@RequestParam(defaultValue="1", required=false) Integer page, 
			@RequestParam(defaultValue="", required=false) String order) {
		try {
			String ordering = order != null && !("").equals(order) ? order : "name";
			PageManager<Region> paginator = new PageManager<Region>();
			paginator.setOffset(this.getPageSize());
			paginator.setUrlBase("/" + locale.getLanguage() + 
					"/territorial/province/" +  provinceId + "/towns/");
			paginator.setStart((page-1)*paginator.getOffset());
			paginator.setSize(this.serviceLocator.getTerritorialService().countRegionsByProvince(provinceId));
			paginator.setCollection(this.serviceLocator.getTerritorialService().getRegionsByProvince(provinceId, 
					locale, ordering, paginator.getStart(), paginator.getEnd()));
			model.addAttribute("paginator", paginator);
			model.addAttribute("order", ordering);
			model.addAttribute("provincesMap", this.serviceLocator.getTransversalService().getProvincesMap());
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "territorial.region.list";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/{provinceId}/towns", method=RequestMethod.GET)
	public String getTowns(@PathVariable Long provinceId, Model model, Locale locale,
			@RequestParam(defaultValue="1", required=false) Integer page, 
			@RequestParam(defaultValue="", required=false) String order) {
		try {
			String ordering = order != null && !("").equals(order) ? order : "name";
			PageManager<Town> paginator = new PageManager<Town>();
			paginator.setOffset(this.getPageSize());
			paginator.setUrlBase("/" + locale.getLanguage() + 
					"/territorial/province/" +  provinceId + "/towns/");
			paginator.setStart((page-1)*paginator.getOffset());
			paginator.setSize(this.serviceLocator.getTerritorialService().countTownsByProvince(provinceId));
			paginator.setCollection(this.serviceLocator.getTerritorialService().getTownsByProvince(provinceId, 
					locale, ordering, paginator.getStart(), paginator.getEnd()));
			model.addAttribute("paginator", paginator);
			model.addAttribute("order", ordering);
			
			//model.addAttribute("province", this.serviceLocator.getTerritorialService().getProvince(provinceId,locale));
			//model.addAttribute("towns", this.serviceLocator.getTerritorialService().getTownsByProvince(provinceId, locale));
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
			PageManager<Province> paginator = new PageManager<Province>();
			paginator.setOffset(this.getPageSize());
			paginator.setUrlBase("/" + locale.getLanguage() + 
					"/territorial/province/list");
			paginator.setStart((page-1)*paginator.getOffset());
			paginator.setSize(this.serviceLocator.getTerritorialService().countProvinces());
			paginator.setCollection(this.serviceLocator.getTerritorialService().getProvinces(locale, ordering, paginator.getStart(), paginator.getEnd()));
			model.addAttribute("paginator", paginator);
			model.addAttribute("order", ordering);
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
			return "common/error";
		}
		return "territorial.province.list";
	}
	
	
}
