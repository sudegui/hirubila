<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<meta http-equiv="Content-Language" content="${rc.locale.language}">
<meta name="title" content="<spring:escapeBody htmlEscape="true">${course.title} - (${school.name})</spring:escapeBody>"> 
<meta name="description" content="<spring:escapeBody htmlEscape="true">${tags}</spring:escapeBody>"> 
<meta name="keywords" content="<spring:escapeBody htmlEscape="true">${tags}</spring:escapeBody>"> <%-- COURSE's meta to use with GSA --%>
<meta name="cTitle" content="<spring:escapeBody htmlEscape="true">${course.title}</spring:escapeBody>" >
<meta name="cTopics" content="<spring:escapeBody htmlEscape="true">${tags}</spring:escapeBody>" >
<meta name="cStart" content="<fmt:formatDate value="${course.start}" type="date" pattern="dd-MM-yyyy" />" >
<meta name="cEnd" content="<fmt:formatDate value="${course.end}" type="date" pattern="dd-MM-yyyy" />" >
<meta name="cProvince" content="<spring:escapeBody htmlEscape="true">${province.name}</spring:escapeBody>" >
<meta name="cRegion" content="<spring:escapeBody htmlEscape="true">${region.name}</spring:escapeBody>" >
<meta name="cTown" content="<spring:escapeBody htmlEscape="true">${town.name}</spring:escapeBody>">