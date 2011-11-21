package com.m4f.web.controller;

import java.io.IOException;
import java.util.Locale;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.m4f.business.domain.CourseCatalog;
import com.m4f.utils.PageManager;
import com.m4f.utils.StackTraceUtil;
import org.springframework.http.HttpStatus;
import com.m4f.web.controller.exception.GenericException;

@Controller
@RequestMapping("/catalog")
public class CatalogController extends BaseController {
	
	private static final Logger LOGGER = Logger.getLogger(CatalogController.class.getName());
	private static final String ORDERING_PROPERTY = "id";
	
	@RequestMapping(method=RequestMethod.GET)
	public String getIndex() {
		return "catalog.index";
	}
	
	/**
	 * COURSES CATALOG
	 */
	@RequestMapping(value="/reglated/course/list", method=RequestMethod.GET)
	public String listReglated(Model model, Locale locale,
			@RequestParam(defaultValue="1", required=false) Integer page) throws GenericException {
		try {
			PageManager<CourseCatalog> paginator = new PageManager<CourseCatalog>();
			paginator.setOffset(this.getPageSize());
			paginator.setUrlBase("/" + locale.getLanguage()+ "/catalog/reglated/course/list");
			paginator.setStart((page-1)*paginator.getOffset());
			paginator.setSize(this.serviceLocator.getCatalogService().countCourseCatalog(true, locale));
			paginator.setCollection(this.serviceLocator.getCatalogService().getCoursesCatalog(true, 
					"-" + ORDERING_PROPERTY, locale, paginator.getStart(), paginator.getEnd()));
			model.addAttribute("paginator", paginator);
			model.addAttribute("type", "reglated");
		} catch(Exception e) {
			throw new GenericException(e);
		}
		return "catalog.course.list";
	}
	
	@RequestMapping(value="/non-reglated/course/list", method=RequestMethod.GET)
	public String listNonReglated(Model model, Locale locale,
			@RequestParam(defaultValue="1", required=false) Integer page) throws GenericException {
		try {
			PageManager<CourseCatalog> paginator = new PageManager<CourseCatalog>();
			paginator.setOffset(this.getPageSize());
			paginator.setUrlBase("/" + locale.getLanguage()+ "/catalog/non-reglated/course/list");
			paginator.setStart((page-1)*paginator.getOffset());
			paginator.setSize(this.serviceLocator.getCatalogService().countCourseCatalog(false, locale));
			paginator.setCollection(this.serviceLocator.getCatalogService().getCoursesCatalog(false, 
					"-" + ORDERING_PROPERTY, locale, paginator.getStart(), paginator.getEnd()));
			model.addAttribute("paginator", paginator);
			model.addAttribute("type", "non-reglated");
		} catch(Exception e) {
			throw new GenericException(e);
		}
		return "catalog.course.list";
	}
	
	@RequestMapping(value="/reglated/course/detail/{courseId}", method=RequestMethod.GET)
	public String reglatedDetail(@PathVariable Long courseId, Model model, Locale locale, 
			HttpServletResponse response) throws GenericException {
		return this.redirectToCourseDetail(response, courseId, model, locale);
	}
	
	@RequestMapping(value="/non-reglated/course/detail/{courseId}", method=RequestMethod.GET)
	public String nonReglatedDetail(@PathVariable Long courseId, Model model, Locale locale, 
			HttpServletResponse response) throws GenericException {
		return this.redirectToCourseDetail(response, courseId, model, locale);
	}
	
	@RequestMapping(value="/course/detail/{courseId}", method=RequestMethod.GET)
	public String detail(@PathVariable Long courseId, Model model, 
			Locale locale, HttpServletResponse response) throws GenericException {
		return this.redirectToCourseDetail(response, courseId, model, locale);
	}
	
	private String redirectToCourseDetail(HttpServletResponse response, 
			Long courseId, Model model, Locale locale) throws GenericException {
		try {
			CourseCatalog courseCatalog = 
				this.serviceLocator.getCatalogService().getCourseCatalogByCourseId(courseId, locale);
			model.addAttribute("course", courseCatalog);
			response.addDateHeader("Last-Modified", courseCatalog.getStart().getTime());
		} catch(Exception e) {
			throw new GenericException(e);
		}
		return "search.result.detail";
	}
	
	@ExceptionHandler(GenericException.class)
	public String handleCatalogException(Exception ex, 
			HttpServletResponse response)  throws IOException {
		LOGGER.severe(StackTraceUtil.getStackTrace(ex));
		response.sendError(HttpStatus.NOT_FOUND.value());
		return "common.error";
	}
	
}