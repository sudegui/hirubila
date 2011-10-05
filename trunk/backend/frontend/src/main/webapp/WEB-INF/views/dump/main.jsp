<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>
<%@ include file="/WEB-INF/views/common/options_menu.jsp"%>
	
<h1><fmt:message key="dump.list.section.header"/> (<fmt:message key="message.total.upper"/> ${paginator.size})</h1>
		
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
    		
			
			</c:if>
			
		<table style="width:100%; border-spacing:0;">
			<tr>
				<th><fmt:message key="dump.main.fields.launchedDate"/></th>
				<th><fmt:message key="dump.main.fields.originClass"/></th>
				<th><fmt:message key="dump.main.actions"/></th>
			</tr>
 			<c:forEach items="${paginator.collection}" var="dump"> 
  				<tr>
  					<td><fmt:formatDate value="${dump.launched}" type="date" pattern="dd-MM-yyyy" /></td>
  					<td>${dump.ownerClass}/${dump.owner}</td>
  					<td>
  						<div class="options-menu">		
							<a class="expand-menu" href="#"><fmt:message key="generic.options.message"/></a>
							<br/>
							<ul class="content-options-menu" style="display: none">
								<li><a href="<c:url value='/${rc.locale.language}/dump/${dump.id}/events/parser-error/'/>"><fmt:message key="dump.main.action.feedError"/></a></li>
  								<li><a href="<c:url value='/${rc.locale.language}/dump/${dump.id}/events/store-error/'/>"><fmt:message key="dump.main.action.saveError"/></a></li>
  								<li><a href="<c:url value='/${rc.locale.language}/dump/${dump.id}/events/store-success/'/>"><fmt:message key="dump.main.action.successOperations"/></a></li>
  								<li><a href="<c:url value='/${rc.locale.language}/dump/${dump.id}/delete/'/>"><fmt:message key="dump.main.action.delete"/></a></li>	
							</ul>
						</div>
					</td>
 		 		</tr>
 		 	</c:forEach>
		</table>			
