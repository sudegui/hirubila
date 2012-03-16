package com.m4f.web.controller;

import java.io.IOException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class ProviderHttpClient {
	
	@Test
	public void loadProviderFeed() throws HttpException, IOException {
    	HttpClient httpclient = new HttpClient();
    	PostMethod post = new PostMethod("http://localhost:8889/es/provider/feed");
    	NameValuePair[] data = {new NameValuePair("providerId", "7596")};
    	post.setRequestBody(data);        
    	httpclient.executeMethod(post);
        String resultado = post.getResponseBodyAsString();
        System.out.println(resultado);
    }
	
	@Test
	public void loadProviderSchools() throws HttpException, IOException {
    	HttpClient httpclient = new HttpClient();
    	PostMethod post = new PostMethod("http://localhost:8889/es/provider/schools");
    	NameValuePair[] data = {new NameValuePair("providerId", "7596")};
    	post.setRequestBody(data);        
    	httpclient.executeMethod(post);
        String resultado = post.getResponseBodyAsString();
        System.out.println(resultado);
    }
	
}