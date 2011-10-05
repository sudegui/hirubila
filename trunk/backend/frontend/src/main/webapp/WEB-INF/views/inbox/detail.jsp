<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<h1><fmt:message key="suggestion.detail.section.header"/></h1>
		
<fmt:message key="suggestion.fields.from"/>  : ${inbox.from} <br/>
<fmt:message key="suggestion.fields.type"/>  : ${inbox.type} <br/>
<fmt:message key="suggestion.fields.created"/>  : ${inbox.created} <br/>
<fmt:message key="suggestion.fields.content" /> : ${inbox.content} <br/>
<fmt:message key="suggestion.fields.readed" /> : ${inbox.readed} <br/>
<fmt:message key="suggestion.fields.active" /> : ${inbox.active} <br/>
<fmt:message key="suggestion.fields.remoteHost" /> : ${inbox.remoteHost} <br/>
<fmt:message key="suggestion.fields.userType" /> : ${inbox.user} <br/>

<br/>

<a href="<c:url value='/${rc.locale.language}/suggestion/response/${inbox.id}'/>" title='<fmt:message key="suggestion.action.response"/>'>
	<fmt:message key="suggestion.action.response"/>
</a>