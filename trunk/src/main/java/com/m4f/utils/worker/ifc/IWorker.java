package com.m4f.utils.worker.ifc;

import java.util.Map;

public interface IWorker {

	void addWork(String workId, String url, Map<String, String> params);
	
}