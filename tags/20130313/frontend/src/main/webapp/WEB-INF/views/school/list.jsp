<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false" %>

<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<%@ include file="/WEB-INF/views/common/includes.jsp"%>
<%@ include file="/WEB-INF/views/common/options_menu.jsp"%>

		<h1><fmt:message key="school.list.section.header"/> (<fmt:message key="message.total.upper"/> ${paginator.size})</h1>
		
		<c:if test="${not empty provider}">
			<h4><fmt:message key="school.fields.providerInfomation"/></h4>
			<fmt:message key="provider.fields.name"/>: ${provider.name}
		</c:if>
		
		<%@ include file="/WEB-INF/views/common/paginator-tables.jsp"%>	
		
		<table style="width:100%; border-spacing:0;">
		  <thead>
			<tr>
				<th class="sortable" order="name"><fmt:message key="school.fields.name"/></th>
				<th><fmt:message key="generic.options.message"/></th>
			</tr>
		  </thead>
		  <tbody>
		  <c:forEach items="${paginator.collection}" var="school" varStatus="counter">
				<tr>			
					<td><a href="<c:url value='/${rc.locale.language}/school/detail/${school.id}'/>" title="${school.name}">${school.name}</a></td>
					<td>
						<div id="menu-item-${counter.count}" class="options-menu">		
							<a class="expand-menu" href="#menu-item-${counter.count}"><fmt:message key="generic.options.message"/></a>
							<br>
							<ul class="content-options-menu" style="display: none">
								<li>
									<a href="<c:url value='/${rc.locale.language}/school/${school.id}/courses/'/>" title="${school.name}">
										<fmt:message key="school.action.viewCourses"/></a>
								</li>
								<sec:authorize ifAllGranted="ROLE_AUTOMATIC_MEDIATOR">
								<li><a class="edit-multilanguage-link" href="/school/edit/${school.id}/" title="${school.name}">
									<fmt:message key="school.action.edit"/></a></li>
								</sec:authorize>
								
								<sec:authorize ifAnyGranted="ROLE_ADMIN,ROLE_AUTOMATIC_MEDIATOR">
								<li><a href="<c:url value='/${rc.locale.language}/i18n/translations/${school.id}'/>" title="${school.name}">
									<fmt:message key="generic.action.translations"/></a></li>
								</sec:authorize>		
								
								<sec:authorize ifAllGranted="ROLE_ADMIN">
								<li>
									<a href="<c:url value='/loader/update/school?schoolId=${school.id}'/>" title="${school.name}">
									<fmt:message key="action.dumpFeed"/>
									</a>
								</li>
								</sec:authorize>
								
								<sec:authorize ifAnyGranted="ROLE_ADMIN,ROLE_AUTOMATIC_MEDIATOR">
								<li><a href="<c:url value='/${rc.locale.language}/school/${school.id}/dumps'/>" title="${school.name}">
									<fmt:message key="provider.action.feedMonitoring"/></a></li>
								</sec:authorize>
								
								<sec:authorize ifAnyGranted="ROLE_ADMIN,ROLE_AUTOMATIC_MEDIATOR">
									<li>
										<a href="<c:url value='${school.feed}'/>" title="${school.name}" target="_blank">
										<fmt:message key="provider.action.showFeedContent"/></a>
									</li>
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
				<sec:authorize ifAllGranted="ROLE_AUTOMATIC_MEDIATOR">
					<td>
						<a href="<c:url value='/${rc.locale.language}/school/'/>" title="<fmt:message key='school.action.add'/>">
							<fmt:message key="school.action.add"/>
						</a>
					</td>
				</sec:authorize>
			</tr>
		</table>
		<%@ include file="/WEB-INF/views/common/dialog/multilanguage-dialog.jsp"%>
