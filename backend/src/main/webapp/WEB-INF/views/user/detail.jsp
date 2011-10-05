<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
		
		<h1><fmt:message key="user.detail.section.header"/></h1>
		
		
		<b><fmt:message key="user.fields.email"/></b>  : ${user.email} <br/>
		<b><fmt:message key="user.fields.password"/></b>  : ${user.password} <br/>
		<b><fmt:message key="user.fields.admin"/></b>  : ${user.admin} <br/>
		<c:if test="${!user.admin}">
			<b><fmt:message key="user.mediator.fields.mediationService"/></b> : 
				<a href="<c:url value='/${rc.locale.language}/mediation/detail/${mediationService.id}/'/>" title="${mediationService.name}">
					${mediationService.name}</a>
			
		</c:if>
		
