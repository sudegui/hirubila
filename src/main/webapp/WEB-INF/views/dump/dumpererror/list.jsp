<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<h1><fmt:message key="dumpererror.list.section.header"/> (<fmt:message key="message.total.upper"/> ${paginator.size})</h1>

<%@ include file="/WEB-INF/views/common/paginator-tables.jsp"%>	
		
<table style="width:100%; border-spacing:0;">
	<thead>
		<tr>
			<th><fmt:message key="dump.parseError.fields.origen"/></th>			
			<th><fmt:message key="dump.parseError.fields.date"/></th>
			<th><fmt:message key="dump.parseError.fields.cause"/></th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${paginator.collection}" var="error">
		<tr>
			<td><fmt:message key="${error.entityClass}"/></td>			
			<td><fmt:formatDate value="${error.when}" type="date" pattern="dd-MM-yyyy hh:mm:ss" /></td>
			<td class="description-collapse">${error.cause.value}</td>
		</tr>
	</c:forEach>
	</tbody>
</table>