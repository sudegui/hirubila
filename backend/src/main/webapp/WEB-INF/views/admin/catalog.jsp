<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<h3><fmt:message key="catalog.source.info"/></h3>
<ul>

	<li><a href="<c:url value='/${rc.locale.language}/dashboard/admin/catalog/providers/'/>" title="<fmt:message key='catalog.providers'/>"><fmt:message key="catalog.providers"/></a></li>
	<li><a href="<c:url value='/${rc.locale.language}/dashboard/admin/catalog/mediations/'/>" title="<fmt:message key='catalog.mediationService'/>"><fmt:message key="catalog.mediationService"/></a></li>
	<li><a href="<c:url value='/${rc.locale.language}/dashboard/admin/catalog/users/'/>" title="<fmt:message key='catalog.systemUsers'/>"><fmt:message key="catalog.systemUsers"/></a></li>
</ul>

<h3><fmt:message key="catalog.source.info.automatic"/></h3>
<ul>

	<li><a href="<c:url value='/${rc.locale.language}/dashboard/admin/catalog/schools/'/>" title="<fmt:message key='catalog.schools'/>"><fmt:message key="catalog.schools"/></a></li>
	<li><a href="<c:url value='/${rc.locale.language}/dashboard/admin/catalog/courses/'/>" title="<fmt:message key='catalog.courses'/>"><fmt:message key="catalog.courses"/></a></li>
</ul>


<h3><fmt:message key="catalog.source.info.manual"/></h3>
<ul>
	<li><a href="<c:url value='/${rc.locale.language}/dashboard/admin/catalog/extended/schools/'/>" title="<fmt:message key='catalog.schools'/>"><fmt:message key="catalog.schools"/></a></li>
    <li><a href="<c:url value='/${rc.locale.language}/dashboard/admin/catalog/extended/courses/'/>" title="<fmt:message key='catalog.courses'/>"><fmt:message key="catalog.courses"/></a></li>
</ul>