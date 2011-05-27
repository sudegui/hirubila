<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<div id="menubar">
        <ul id="menu">
         	<!-- put class="selected" in the li tag for the selected page - to highlight which page you're on -->
         	        		
	    	<sec:authorize ifAllGranted="ROLE_ADMIN">
	    		<li id="dashboardMenu">
	    			<a href="<c:url value='/${rc.locale.language}/dashboard/admin'/>" title='<fmt:message key="website.section.homepage"/>'>
	    				<fmt:message key="website.section.homepage"/>
	    			</a>
	    		</li>
	    		<li id="catalogMenu">
	    			<a href="<c:url value='/${rc.locale.language}/dashboard/admin/catalog'/>" title='<fmt:message key="site.sections.profile"/>'>
	    				<fmt:message key="site.sections.mycatalog"/>
	    			</a>
	    		</li>
	    		<li id="territorialMenu">
	    			<a href="<c:url value='/${rc.locale.language}/dashboard/admin/territorial'/>" title='<fmt:message key="site.sections.territorial"/>'>
	    				<fmt:message key="site.sections.territorial"/>
	    			</a>
	    		</li>
	    		<li id="suggestionMenu">
	    			<a href="<c:url value='/${rc.locale.language}/dashboard/admin/suggestions'/>" title='<fmt:message key="suggestion.inbox.section.title"/>'>
	    				<fmt:message key="suggestion.inbox.section.title"/>
	    			</a>
	    		</li>
	    		<li id="processMenu">
	    			<a href="<c:url value='/${rc.locale.language}/summary/crontaskreport'/>" title='<fmt:message key="site.sections.process"/>'>
	    				<fmt:message key="site.sections.process"/>
	    			</a>
	    		</li>
	    		<li id="configurationMenu">
	    			<a href="<c:url value='/${rc.locale.language}/dashboard/admin/configuration'/>" title='<fmt:message key="site.sections.configuration"/>'>
	    				<fmt:message key="site.sections.configuration"/>
	    			</a>
	    		</li>
	    		<!-- 
	    		<li id="providerMenu"><a href="<c:url value='/${rc.locale.language}/provider/list'/>" title="Providers"><fmt:message key="site.sections.providers"/>
	    		</a></li>
	    		<li id="userMenu"><a href="<c:url value='/${rc.locale.language}/user/list'/>" title="Users"><fmt:message key="site.sections.users"/></a></li>
	    		<li id="suggestionMenu"><a href="<c:url value='/${rc.locale.language}/suggestion/list'/>" title="Inbox"><fmt:message key="site.sections.inbox"/></a></li>
	    		<li id="schoolMenu"><a href="<c:url value='/${rc.locale.language}/school/list'/>" title="Schools"><fmt:message key="site.sections.schools"/></a></li>
	    		<li id="courseMenu"><a href="<c:url value='/${rc.locale.language}/course/list'/>" title="Courses"><fmt:message key="site.sections.courses"/></a></li>
	    		<li id="territorialMenu"><a href="<c:url value='/${rc.locale.language}/territorial'/>" title="Territorial"><fmt:message key="site.sections.territorial"/></a></li>
	    		 
	    			<li id="dumpMenu"><a href="<c:url value='/${rc.locale.language}/dump'/>" title="Dumps"><fmt:message key="site.sections.dumps"/></a></li>
	    			<li id="mediationMenu"><a href="<c:url value='/${rc.locale.language}/mediation/list'/>" title="Schools">Mediacion</a></li>
	    		
	    		<li id="i18nMenu"><a href="<c:url value='/${rc.locale.language}/i18n'/>" title="Schools"><fmt:message key="site.sections.i18n"/></a></li>
	    		-->
	    	</sec:authorize>
	    	
	    	<sec:authorize ifAnyGranted="ROLE_MANUAL_MEDIATOR,ROLE_AUTOMATIC_MEDIATOR">
	    		<li id="dashboardMenu"><a href="<c:url value='/${rc.locale.language}/dashboard/mediator'/>" title='dashboard'><fmt:message key="website.section.homepage"/></a></li>
	    		<li id="catalogMenu"><a href="<c:url value='/${rc.locale.language}/dashboard/mediator/catalog'/>" title='<fmt:message key="site.sections.profile"/>'>
	    		<fmt:message key="site.sections.mycatalog"/></a>
	    		</li>
	    		<li id="tutorialsMenu"><a href="<c:url value='/${rc.locale.language}/dashboard/mediator/tutorials'/>" title='<fmt:message key="site.sections.tutorials"/>'>
	    		<fmt:message key="site.sections.tutorials"/></a>
	    		</li>
	    		<!--  
		    	<li id="profileMenu"><a href="<c:url value='/${rc.locale.language}/dashboard/mediator/profile'/>" title='<fmt:message key="site.sections.profile"/>'><fmt:message key="site.sections.profile"/></a></li>
		    	-->
			</sec:authorize>

			<sec:authorize ifAnyGranted="ROLE_AUTOMATIC_MEDIATOR">
				<li id="summaryMenu"><a href="<c:url value='/${rc.locale.language}/summary'/>" title='<fmt:message key="site.sections.summary"/>'>
	    		<fmt:message key="site.sections.summary"/></a>
	    		</li>
			</sec:authorize>
	    		    	
	    	<sec:authorize access="!isAuthenticated()">
	    		<li id="homeMenu"><a href="<c:url value='/${rc.locale.language}/'/>"><fmt:message key="website.section.homepage"/></a></li>
   				<li id="contactMenu"><a href="<c:url value='/${rc.locale.language}/contact'/>" title="Contact"><fmt:message key="website.section.contactpage"/></a></li>
			</sec:authorize>
    		
    		<!--  
    		<li><a href="<c:url value='/${rc.locale.language}/i18n/delete/all'/>" title="delete all">Delete all i18N - 2</a></li>
    		-->
        </ul>
</div>
