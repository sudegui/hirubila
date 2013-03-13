<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>
		
		<h1><fmt:message key="dumperParseError.list.section.header"/> (<fmt:message key="message.total.upper"/> ${paginator.size})</h1>
		
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
					<td>
						<c:choose>
							<c:when test="${error.entityClass == 'com.m4f.business.domain.Provider'}">
								<a href="<c:url value='/${rc.locale.language}/provider/detail/${error.entityId}'/>" title="detalle" target="_blank"><fmt:message key="${error.entityClass}"/></a>
							</c:when>
							<c:when test="${error.entityClass == 'com.m4f.business.domain.School'}">
								<a href="<c:url value='/${rc.locale.language}/school/detail/${error.entityId}'/>" title="detalle" target="_blank"><fmt:message key="${error.entityClass}"/></a>
							</c:when>
						</c:choose>
					</td>			
					<td><fmt:formatDate value="${error.when}" type="date" pattern="dd-MM-yyyy hh:mm:ss" /></td>
					<td class="description-collapse">${error.cause.value}</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
