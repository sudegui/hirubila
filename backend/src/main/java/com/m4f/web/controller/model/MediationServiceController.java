package com.m4f.web.controller.model;

import java.security.Principal;
import java.util.Locale;
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

import com.google.appengine.api.datastore.GeoPt;
import com.m4f.business.domain.MediationService;
import com.m4f.business.domain.Provider;
import com.m4f.business.service.extended.impl.InternalFeedServiceImpl;
import com.m4f.utils.StackTraceUtil;
import com.m4f.web.bind.form.MediationForm;


@Controller
@Secured({"ROLE_ADMIN"})
@RequestMapping("/mediation")
public class MediationServiceController extends BaseModelController {
	
	private static final Logger LOGGER = Logger.getLogger(MediationServiceController.class.getName());
	
	
	@RequestMapping(method=RequestMethod.GET)
	public String getView(Principal currentUser, Model model, Locale locale,
			@RequestHeader(required=false,value="referer") String referer, 
			HttpSession session) {
		try {
			MediationForm form = new MediationForm();
			form.setMediationService(this.serviceLocator.getMediatorService().createMediationService());
			form.setProvider(this.serviceLocator.getProviderService().createProvider());
			model.addAttribute("mediation", form);
			model.addAttribute("provinces", this.serviceLocator.getTerritorialService().getAllProvinces(locale));
			session.setAttribute(this.REFERER_PARAM, referer);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "mediation.form";
	}
	
	
	@RequestMapping(value="/detail/{mediationId}", method=RequestMethod.GET)
	public String getDetail(@PathVariable Long mediationId, 
			Model model, Locale locale) {
		try {
			MediationService mediationService = 
				this.serviceLocator.getMediatorService().getMediationService(mediationId, locale);
			model.addAttribute("mediationService", mediationService);
			model.addAttribute("members", this.serviceLocator.getUserService().findUsersByMediationService(mediationService));
		} catch (Exception e) {
			this.viewHelper.errorManagement(e);
		}
		return "mediation.detail";
	}
	
	@RequestMapping(value="/edit/{mediationId}", method=RequestMethod.GET)
	public String getView(@PathVariable Long mediationId, 
			Principal currentUser, Model model, Locale locale,
			@RequestHeader(required=false,value="referer") String referer, 
			HttpSession session) {
		try {
			MediationService mediationService = 
				this.serviceLocator.getMediatorService().getMediationService(mediationId, locale);
			MediationForm form = new MediationForm();
			form.setMediationService(mediationService);
			form.setLongitude(mediationService.getGeoLocation().getLongitude());
			form.setLatitude(mediationService.getGeoLocation().getLatitude());
			//TODO aunque no sea un servicio de mediacion automatico también tiene un proveedor asociado. 
			Provider provider = this.serviceLocator.getProviderService().createProvider();
			if(mediationService.getHasFeed()) {
				provider = this.serviceLocator.getProviderService().findProviderByMediator(mediationService, locale);
			}
			form.setProvider(provider);
			form.addMembers(this.serviceLocator.getUserService().findUsersByMediationService(mediationService));
			model.addAttribute("mediation", form);
			model.addAttribute("provinces", this.serviceLocator.getTerritorialService().getAllProvinces(locale));
			session.setAttribute(this.REFERER_PARAM, referer);
		}catch(Exception e) {
			this.viewHelper.errorManagement(e);
		}
		return "mediation.form";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String save(@ModelAttribute("mediation") @Valid MediationForm form, 
			BindingResult result, Principal currentUser, Locale locale,
			@RequestHeader("Host") String host, HttpSession session) {
		
		try {
			MediationService mediationService = form.getMediationService();
			if(form.getLatitude() != null && form.getLongitude() != null) {
				mediationService.setGeoLocation(new GeoPt(form.getLatitude(), form.getLongitude()));
			}
			mediationService.addUsers(form.getMembers()); // TODO I think that this don´t do anything useful, because there are not member fields in the form.
			this.validator.validate(mediationService, result);
			if(result.hasErrors()) {
				return "mediation.form";
			}
			if(mediationService.getId() != null) { // Edit mode. Get old mediation to set the user list and not lost them.
				MediationService mediationOld = this.serviceLocator.getMediatorService().getMediationService(mediationService.getId(), locale);
				mediationService.setMembers(mediationOld.getMembers());
			}
			this.serviceLocator.getMediatorService().save(mediationService, locale);
			Provider provider = form.getProvider();
			provider.setName(mediationService.getName());
			provider.setMediationService(mediationService.getId());
			if(mediationService.getHasFeed()) {
				this.validator.validate(provider, result);
				if(result.hasErrors()) {
					return "mediation.form";
				}
				this.serviceLocator.getProviderService().save(provider, locale);
			} else if(!mediationService.getHasFeed()) {
				// NEW. For manual mediations service a provider is created to with hard-coded url to get its school feed.
				this.serviceLocator.getProviderService().save(provider, locale);
				provider.setFeed(new StringBuffer("http://").append(host).append("/").
						append(locale).append(InternalFeedServiceImpl.SCHOOL_DETAIL_URL).append(provider.getId()).toString());
				this.serviceLocator.getProviderService().save(provider, locale);
				this.validator.validate(provider, result);
			}else {
				if(provider.getId()!=null) {
					/*Operacion obligatoria para poder borrar el provider, ya que
					 * al lanzar la sentencia this.providerService.deleteProvider(provider, locale),
					 * se producía un error al ser provider un objeto transitorio.
					 */
					this.serviceLocator.getProviderService().deleteProvider(
							this.serviceLocator.getProviderService().getProviderById(provider.getId(), 
							locale), locale);
				}
			}
		} catch(Exception e) {
			this.viewHelper.errorManagement(e);
		}
		String returnURL = this.buildReturnURL(host, 
				(String) session.getAttribute(this.REFERER_PARAM), locale);
		return "redirect:" + returnURL;
	}
	
	
	@RequestMapping(value="/delete/{mediationId}", method=RequestMethod.GET)
	public String delete(@PathVariable Long mediationId, 
			Principal currentUser, Model model, Locale locale, 
			@RequestHeader("referer") String referer, 
			@RequestHeader("Host") String host) {
		try {
			MediationService mediationService = 
				this.serviceLocator.getMediatorService().getMediationService(mediationId, locale);
			/*
			 * TODO comento la eliminacion del provider cuando se elimina el mediation service, ya que esto implica que  hay que eliminar
			 * todos los centros, cursos, cursos del catalogo asociados a este provider. Ademas, ya no hay que distinguir si el meditionservice
			 * tiene feed, porque siempre se crea un proveedor cuando se anyade un mediation service.
			 * 
			 * if(mediationService.getHasFeed()) {
				Provider provider = this.serviceLocator.getProviderService().findProviderByMediator(mediationService, locale);
				if(provider != null) this.serviceLocator.getProviderService().deleteProvider(provider, locale);
			}		*/
			//Se borra el servicio de mediación y el proveedor asociado.
			//TODO que hacer con los usuarios relacionados con el servicio de mediacion.
			this.serviceLocator.getMediatorService().delete(mediationService, locale);
		}catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
			return "common.error";
		}
		return "redirect:" + this.buildReturnURL(host, referer, locale);
	}
	
}