package com.m4f.utils.worker.ifc;

import java.net.MalformedURLException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface IWorker {

	void addWork(String workId, String url, Map<String, String> params) throws MalformedURLException, InterruptedException, ExecutionException;
	
}