package com.m4f.utils.feeds.parser.impl;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import com.m4f.test.loader.DataLoader;

@Ignore
@RunWith(Suite.class)
@Suite.SuiteClasses({
		DataLoader.class,
		SchoolsFeedParserTest.class,
		CoursesFeedParserTest.class
})
public class ParserTest {

}