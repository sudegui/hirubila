package com.m4f.test.gae;

import org.junit.After;
import org.junit.Before;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalTaskQueueTestConfig;


public class GaeContextLoader {
	
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
		     new LocalDatastoreServiceTestConfig());
	
	@Before
	public void setUp() {
		helper.setUp();		
	}
	
	@After
    public void tearDown() {
        helper.tearDown();
    }
	
}