<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<h1><fmt:message key="user.mediator.home.section.header"/></h1>

<ul>
<sec:authorize ifAllGranted="ROLE_AUTOMATIC_MEDIATOR">
	<li><a href="<c:url value='/${rc.locale.language}/dashboard/mediator/catalog/schools/'/>" title="<fmt:message key='sidebar.ownedSchools'/>"><fmt:message key="sidebar.ownedSchools"/></a></li>
	<li><a href="<c:url value='/${rc.locale.language}/dashboard/mediator/catalog/courses/'/>" title="<fmt:message key='sidebar.ownedCourses'/>"><fmt:message key="sidebar.ownedCourses"/></a></li>
</sec:authorize>
<sec:authorize ifAllGranted="ROLE_MANUAL_MEDIATOR">
	<li><a href="<c:url value='/${rc.locale.language}/dashboard/mediator/catalog/extended/schools/'/>" title="<fmt:message key='sidebar.ownedSchools'/>"><fmt:message key="sidebar.ownedSchools"/></a></li>
    <li><a href="<c:url value='/${rc.locale.language}/dashboard/mediator/catalog/extended/courses/'/>" title="<fmt:message key='sidebar.ownedCourses'/>"><fmt:message key="sidebar.ownedCourses"/></a></li>
</sec:authorize>
</ul>