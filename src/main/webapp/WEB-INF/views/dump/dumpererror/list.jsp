<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<h1><fmt:message key="error.list.section.header"/> (<fmt:message key="message.total.upper"/> ${paginator.size})</h1>

<c:if test="${paginator.size > 0 && (paginator.pageStart < paginator.pageEnd)}">
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
		<th><fmt:message key="dump.parseError.fields.object"/></th>			
		<th><fmt:message key="dump.parseError.fields.identifier"/></th>
		<th><fmt:message key="dump.parseError.fields.date"/></th>
		<th><fmt:message key="dump.parseError.fields.cause"/></th>
		<th><fmt:message key="dump.parseError.fields.message"/></th>
	</tr>
	<c:forEach items="${paginator.collection}" var="error">
	<tr>
		<td>${error.entityClass}</td>			
		<td>${error.entityId}</td>
		<td>${error.when}</td>
		<td class="description-collapse">${error.cause.value}</td>
	</tr>
	</c:forEach>
</table>