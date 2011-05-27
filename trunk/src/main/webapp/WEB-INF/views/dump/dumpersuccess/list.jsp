<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<h1><fmt:message key="dumpersuccess.list.section.header"/> (TOTAL ${paginator.size})</h1>
		
<a href="<c:url value='/${rc.locale.language}/feed/events/success/delete/all/${dump.id}'/>" title="delete all">
	<fmt:message key="business.action.deleteall"/>		
</a>

<c:if test="${paginator.size > 0}">
			<table>
				<tr>
					<c:if test="${paginator.currentPage != paginator.pageStart}">
						<td><a href="<c:url value='${paginator.urlBase}?page=${paginator.currentPage-1}'/>"><fmt:message key="pagination.before"/></a></td>
					</c:if>
					<c:forEach items='${paginator.pagesIterator}' var='page'>
      						<c:if test="${page == paginator.currentPage}">
      							<td>${page}</td>
      						</c:if>
      						<c:if test="${page != paginator.currentPage}">
      							<td><a href="<c:url value='${paginator.urlBase}?page=${page}'/>" title="Schools">${page}</a></td>
      						</c:if>
    				</c:forEach>
    				<c:if test="${paginator.currentPage != paginator.pageEnd}">
						<td><a href="<c:url value='${paginator.urlBase}?page=${paginator.currentPage+1}'/>"><fmt:message key="pagination.next"/></a></td>
					</c:if>
    			</tr>
    		</table>
    		
			<hr/>
</c:if>
			
<table style="width:100%; border-spacing:0;">
	<tr>
		<th><fmt:message key="dump.success.fields.identifier"/></th>
		<th><fmt:message key="dump.success.fields.entityClass"/></th>			
		<th><fmt:message key="dump.success.fields.entityId"/></th>
		<th><fmt:message key="dump.success.fields.time"/></th>
		<th><fmt:message key="dump.success.fields.language"/></th>
	</tr>
	<c:forEach items="${paginator.collection}" var="suc">
	<tr>
		<td>${suc.dumpId}</td>
		<td>${suc.entityClass}</td>			
		<td>${suc.entityId}</td>
		<td>${suc.when}</td>
		<td>${suc.language}</td>
	</tr>
	</c:forEach>
</table>