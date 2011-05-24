<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<h1><fmt:message key="territorial.section.hedader"/></h1>

<ul>
       <li>
       		<a href="<c:url value='/${rc.locale.language}/territorial/province/list'/>" title="Schools">
       		<fmt:message key="territorial.provinces"/></a>
       	</li>
       <li>
       		<a href="<c:url value='/${rc.locale.language}/territorial/region/list'/>" title="Courses"><fmt:message key="territorial.regions"/></a>
       	</li>
	   <li><a href="<c:url value='/${rc.locale.language}/territorial/town/list'/>" title="Courses"><fmt:message key="territorial.towns"/></a></li>
</ul>
