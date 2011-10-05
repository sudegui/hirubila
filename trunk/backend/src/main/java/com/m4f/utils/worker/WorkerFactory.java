package com.m4f.utils.worker;

import com.m4f.utils.worker.ifc.IWorker;

public class WorkerFactory {
	
	private IWorker worker = null;
	
	public WorkerFactory(IWorker w) {
		this.worker = w;
	}
	
	public IWorker createWorker() {
		return this.worker;
	}
		
}