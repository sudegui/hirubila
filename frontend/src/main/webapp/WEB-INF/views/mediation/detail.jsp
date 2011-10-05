<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>
<h1><fmt:message key="mediation.detail.section.header"/></h1>


<%-- Miembros del servicio de mediación en curso --%>
<h3><fmt:message key="mediation.members.message"/>:</h3>
<ul>
	<c:forEach items="${members}" var="member">
		<li><a href="<c:url value="/${rc.locale.language}/user/detail/${member.id}/"/>" >${member.name} (${member.email})</a></li>
	</c:forEach>		
</ul>

<h3><fmt:message key="basicInfo.title"/></h3>
<p><strong><fmt:message key="user.mediator.fields.name"/></strong>: ${mediationService.name}</p>
<p><strong><fmt:message key="user.mediator.fields.entity"/></strong>: ${mediationService.entity}</p>
<p><strong><fmt:message key="user.fields.hasFeed"/></strong>: <form:checkbox path="mediationService.hasFeed" id="has_feed" disabled="true"/></p>

<h3><fmt:message key="contactInfo.title"/></h3>

<p><strong><fmt:message key="contactInfo.fields.telephone"/></strong>: ${mediationService.contactInfo.telephone}</p>
<p><strong><fmt:message key="contactInfo.fields.fax"/></strong>: ${mediationService.contactInfo.fax}</p>
<p><strong><fmt:message key="contactInfo.fields.country"/></strong>: ${mediationService.contactInfo.country}</p>
<p><strong><fmt:message key="contactInfo.fields.city"/></strong>:${mediationService.contactInfo.city}</p>
<p><strong><fmt:message key="contactInfo.fields.zipCode"/></strong>:${mediationService.contactInfo.zipCode}</p>
<p><strong><fmt:message key="contactInfo.fields.streetAddress"/></strong>: ${mediationService.contactInfo.streetAddress}</p>
<p><strong><fmt:message key="contactInfo.fields.email"/></strong>: ${mediationService.contactInfo.email}</p>
<p><strong><fmt:message key="contactInfo.fields.webSite"/></strong>: ${mediationService.contactInfo.webSite}</p>

<h3><fmt:message key="geoLocation.title"/></h3>
<p><strong><fmt:message key="geolocation.fields.latitude"/></strong>: ${mediationService.geoLocation.latitude}</p>
<p><strong><fmt:message key="geolocation.fields.longitude"/></strong>: ${mediationService.geoLocation.longitude}</p>
