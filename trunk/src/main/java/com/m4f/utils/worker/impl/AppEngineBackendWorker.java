package com.m4f.utils.worker.impl;

import java.net.MalformedURLException;
import java.util.Map;
import com.google.appengine.api.backends.BackendServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;
import com.m4f.utils.worker.ifc.IWorker;
import java.util.concurrent.ExecutionException;

public class AppEngineBackendWorker implements IWorker {

	@Override
	public void addWork(String workId, String url, Map<String, String> params) 
		throws MalformedURLException, InterruptedException, ExecutionException {
		Queue queue = QueueFactory.getQueue(workId);
		TaskOptions options = TaskOptions.Builder.withUrl(url);
		options.method(Method.POST);
		for(String name : params.keySet()) {
			options.param(name, params.get(name));
		}
		options.header("Host", BackendServiceFactory.getBackendService().getBackendAddress("worker"));
		queue.add(options);    	 
	}
	

}