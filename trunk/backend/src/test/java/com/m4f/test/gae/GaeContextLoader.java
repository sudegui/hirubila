package com.m4f.test.gae;

import org.junit.After;
import org.junit.Before;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalTaskQueueTestConfig;


public class GaeContextLoader {
	
	private LocalServiceTestHelper helper;
	
	@Before
	public void setUp() {
		LocalDatastoreServiceTestConfig datastore = new LocalDatastoreServiceTestConfig();
		datastore.setNoStorage(false);
		datastore.setStoreDelayMs(10);
		helper =  new  LocalServiceTestHelper(datastore);
		helper.setUp();		
	}
	
	@After
    public void tearDown() {
        helper.tearDown();
    }
	
}