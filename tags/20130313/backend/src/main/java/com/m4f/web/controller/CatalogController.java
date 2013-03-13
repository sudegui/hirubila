package com.m4f.web.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.m4f.business.domain.Course;
import com.m4f.utils.PageManager;

@Controller
@RequestMapping("/catalog")
public class CatalogController extends BaseController {

	private static final Logger LOGGER = Logger.getLogger(CatalogController.class.getName());

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void generateAll() throws Exception {
		final int RANGE = 200;
		LOGGER.severe("#### Regenerating catalog for all Courses.............");
		PageManager<Course> paginator = new PageManager<Course>();
		paginator.setOffset(RANGE);
		paginator.setStart(0);
		long total = courseService.count();
		paginator.setSize(total);
		LOGGER.severe("#### Total Courses............. " + total);
		LOGGER.severe("#### End page.................."
				+ paginator.getPagesMax());
		for (Integer page : paginator.getTotalPagesIterator()) {
			Map<String, String> params = new HashMap<String, String>();
			int start = (page - 1) * RANGE;
			params.put("start", "" + start);
			int end = (page) * RANGE;
			this.createInterval(start, end);
		}
	}
 
	@RequestMapping(value="/provider/create", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void createCatalog(@RequestParam Long providerId) throws Exception {
		LOGGER.severe("#### Start - Generation catalog for provider " + providerId);
		final int RANGE = 200;
		PageManager<Course> paginator = new PageManager<Course>();
		long total = courseService.countCoursesByProvider(providerId);
		paginator.setOffset(RANGE);
		paginator.setStart(0);
		paginator.setSize(total);
		for (Integer page : paginator.getTotalPagesIterator()) {
			Map<String, String> params = new HashMap<String, String>();
			int start = (page - 1) * RANGE;
			params.put("start", "" + start);
			int end = (page) * RANGE;
			this.createInterval(start, end);
		}
	}
	
	private void createInterval(Integer start, Integer finish) throws Exception {
		Collection<Course> courses = courseService.getCourses("title", null,
				start, finish);
		LOGGER.severe("+++ Paginated total courses: " + courses.size());
		for (Course course : courses) {
			this.catalogBuilder.buildSeoEntity(course.getId(), this.getAvailableLanguages());			
		}
	}
	
}