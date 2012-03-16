<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>
<h1><fmt:message key="provider.detail.section.header"/></h1>
		
		<h4><fmt:message key="provider.information.basic"/></h4>
		<ul>
			<li>${provider.name}</li>
			<li>${provider.feed}</li>
		</ul>