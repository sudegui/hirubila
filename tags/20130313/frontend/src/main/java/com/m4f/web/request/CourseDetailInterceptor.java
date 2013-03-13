package com.m4f.web.request;

import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import com.m4f.business.service.ifc.IServiceLocator;

public class CourseDetailInterceptor extends HandlerInterceptorAdapter  {
	
	private static final Logger LOGGER = Logger.getLogger(CourseDetailInterceptor.class.getName());
	@Autowired
	protected IServiceLocator serviceLocator;
	
	public void	afterCompletion(HttpServletRequest request, 
			HttpServletResponse response, Object handler, Exception ex) throws Exception {
		LOGGER.severe("After completion.");
		super.afterCompletion(request, response, handler, ex);
	}
    
	public void	postHandle(HttpServletRequest request, 
			HttpServletResponse response, Object handler, 
			ModelAndView modelAndView) throws Exception {
		LOGGER.severe("Post handle");
		super.postHandle(request, response, handler, modelAndView);
	}
    
	public boolean preHandle(HttpServletRequest request, 
			HttpServletResponse response, Object handler) {
		return true;
	}

}
