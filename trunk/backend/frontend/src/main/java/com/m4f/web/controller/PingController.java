package com.m4f.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/ping")
public class PingController extends BaseController {
	@RequestMapping(method=RequestMethod.GET)
	public void getIndex(HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html");
	 	resp.getWriter().println("Ping ping");
	}
}
