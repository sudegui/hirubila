package com.m4f.utils.worker.impl;

import java.util.Map;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;
import com.m4f.utils.worker.ifc.IWorker;

public class GaeTaskWorker implements IWorker{

	@Override
	public void addWork(String workerId, String url, Map<String, String> params) {
		Queue queue = QueueFactory.getQueue(workerId);
		String urlTask = new StringBuffer("/task/catalog/create").toString();
		TaskOptions options = TaskOptions.Builder.withUrl(urlTask);
		options.method(Method.POST);
		for(String name : params.keySet()) {
			options.param(name, params.get(name));
		}
		queue.add(options);
	}

}