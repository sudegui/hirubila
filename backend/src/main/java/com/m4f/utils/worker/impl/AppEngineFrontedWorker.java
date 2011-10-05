package com.m4f.utils.worker.impl;

import java.net.MalformedURLException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;
import com.m4f.utils.worker.ifc.IWorker;

public class AppEngineFrontedWorker implements IWorker {

	@Override
	public void addWork(String workId, String url, Map<String, String> params) 
		throws MalformedURLException, InterruptedException, ExecutionException {
		Queue queue = QueueFactory.getQueue(workId);
		TaskOptions options = TaskOptions.Builder.withUrl(url);
		options.method(Method.POST);
		for(String name : params.keySet()) {
			options.param(name, params.get(name));
		}
		queue.add(options);
	}
	
}