<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<h1><fmt:message key="course.detail.section.header"/></h1>

<p><strong><fmt:message key="course.fields.ownedSchool"/></strong>: ${school.name} &nbsp;&nbsp; <a href="<c:url value='/${rc.locale.language}/extended/public/school/${school.id}'/>">Ver datos del centro</a></p>
<p><strong><fmt:message key="course.fields.title"/></strong>: ${course.title}</p>
<p><strong><fmt:message key="course.fields.information"/></strong>: ${course.information.value}</p>
<p><strong><fmt:message key="course.fields.start" /></strong>: <fmt:formatDate value="${course.start}" type="date" pattern="dd-MM-yyyy" /></p>
<p><strong><fmt:message key="course.fields.end" /></strong>: <fmt:formatDate value="${course.end}" type="date" pattern="dd-MM-yyyy" /></p>
<p><strong><fmt:message key="course.fields.timetable" /></strong>: ${course.timeTable}</p>
<p><strong><fmt:message key="course.fields.free" /></strong>: ${course.free}</p>
<p><strong><fmt:message key="course.fields.billMode" /></strong>: ${course.billMode}</p>
<p><strong><fmt:message key="course.fields.languages" /></strong>: <c:forEach items="${course.languages}" var="tag">${tag.category},</c:forEach></p>
<p><strong><fmt:message key="course.fields.tags" /></strong>: <c:forEach items="${course.tags}" var="tag">${tag.category},</c:forEach></p>