package com.m4f.web.controller;

import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.m4f.business.domain.Provider;

@Controller
@RequestMapping("/home")
public class HomeController extends BaseController {
	
	public HomeController() {
		super();
	}
	
	@RequestMapping(method=RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void getHome(Locale locale) throws Exception {
		Provider provider = providerService.createProvider();
		provider.setName("Proveedor local");
		provider.setFeed("http://localhost/feeds/zentruak_test.xml");
		providerService.save(provider, new Locale("es"));
		provider = providerService.createProvider();
		provider.setName("Tolosaldea provider");
		provider.setFeed("http://www.ikastn.com/feed/ikasgida/tolosaldea.php");
		providerService.save(provider, new Locale("es"));
		provider = providerService.createProvider();
		provider.setName("Mungialdea provider");
		provider.setFeed("http://www.ikasizhazi.com/feeds/mungialdea.xml");
		providerService.save(provider, new Locale("es"));
		provider = providerService.createProvider();
		provider.setName("Durangaldea provider");
		provider.setFeed("http://hirubila.appspot.com/es/extended/public/school/feed/290008");
		providerService.save(provider, new Locale("es"));
	}
	
	
}