package com.m4f.utils.worker.ifc;

import java.util.Map;

public interface IWorker {

	void addWork(String workerId, String url, Map<String, String> params);
	
}