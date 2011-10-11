package com.m4f.backend.client;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;


public class CatalogHttpClient {
	
	@Before
    public void setUp() {
       
    }

    @After
    public void tearDown() {
        
    }
    
  
    public void generateAll() throws HttpException, IOException {
    	HttpClient httpclient = new HttpClient();
    	HttpMethod method = new PostMethod("http://localhost:8888/es/catalog/create");
    	method.addRequestHeader("accept", "application/json");
    	httpclient.executeMethod(method);
        String resultado = method.getResponseBodyAsString();
        System.out.println(resultado);
    }
    
    @Test
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