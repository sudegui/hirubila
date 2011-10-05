<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp" %>
<h1><fmt:message key="error.403.section.header"/></h1>
<img src="<c:url value='/static/img/403-error.png'/>" alt="error"/>
<fmt:message key="error.403.message"/>