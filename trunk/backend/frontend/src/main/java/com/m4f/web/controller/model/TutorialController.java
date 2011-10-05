package com.m4f.web.controller.model;

import java.util.Locale;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/tutorial")
public class TutorialController {
	
	@RequestMapping(value="/detail/{courseId}", method=RequestMethod.GET)
	public String detail(@PathVariable Long tutorialId, Model model, Locale locale) {
		
		return "";
	}


}