package com.m4f.web.servlet;

import java.io.IOException;
import java.util.Locale;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.m4f.business.domain.CourseCatalog;
import com.m4f.business.service.ifc.ICatalogService;
import com.m4f.business.service.ifc.IServiceLocator;
import com.m4f.business.service.impl.GaeJdoCatalogService;
import com.m4f.utils.i18n.dao.impl.jdo.JdoDAO;

@SuppressWarnings("serial")
public class CourseCatalogServlet extends HttpServlet {
	private static final Logger LOGGER = Logger.getLogger(CourseCatalogServlet.class.getName());
	
	private ICatalogService courseHTMLService;
	
	public CourseCatalogServlet() {
		this.courseHTMLService = new GaeJdoCatalogService(new JdoDAO());
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
	 	throws IOException {	 	
		
		 String strCourseId = req.getParameter("courseId");
		 String lang = req.getParameter("lang");
		 String from = req.getParameter("from");	 
		 if(strCourseId==null) {
			 throw new IOException("CourseId PARAM is Empty..");
		 } 
		 if(lang==null) {
			 lang="es";
		 }
		 
		 if("gsa".equals(from)) {
			 String redirectURL = "/" + lang + "/catalog" + "/course/redirect/" + strCourseId;
			 LOGGER.severe("++Redirecting URL: " + redirectURL);
			 resp.sendRedirect(redirectURL);
		 }
		 
		 try {
			 Long courseId = Long.parseLong(strCourseId);
			 CourseCatalog course = this.courseHTMLService.getCourseCatalogByCourseId(courseId, new Locale(lang));
			 req.setAttribute("course", course);
			 req.getRequestDispatcher("/catalog2/course_detail.jsp").forward(req, resp); 
		 } catch(Exception e) {
			 throw new IOException(e);
		 }
	 }

	

}