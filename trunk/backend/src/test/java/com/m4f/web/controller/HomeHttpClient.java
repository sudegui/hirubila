package com.m4f.web.controller;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.HttpMethod;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import com.m4f.test.spring.GaeSpringContextTest;

@Ignore
public class HomeHttpClient  {
	

	@Test
	public void testHome() throws HttpException, IOException {
		HttpClient httpclient = new HttpClient();
		HttpMethod method = new GetMethod("http://localhost:8889/es/");
		httpclient.executeMethod(method);
		System.out.println("Status code: " + method.getStatusCode());
	}
	
    public void createProviderCatalog() throws HttpException, IOException {
    	HttpClient httpclient = new HttpClient();
    	PostMethod post = new PostMethod("http://localhost:8888/es/catalog/provider/create");
    	post.addRequestHeader("accept", "application/json");
    	NameValuePair[] data = {new NameValuePair("providerId", "684")};
    	post.setRequestBody(data);        
    	httpclient.executeMethod(post);
        String resultado = post.getResponseBodyAsString();
        System.out.println(resultado);
    }
  
    
    
}