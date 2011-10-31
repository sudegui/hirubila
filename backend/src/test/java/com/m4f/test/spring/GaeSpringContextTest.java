package com.m4f.test.spring;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.m4f.test.gae.GaeContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
//ApplicationContext will be loaded from "/applicationContext.xml" and "/applicationContext-test.xml"
//in the root of the classpath
@ContextConfiguration({"/app-config-test.xml"})
public class GaeSpringContextTest extends GaeContextLoader {

}
