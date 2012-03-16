<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>

		<h1><fmt:message key="school.detail.section.header"/></h1>
		
		<hr>
		
		<h3><fmt:message key="school.information.message"/>:</h3>
		<fmt:message key="school.fields.name"/>  : ${school.name} <br/>
		<fmt:message key="school.fields.externalId"/>  : ${school.externalId} <br/>
		<fmt:message key="school.fields.feed"/>  : ${school.feed} <br/>
		<fmt:message key="school.fields.created"/>  : ${school.created} <br/>
		<fmt:message key="school.fields.updated"/>  : ${school.updated} <br/>
		
		<h3><fmt:message key="school.fields.contacInfo"/>:</h3>
		<fmt:message key="contactInfo.fields.city"/>  : ${school.contactInfo.city} <br/>
		<fmt:message key="contactInfo.fields.country"/>  : ${school.contactInfo.country} <br/>
		<fmt:message key="contactInfo.fields.telephone"/> : ${school.contactInfo.telephone} <br/>
		<fmt:message key="contactInfo.fields.fax"/>  : ${school.contactInfo.fax} <br/>
		<fmt:message key="contactInfo.fields.zipCode"/>  : ${school.contactInfo.zipCode} <br/>
		<fmt:message key="contactInfo.fields.email"/>  : ${school.contactInfo.email} <br/>
		<fmt:message key="contactInfo.fields.streetAddress"/> : ${school.contactInfo.streetAddress} <br/>
		<fmt:message key="contactInfo.fields.webSite"/> : ${school.contactInfo.webSite} <br/>
