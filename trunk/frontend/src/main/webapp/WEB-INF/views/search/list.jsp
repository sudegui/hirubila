<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>
	
<h1><fmt:message key="search.phrases.section.header"/> (<fmt:message key="message.total.upper"/> ${fn:length(phrases)})</h1>
<hr>
<table border="1">
	<tr>
		<th></th>
		<th><fmt:message key="search.phrases.fields.phrase" /></th>
		<th><fmt:message key="search.phrases.fields.language" /></th>
		<th><fmt:message key="search.phrases.fields.date" /></th>
	</tr>
	<c:forEach items="${phrases}" var="phrase">
	<tr>
		<td>${phrase.id}</td>			
		<td>${phrase.phrase}</td>
		<td>${phrase.language}</td>
		<td>${phrase.date}</td>
	</tr>
</c:forEach>
</table>
		
