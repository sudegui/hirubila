<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<div id="providerInfo">
<strong>Curso facilitado por:</strong>
<spring:escapeBody htmlEscape="true">${course.providerName}</spring:escapeBody> <br/>
</div>

<h1><spring:escapeBody htmlEscape="true">${course.title}</spring:escapeBody></h1>

<div id="information">
	<%--<spring:escapeBody javaScriptEscape="true">${course.information.value}</spring:escapeBody>--%>
	${course.information.value}
</div>
<br/>
<div id="tags">
	<strong><fmt:message key="course.fields.tags" /></strong>
	<spring:escapeBody htmlEscape="true">${course.tags}</spring:escapeBody>
</div>
<br/>


<c:if test="${not empty course.url}">
	<div id="url">
		<strong><fmt:message key="course.fields.url" /></strong>
		<a href="${course.url}" target="_blank">${course.url}</a>
	</div>
</c:if>

<!--  
<c:if test="${course.regulated == true}">
<input type="checkbox" disabled="disabled" checked="checked"/>
</c:if>
<c:if test="${course.regulated == false}">
<input type="checkbox" disabled="disabled"/>
</c:if>
Reglado
 <br/>
-->
<div id="schoolInfo">
	<strong><fmt:message key="course.fields.school" />:</strong>
	<spring:escapeBody htmlEscape="true">${course.schoolName}</spring:escapeBody> <br/>
	
	<h3><fmt:message key="school.fields.contacInfo"/>:</h3>
		<fmt:message key="contactInfo.fields.city"/>  : ${course.schoolInfo.city} <br/>
		<fmt:message key="contactInfo.fields.country"/>  : ${course.schoolInfo.country} <br/>
		<fmt:message key="contactInfo.fields.telephone"/> : ${course.schoolInfo.telephone} <br/>
		<fmt:message key="contactInfo.fields.fax"/>  : ${course.schoolInfo.fax} <br/>
		<fmt:message key="contactInfo.fields.zipCode"/>  : ${course.schoolInfo.zipCode} <br/>
		<fmt:message key="contactInfo.fields.email"/>  : ${course.schoolInfo.email} <br/>
		<fmt:message key="contactInfo.fields.streetAddress"/> : ${course.schoolInfo.streetAddress} <br/>
		<fmt:message key="contactInfo.fields.webSite"/> : ${course.schoolInfo.webSite} <br/>
</div>
