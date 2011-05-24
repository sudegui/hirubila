<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>
<%@ include file="/WEB-INF/views/common/includes.jsp"%>

<%@ include file="/WEB-INF/views/common/options_menu.jsp"%>

<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
		<h1><fmt:message key="school.list.section.header"/> (<fmt:message key="message.total.upper"/> ${paginator.size})</h1>
		
		
		<%@ include file="/WEB-INF/views/common/table-filter.jsp"%>			
		<c:if test="${paginator.size > 0 && (paginator.pageStart < paginator.pageEnd)}">
		<table>
			<tr>
			<c:if test="${paginator.currentPage != paginator.pageStart}">
				<td><a href="<c:url value='${paginator.urlBase}?page=${paginator.currentPage-1}&order=${order}&provinceId=${filterForm.provinceId}&regionId=${filterForm.regionId}&townId=${filterForm.townId}'/>"><fmt:message key="pagination.before"/></a></td>
			</c:if>
			<c:forEach items='${paginator.pagesIterator}' var='page'>
		  						<c:if test="${page == paginator.currentPage}">
		  							<td>${page}</td>
		  						</c:if>
		  						<c:if test="${page != paginator.currentPage}">
		  							<td><a href="<c:url value='${paginator.urlBase}?page=${page}&order=${order}&provinceId=${filterForm.provinceId}&regionId=${filterForm.regionId}&townId=${filterForm.townId}'/>" title="Schools">${page}</a></td>
		  						</c:if>
						</c:forEach>
						<c:if test="${paginator.currentPage != paginator.pageEnd}">
				<td><a href="<c:url value='${paginator.urlBase}?page=${paginator.currentPage+1}&order=${order}&provinceId=${filterForm.provinceId}&regionId=${filterForm.regionId}&townId=${filterForm.townId}'/>"><fmt:message key="pagination.next"/></a></td>
			</c:if>
			</tr>
		</table>	
		</c:if>
			
		<table style="width:100%; border-spacing:0;">
			<thead>
				<tr>
					<th><fmt:message key="school.fields.name"/></th>
					<th><fmt:message key="generic.options.message"/></th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${paginator.collection}" var="school" varStatus="counter">
				<tr>
					<td><a href="<c:url value='/${rc.locale.language}/extended/school/detail/${school.id}'/>" title="${school.name}">
					${school.name}</a></td>
					<td>
						<div id="menu-item-${counter.count}" class="options-menu">		
							<a class="expand-menu" href="#menu-item-${counter.count}"><fmt:message key="generic.options.message"/></a>
							<br>
							<ul class="content-options-menu" style="display: none">
								<li>
									<a href="<c:url value='/${rc.locale.language}/extended/school/${school.id}/courses/'/>" title="${school.name}">
										<fmt:message key="school.action.viewCourses"/></a>
								</li>
								<sec:authorize ifAllGranted="ROLE_MANUAL_MEDIATOR">
								<li>
									<a class="edit-multilanguage-link" href="/extended/school/edit/${school.id}/" title="${school.name}">
									<fmt:message key="generic.action.edit"/></a>
								</li>
								</sec:authorize>
								<sec:authorize ifAnyGranted="ROLE_ADMIN,ROLE_AUTOMATIC_MEDIATOR">
								<li><a href="<c:url value='/${rc.locale.language}/i18n/translations/${school.id}'/>" title="${school.name}">
									<fmt:message key="generic.action.translations"/></a></li>
								</sec:authorize>
						</ul>
						</div>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
				
		<table id="operations">
			<tr>
				<sec:authorize ifAllGranted="ROLE_MANUAL_MEDIATOR">
				<td>
					<a href="<c:url value='/${rc.locale.language}/extended/school/'/>" title="<fmt:message key='school.action.add'/>">
						<fmt:message key="school.action.add"/>
					</a>
				</td>
				</sec:authorize>
			</tr>
		</table>
		<%@ include file="/WEB-INF/views/common/dialog/multilanguage-dialog.jsp"%>
