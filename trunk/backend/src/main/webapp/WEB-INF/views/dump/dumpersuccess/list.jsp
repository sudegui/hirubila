<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<h1><fmt:message key="dumpersuccess.list.section.header"/> (TOTAL ${paginator.size})</h1>
		
<%@ include file="/WEB-INF/views/common/paginator-tables.jsp"%>
			
<table style="width:100%; border-spacing:0;">
	<thead>
		<tr>
			<th><fmt:message key="dump.parseError.fields.origen"/></th>	
			<th><fmt:message key="dump.parseError.fields.date"/></th>
			<th><fmt:message key="dump.success.fields.language"/></th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${paginator.collection}" var="suc">
		<tr>					
			<td>
				<c:choose>
					<c:when test="${suc.entityClass == 'com.m4f.business.domain.Course'}">
						<a href="<c:url value='/${rc.locale.language}/course/detail/${suc.entityId}'/>" title="detalle" target="_blank"><fmt:message key="${suc.entityClass}"/></a>
					</c:when>
					<c:when test="${suc.entityClass == 'com.m4f.business.domain.School'}">
						<a href="<c:url value='/${rc.locale.language}/school/detail/${suc.entityId}'/>" title="detalle" target="_blank"><fmt:message key="${suc.entityClass}"/></a>
					</c:when>
				</c:choose>
			</td>
			<td><fmt:formatDate value="${suc.when}" type="date" pattern="dd-MM-yyyy hh:mm:ss" /></td>
			<td>${suc.language}</td>
		</tr>
	</c:forEach>
	</tbody>
</table>