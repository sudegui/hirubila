<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<h1><fmt:message key="catalog.index.section.header"/></h1>

<a href="<c:url value='/${rc.locale.language}/catalog/reglated/course/list'/>" title="<fmt:message key='catalog.index.links.courses'/>">
	<fmt:message key="catalog.index.reglated.courses"/>
</a>

<br/>

<a href="<c:url value='/${rc.locale.language}/catalog/non-reglated/course/list'/>" title="<fmt:message key='catalog.index.links.courses'/>">
	<fmt:message key="catalog.index.nonreglated.courses"/>
</a>
		
	
		