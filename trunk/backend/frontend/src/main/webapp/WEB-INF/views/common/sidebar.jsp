<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<!-- insert your sidebar items here 
        <h3>Latest News</h3>
        <h4>New Website Launched</h4>
        <h5>January 1st, 2010</h5>
        <p>2010 sees the redesign of our website. Take a look around and let us know what you think.<br /><a href="#">Read more</a></p>
        <p></p>
        <h4>New Website Launched</h4>
        <h5>January 1st, 2010</h5>
        <p>2010 sees the redesign of our website. Take a look around and let us know what you think.<br /><a href="#">Read more</a></p>
        -->
        
        <sec:authorize ifAllGranted="ROLE_AUTOMATIC_MEDIATOR">
   			<h3><fmt:message key="usefullinks.message"/></h3>
        		<ul>
        			<li><a href="<c:url value='/${rc.locale.language}/dashboard/mediator/catalog/schools'/>" title="<fmt:message key='sidebar.ownedSchool'/>"><fmt:message key="sidebar.ownedSchools"/></a></li>
        			<li><a href="<c:url value='/${rc.locale.language}/dashboard/mediator/catalog/courses/'/>" title="<fmt:message key='sidebar.ownedCourses'/>"><fmt:message key="sidebar.ownedCourses"/></a></li>
        			<li><a href="<c:url value='/${rc.locale.language}/suggestion'/>" title="<fmt:message key='sidebar.sendSuggestion'/>"><fmt:message key="sidebar.sendSuggestion"/></a></li>
        		</ul>
		</sec:authorize>
		<sec:authorize ifAllGranted="ROLE_MANUAL_MEDIATOR">
   			<h3><fmt:message key="usefullinks.message"/></h3>
        		<ul>
        			<li><a href="<c:url value='/${rc.locale.language}/dashboard/mediator/catalog/extended/schools'/>" title="<fmt:message key='sidebar.ownedSchool'/>"><fmt:message key="sidebar.ownedSchools"/></a></li>
        			<li><a href="<c:url value='/${rc.locale.language}/dashboard/mediator/catalog/extended/courses'/>" title="<fmt:message key='sidebar.ownedCourses'/>"><fmt:message key="sidebar.ownedCourses"/></a></li>
        			<li><a href="<c:url value='/${rc.locale.language}/suggestion'/>" title="<fmt:message key='sidebar.sendSuggestion'/>"><fmt:message key="sidebar.sendSuggestion"/></a></li>
        		</ul>
		</sec:authorize>
			
        
        
			
        