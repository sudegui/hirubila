<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>

<%@ include file="/WEB-INF/views/common/includes.jsp"%>
<%@ include file="/WEB-INF/views/common/options_menu.jsp"%>

<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<h1><fmt:message key="mediation.list.section.header"/> (<fmt:message key="message.total.upper"/> ${paginator.size}) </h1>

<%@ include file="/WEB-INF/views/common/paginator-tables.jsp"%>	
		
<table style="width:100%; border-spacing:0;">	
	<thead>
		<tr>
			<th class="sortable" order="name"><fmt:message key="mediationService.fields.name"/></th>
			<th class="sortable" order="hasFeed"><fmt:message key="mediationService.fields.automatic"/></th>
			<th><fmt:message key="generic.options.operations"/></th>
		</tr>
	</thead>
	<tbody>
	<c:forEach items="${paginator.collection}" var="mediationService" varStatus="counter">
	<tr>			
		<td><a href="<c:url value='/${rc.locale.language}/mediation/detail/${mediationService.id}/'/>" title="${mediationService.name}">
			${mediationService.name}</a>
			</td>
		<td>${mediationService.hasFeed}</td>
		<td>
			<div id="menu-item-${counter.count}" class="options-menu">		
				<a class="expand-menu" href="#menu-item-${counter.count}"><fmt:message key="generic.options.message"/></a>
					<br/>
						<ul class="content-options-menu" style="display: none">
							<li>
								<a class="edit-multilanguage-link" href="<c:url value='/mediation/edit/${mediationService.id}/'/>" title="${mediationService.name}">
										<fmt:message key="generic.action.edit"/></a>
							</li>
							<li>
								<a href="<c:url value='/${rc.locale.language}/mediation/delete/${mediationService.id}/'/>" title="${mediationService.name}">
										<fmt:message key="generic.action.delete"/></a>
							</li>
						</ul>
					</div>
					</td>
	</tr>
	</c:forEach>
	</tbody>
</table>

<table id="operations">
	<tr>
		<sec:authorize ifAllGranted="ROLE_ADMIN">
			<td>
				<a href="<c:url value='/${rc.locale.language}/mediation/'/>" title='<fmt:message key="mediation.action.add"/>'>
					<fmt:message key="mediation.action.add"/>
				</a>
			</td>
		</sec:authorize>
	</tr>
</table>

<%@ include file="/WEB-INF/views/common/dialog/multilanguage-dialog.jsp"%>