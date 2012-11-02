<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<h1><fmt:message key="course.detail.section.header"/></h1>
<input id="eId" type="hidden" value="${course.externalId}">
<p><strong><fmt:message key="course.fields.ownedSchool"/></strong>: <a href="<c:url value='/${rc.locale.language}/school/detail/${school.id}/'/>" title="${school.name}">${school.name}</a></p>
<p><strong><fmt:message key="course.fields.title"/></strong>: ${course.title}</p>
<p><strong><fmt:message key="course.fields.information"/></strong>: ${course.information.value}</p>
<p><strong><fmt:message key="course.fields.url"/></strong>: <a href="${course.url}" target="_blank">${course.url}</a></p>
<p><strong><fmt:message key="course.fields.start" /></strong>: <fmt:formatDate value="${course.start}" type="date" pattern="dd-MM-yyyy" /></p>
<p><strong><fmt:message key="course.fields.end" /></strong>: <fmt:formatDate value="${course.end}" type="date" pattern="dd-MM-yyyy" /></p>
<p><strong><fmt:message key="course.fields.tags" /></strong>: <c:forEach items="${course.tags}" var="tag">${tag.category},</c:forEach></p> 