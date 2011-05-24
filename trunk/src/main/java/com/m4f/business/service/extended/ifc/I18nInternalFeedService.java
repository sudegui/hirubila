package com.m4f.business.service.extended.ifc;

import com.m4f.business.domain.MediationService;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.extended.ExtendedSchool;
import com.m4f.business.domain.extended.FeedCourses;
import com.m4f.business.domain.extended.FeedSchools;


public interface I18nInternalFeedService {
	FeedSchools createFeedSchools(String contextPath, Provider provider, MediationService mediationService) throws Exception;
	FeedCourses createFeedCourses(String contextPath, Provider provider, MediationService mediationService, ExtendedSchool school) throws Exception;
	
	void saveFeedSchools(FeedSchools feed) throws Exception;
	void saveFeedCourses(FeedCourses feed) throws Exception;
	
	FeedSchools getLastFeedSchools(Long providerId) throws Exception;
	FeedCourses getLastFeedCourses(Long providerId, Long extendedSchoolId) throws Exception;
	
	
}
