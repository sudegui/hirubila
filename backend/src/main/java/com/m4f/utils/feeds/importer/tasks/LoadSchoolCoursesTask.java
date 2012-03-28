package com.m4f.utils.feeds.importer.tasks;


/*@SuppressWarnings("serial")
public class LoadSchoolCoursesTask extends SpringContextAwareTask implements DeferredTask {
	private static final Logger LOGGER = Logger.getLogger(LoadSchoolCoursesTask.class.getName());
	
	@Autowired 
	SchoolImporter schoolImporter;
	
	private Provider provider;
	private School school;
	
	
	public LoadSchoolCoursesTask(Provider p, School s) {
		this.provider = p;
		this.school = s;	
	}
	
	@Override
	public void run() {
		try {
			schoolImporter.importCourses(provider, school, null);
		} catch(Exception e) {
			LOGGER.severe(StackTraceUtil.getStackTrace(e));
		}
	}

}*/
