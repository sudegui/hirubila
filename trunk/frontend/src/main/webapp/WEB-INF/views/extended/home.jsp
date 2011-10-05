<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<h1><fmt:message key="user.mediator.home.section.header"/></h1>

<ul>
       <li><a href="<c:url value='/${rc.locale.language}/extended/school/list'/>" title="<fmt:message key='sidebar.ownedSchools'/>"><fmt:message key="sidebar.ownedSchools"/></a></li>
       <li><a href="<c:url value='/${rc.locale.language}/extended/course/list'/>" title="<fmt:message key='sidebar.ownedCourses'/>"><fmt:message key="sidebar.ownedCourses"/></a></li>

</ul>
