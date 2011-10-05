<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>
		
<h1><fmt:message key="catalog.course.list.section.header"/> (<fmt:message key="message.total.upper"/> ${paginator.size})</h1>
				
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
</c:if>
		
<c:forEach items="${paginator.collection}" var="course">
	<p>
	 	<a href="<c:url value='/${rc.locale.language}/catalog/${type}/course/detail/${course.courseId}'/>" title="${course.title}">
			${course.title}
		</a> 
		<%-- <a href="<c:url value='/courseCatalog?courseId=${course.courseId}&lang=${rc.locale.language}'/>" title="<fmt:message key="${course.title}"/>">
			${course.title}
		</a> --%>
	</p>			
</c:forEach>
		
