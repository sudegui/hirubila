package com.m4f.utils.feeds.parser.impl;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.m4f.test.spring.GaeSpringContextTest;

public class CoursesFeedParserTester extends GaeSpringContextTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("setUpBeforeClass");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("tearDownAfterClass");
	}

	@Before
	public void setUp() {
		super.setUp();
		System.out.println("setUp");
	}

	@After
	public void tearDown() {
		super.tearDown();
		System.out.println("tearDown");
	}

	@Test
	public final void testGetCourses() {
		fail("Not yet implemented");
	}

}
