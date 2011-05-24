package com.m4f.business.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import com.google.appengine.api.datastore.Blob;
import com.m4f.business.domain.Course;
import com.m4f.business.domain.CourseCatalog;
import com.m4f.business.domain.CourseHtml;
import com.m4f.business.service.ifc.ICourseHtmlService;
import com.m4f.utils.cache.annotations.Cacheable;
import com.m4f.utils.cache.annotations.Cacheflush;
import com.m4f.utils.i18n.dao.ifc.DAOSupport;
import java.net.SocketTimeoutException;

public class CourseHtmlServiceImpl extends DAOBaseService implements ICourseHtmlService {
	
	public CourseHtmlServiceImpl(DAOSupport dao) {
		super(dao);
	}

	@Override
	public CourseHtml create() {
		return this.DAO.createInstance(CourseHtml.class);
	}                                                                                                                
	
	@Override
	//@Cacheflush(cacheName="htmlcourses")
	public void save(CourseHtml courseHtml) throws Exception {
		this.DAO.saveOrUpdate(courseHtml);
	}
	
	@Override
	public long count(Locale locale) throws Exception {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("lang", locale.getLanguage());
		return this.DAO.count(CourseHtml.class, filter);
	}
	
	@Override
	//@Cacheable(cacheName="htmlcourses")
	public CourseHtml get(Long courseId, 
			String language) throws Exception {
		CourseHtml courseHtml = this.DAO.findEntity(CourseHtml.class, 
				"courseId == idParam && lang == langParam", 
				"java.lang.Long idParam, java.lang.String langParam", 
				new Object[]{courseId, language});	
		return courseHtml;
	}

	@Override
	//@Cacheable(cacheName="htmlcourses")
	public Collection<CourseHtml> getCourses(Locale locale, 
			String ordering, int init, int end) throws Exception {
		String filter = "courseId == idParam && lang == langParam"; 
		String params = "java.lang.String langParam";
		return this.DAO.findEntitiesByRange(CourseHtml.class, filter, params, 
				new Object[]{locale.getLanguage()}, ordering, init, end);
	}

	@Override
	//@Cacheflush(cacheName="htmlcourses")
	public CourseHtml convertToCourseHTML(Course course, Locale locale,
			URL urlCourse) throws Exception {
		CourseHtml courseHTML = this.get(course.getId(), locale.getLanguage());
		
		if(courseHTML == null) {
			courseHTML = this.create();
			courseHTML.setCourseId(course.getId());
			courseHTML.setLang(locale.getLanguage());
		}
		
		courseHTML.setDate(Calendar.getInstance(new Locale("es")).getTime());
		courseHTML.setTitle(course.getTitle());
		URLConnection connection = urlCourse.openConnection();
		connection.setAllowUserInteraction(false);         
		connection.setDoOutput(true);
		ByteArrayOutputStream tmpOut = new ByteArrayOutputStream();
		byte[] buf = new byte[512];
		       int len;
		       while (true) {
		            len = connection.getInputStream().read(buf);
		            if (len == -1) {
		                break;
		            }
		            tmpOut.write(buf, 0, len);
		        }
		    tmpOut.close();
			Blob content = new Blob(tmpOut.toByteArray());
			courseHTML.setContent(content);
			this.save(courseHTML);
		
		return courseHTML;
	}

	@Override
	//@Cacheflush(cacheName="htmlcourses")
	public void delete(CourseHtml course, Locale locale) throws Exception {
		this.DAO.delete(course);		
	}
	
	/****************
	 * COURSE CATALOG
	 */
	@Override
	public CourseCatalog createCourseCatalog() {
		return this.DAO.createInstance(CourseCatalog.class);
	}

	@Override
	@Cacheflush(cacheName="coursesCatalog")
	public void delete(CourseCatalog course) throws Exception {
		this.DAO.delete(course);
	}
	
	@Override
	@Cacheable(cacheName="coursesCatalog")
	public CourseCatalog getCourseCatalogById(Long id) throws Exception {
		return this.DAO.findById(CourseCatalog.class, id);
	}

	@Override
	@Cacheable(cacheName="coursesCatalog")
	public CourseCatalog getCourseCatalogByCourseId(Long courseId, Locale locale) throws Exception {		
		String filter = "courseId == courseIdParam && lang == langParam";
		String params = "java.lang.String courseIdParam, java.lang.String langParam";
		CourseCatalog course = this.DAO.findEntity(CourseCatalog.class, filter, 
				params, new Object[] {courseId, locale.getLanguage()});
		return course;
	}

	@Override
	@Cacheflush(cacheName="coursesCatalog")
	public void save(CourseCatalog courseCatalog) throws Exception {
		this.DAO.saveOrUpdate(courseCatalog);
	}
	
	@Override
	@Cacheable(cacheName="coursesCatalog")
	public long countCourseCatalog(Locale locale) throws Exception {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("lang", locale.getLanguage());
		return this.DAO.count(CourseCatalog.class, filter);
	}

	
	@Override
	@Cacheable(cacheName="coursesCatalog")
	public Collection<CourseCatalog> getCoursesCatalog(String ordering,
			Locale locale, int init, int end) {
		String filter = "lang == langParam"; 
		String params = "java.lang.String langParam";
		return this.DAO.findEntitiesByRange(CourseCatalog.class, filter, params, 
				new Object[]{locale.getLanguage()}, ordering, init, end);
	}
	
	@Override
	public long countCourseCatalog(boolean reglated, Locale locale)
			throws Exception {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("lang", locale.getLanguage());
		filter.put("regulated", reglated);
		return this.DAO.count(CourseCatalog.class, filter);
	}

	@Override
	public Collection<CourseCatalog> getCoursesCatalog(boolean reglated,
			String ordering, Locale locale, int init, int end) {
		String filter = "lang == langParam && regulated == reglatedParam"; 
		String params = "java.lang.String langParam, java.lang.Boolean reglatedParam";
		return this.DAO.findEntitiesByRange(CourseCatalog.class, filter, params, 
				new Object[]{locale.getLanguage(), reglated}, ordering, init, end);
	}
	
	@Override
	public void deleteCourseCatalogByCourseId(Long courseId)
			throws Exception {
		Collection<CourseCatalog> coursesCatalog = this.getAllByCourseId(courseId);
		for(CourseCatalog courseCatalog : coursesCatalog) {
			this.delete(courseCatalog);
		}
	}
	
	private Collection<CourseCatalog> getAllByCourseId(Long courseId) {
		String filter = "courseId == courseIdParam";
		String params = "java.lang.Long courseIdParam";
		Collection<CourseCatalog> coursesCatalog = this.DAO.findEntities(CourseCatalog.class, filter, 
				params, new Object[] {courseId}, null);
		return coursesCatalog;
	}
}