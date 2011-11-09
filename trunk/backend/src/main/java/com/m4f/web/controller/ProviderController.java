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
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.m4f.business.domain.CronTaskReport;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.School;
import com.m4f.utils.PageManager;
import com.m4f.utils.feeds.tasks.LoadSchoolCoursesTask;


@Controller
@RequestMapping("/provider")
public class ProviderController extends BaseController {
	
	@RequestMapping(value = "/ids", method = RequestMethod.GET)
	@ResponseBody
	public Collection<Long> getIds() throws Exception {
		return this.providerService.getAllProviderIds();
	}
	
	@RequestMapping(value = "/feed", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void loadFeed(@RequestParam Long providerId) 
			throws ParserConfigurationException, SAXException, IOException, Exception {
		CronTaskReport report = cronTaskReportService.create();
		report.setObject_id(providerId);
		report.setDate(new Date());
		report.setType(CronTaskReport.TYPE.PROVIDER_FEED);
		try {
			Provider provider = this.providerService.getProviderById(providerId, null);
			Collection<School> schools = schoolsParser.getSchools(provider);
			for(Locale locale : this.configurationService.getLocales()) {
				this.storeSchools(provider, schools, locale);
			}
			report.setResult("OK");
		} catch(Exception e) {
			report.setResult(new StringBuffer("ERROR: ").append(e.getMessage()).toString());
			throw e;
		} finally {
			cronTaskReportService.save(report);
		}
	}
	
	
	@RequestMapping(value = "/schools", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void loadSchools(@RequestParam Long providerId) 
			throws ParserConfigurationException, SAXException, IOException, Exception {
		CronTaskReport report = cronTaskReportService.create();
		report.setObject_id(providerId);
		report.setDate(new Date());
		report.setType(CronTaskReport.TYPE.PROVIDER_SCHOOLS);
		final int RANGE = 100;
		try {
			Provider provider = this.providerService.getProviderById(providerId, null);
			PageManager<School> paginator = new PageManager<School>();
			long total = schoolService.countSchoolsByProvider(providerId);
			paginator.setOffset(RANGE);
			paginator.setStart(0);
			paginator.setSize(total);
			for (Integer page : paginator.getTotalPagesIterator()) {
				int start = (page - 1) * RANGE;
				int end = (page) * RANGE;
				Collection<School> schools = schoolService.getSchoolsByProvider(providerId, "updated", null, start, end);
				for(School school : schools) {
					this.createLoadTask(provider, school);
				}
			}
			report.setResult("OK");
		} catch(Exception e) {
			report.setResult(new StringBuffer("ERROR: ").append(e.getMessage()).toString());
			throw e;
		} finally {
			cronTaskReportService.save(report);
		}
		
	}
	
	private void storeSchools(Provider provider, Collection<School> schools, 
			Locale locale) throws Exception {
		schoolStorage.store(schools, locale, provider);
	}
	
	
	private void createLoadTask(Provider p, School s) {
		TaskOptions taskOptions = TaskOptions.Builder.withPayload(new LoadSchoolCoursesTask(p,s));
		taskOptions.taskName(p.getId() + "-" + s.getId() + 
				"-" + Calendar.getInstance().getTimeInMillis());
		Queue queue = QueueFactory.getQueue(configurationService.getGlobalConfiguration().SCHOOL_QUEUE);
		queue.add(taskOptions);
	}
	
	
}