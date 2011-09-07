package com.m4f.utils.worker.impl;

import java.util.Map;
import com.google.appengine.api.backends.BackendService;
import com.google.appengine.api.backends.BackendServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;
import com.m4f.utils.worker.ifc.IWorker;

public class AppEngineBackendWorker implements IWorker {

	@Override
	public void addWork(String workId, String url, Map<String, String> params) {
		// TODO Auto-generated method stub+
		if(!url.startsWith("/")) {
			url = "/" + url;
		}
		Queue queue = QueueFactory.getQueue(workId);
		url = this.getBackendAddress() + url;
		TaskOptions options = TaskOptions.Builder.withUrl(url);
		options.method(Method.POST);
		for(String name : params.keySet()) {
			options.param(name, params.get(name));
		}
		queue.add(options);
	}
	
	
	private String getBackendAddress() {
		BackendService backendsApi = BackendServiceFactory.getBackendService();
		// Get the backend handling the current request.
		// Get the backend instance handling the current request.
		int currentInstance = backendsApi.getCurrentInstance();
		String backend = backendsApi.getCurrentBackend();
		String address = backendsApi.getBackendAddress(backend);
		System.out.println("Current BackednName: " + backend + 
				" instance: " + currentInstance + " address: " + address);
		return address;
	}

}