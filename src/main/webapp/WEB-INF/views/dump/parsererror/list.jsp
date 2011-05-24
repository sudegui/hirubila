<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>
		
		<h1><fmt:message key="error.list.section.header"/> (<fmt:message key="message.total.upper"/> ${fn:length(errors)})</h1>
		
		<a href="<c:url value='/${rc.locale.language}/parsererror/delete/all'/>" title="delete all">
							<fmt:message key="business.action.deleteall"/>
		</a>
		
		<table style="width:100%; border-spacing:0;">
			<tr>
				<th>Entity Class</th>			
				<th>Entity ID</th>
				<th>Time</th>
				<th>Cause</th>
				<th>Operations</th>
			</tr>
			<c:forEach items="${errors}" var="error">
				<tr>
					<td>${error.entityClass}</td>			
					<td>${error.entityId}</td>
					<td>${error.when}</td>
					<td class="description-collapse">${error.cause.value}</td>
					<td>
						<ul>
							<li><a href="<c:url value='/${rc.locale.language}/parsererror/delete/${error.id}'/>" title="${error.id}">
								<fmt:message key="school.action.delete"/></a></li>
						</ul>
						
					</td>
				</tr>
			</c:forEach>
		</table>
